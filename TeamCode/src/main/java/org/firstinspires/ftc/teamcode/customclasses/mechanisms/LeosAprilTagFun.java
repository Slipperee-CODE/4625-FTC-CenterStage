package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.customclasses.VisibleTagsStorage;
import org.firstinspires.ftc.teamcode.customclasses.webcam.SpeedyAprilTagPipeline;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagPose;

import java.util.List;

public class LeosAprilTagFun extends MechanismBase {
    // VALID STATES:
    // OFF: Not doing anything on purpose
    // IDLE: not detecting on the camera but we want to make sure our range is long so we set decimation to be ultra_low, exposure time should be semi high like 6 milliseconds
    // ON: alias for IDLE
    // FAR: we detecting far away, meaning more than 0.7 meter away, to avoid a sudden acceleration of the robot causing camera blurriness, we purposefully go slow on this part, decimation is still slow, exposure time should be turned to 5 milliseconds
    // NORMAL: we detect the april tag less than 0.7 meters // decimation should be set to normal
    // Footnotes: we will not be counting apriltags farther than 1.2 meters away as we would be on the other side of the field and might collid with the centerpiece if we tried to drive to it
    private final Webcam webcam;

    private final Robot robot;
    private final SpeedyAprilTagPipeline aprilTagPipline;

    public LeosAprilTagFun(Telemetry telemetry, HardwareMap hardwareMap, Robot robot) {
        this.robot = robot;
        this.state = MechanismState.OFF;
        webcam = new Webcam(hardwareMap);
        aprilTagPipline = new SpeedyAprilTagPipeline(0.171); /// tagsize is in meters for the page sized tags
        webcam.UseCustomPipeline(aprilTagPipline);
        webcam.setGain(webcam.getGain()+20);
        setState(MechanismState.OFF);
    }


    public void update() {
        switch (state) {
            case OFF:
                break;
            case IDLE:
                VisibleTagsStorage.stored_native = aprilTagPipline.getDetectionsUpdate();
                break;
            case FAR:
                VisibleTagsStorage.stored_native = aprilTagPipline.getDetectionsUpdate();
                navigateToAprilTag(VisibleTagsStorage.stored_native,0.3);
                break;
            case NORMAL:
                VisibleTagsStorage.stored_native = aprilTagPipline.getDetectionsUpdate();
                navigateToAprilTag(VisibleTagsStorage.stored_native,1.0);
                break;
            default:
                state = MechanismState.OFF;
        }

    }

    @Override
    public void setState(MechanismState state) {
        this.state = state;
        switch (state) {
            case OFF:
                webcam.stopStreaming();
            case ON:
                webcam.resumeStreaming();
                setState(MechanismState.IDLE);
                break;
            case IDLE:
                aprilTagPipline.setDecimation(2.0f);// decimation must be a float but i  dont really know how that works oh well. ¯\_(ツ)_/¯
                webcam.setExposure(7L);
                break;
            case FAR:
                webcam.setExposure(6L);
                break;
            case NORMAL:
                aprilTagPipline.setDecimation(3.0f);
                webcam.setExposure(4L);
        }
    }
    public void navigateToAprilTag(List<AprilTagDetection> currentTags, double k)
    {
        // We are guaranteed that currentTags.size > 0

        AprilTagDetection toDriveTo = getStrongestDetection(currentTags);


        //double dist = poseDistance(toDriveTo.rawPose); //distance is in meters because pose is meters
        //HOW TO TUNE GAINS
        // set all to zero except one and change the number until it works
        // save it somewhere else and then repeat for all gains
        final double TURN_GAIN = 0.5; // tuned to 0.3
        final double STRAFE_GAIN = 4.0; // tuned to 3.0
        final double FORWARD_GAIN = 1.4;
        final double MAX_FORWARD = 0.5; // This should be determined by how fast the robot can move while still having a still image
        final double MAX_STRAFE = 0.5; // This should be determined by how fast the robot can move while still having a still image
        double FORWARD_OFFSET = 0.3; // in meters.  Forward distance between the marker to shoot for
        double forwardError = FORWARD_OFFSET - toDriveTo.pose.z;
        // there is some artifact when we shoot past our offset we actually back up too slowly, so whenever the error is negative we should double it
        if (forwardError < 0) {
            forwardError *= 2.0;
        }


        //"we do a little moving" - cai probably
        Orientation rot = Orientation.getOrientation(toDriveTo.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotation
        robot.emulateController(Math.min(FORWARD_GAIN * Math.tanh(forwardError), MAX_FORWARD)*k, k*Math.max(-MAX_STRAFE,Math.min(STRAFE_GAIN * toDriveTo.pose.x, MAX_STRAFE)), k*TURN_GAIN * rot.firstAngle);

    }


    // HELPER FUNCTIONS
    private double poseDistance(AprilTagPose pose) {
        return Math.sqrt(pose.x * pose.x + pose.z*pose.z);
    }
    private AprilTagDetection getStrongestDetection(List<AprilTagDetection> tags) {
        if (tags == null || tags.size() == 0) return null;
        AprilTagDetection strongestDetection = tags.get(0);
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (AprilTagDetection detection : tags) {
            if (poseDistance(detection.pose) < shortestDistance) {
                shortestDistance = poseDistance(detection.pose);
                strongestDetection = detection;
            }
        }
        if (shortestDistance > 1.2) return null; // if the distance is farther than 1.2 meters we dont count it
        return strongestDetection;
    }
}
