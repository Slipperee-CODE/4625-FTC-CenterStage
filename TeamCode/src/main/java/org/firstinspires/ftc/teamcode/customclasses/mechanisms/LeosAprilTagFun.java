package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.customclasses.VisibleTagsStorage;
import org.firstinspires.ftc.teamcode.customclasses.webcam.SpeedyAprilTagPipeline;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagPose;

import java.util.ArrayList;
import java.util.List;

public class LeosAprilTagFun extends MechanismBase {
    // VALID STATES:
    // OFF: Not doing anything on purpose
    // IDLE: not detecting on the camera but we want to make sure our range is long so we set decimation to be ultra_low, exposure time should be semi high like 6 milliseconds
    // ON: alias for IDLE
    // FAR: we detecting far away, meaning more than 0.7 meter away, to avoid a sudden acceleration of the robot causing camera blurriness, we purposefully go slow on this part, decimation is still slow, exposure time should be turned to 5 milliseconds
    // NORMAL: we detect the april tag less than 0.7 meters // decimation should be set to normal
    // Footnotes: we will not be counting apriltags farther than 1.2 meters away as we would be on the other side of the field and might collid with the centerpiece if we tried to drive to it
    private Webcam webcam;

    private final Robot robot;
    private final SpeedyAprilTagPipeline aprilTagPipline;
    private boolean detectionsAreLatest = true;
    private int framesWithoutDetections;
    private int framesWithDetections;
    private int visionsInARowWithNoDetections = 0;
    public boolean useAngleToStrafe = false;  //// change this one to true to help maybe if not dont change

    private final double FORWARD_OFFSET = 0.15;
    private Telemetry telemetry;

    public int targetID = 1;
    private boolean startingActive = false;

    public LeosAprilTagFun(Telemetry telemetry, HardwareMap hardwareMap, Robot robot, Webcam webcam,boolean startActive) {
        this.robot = robot;
        this.state = MechanismState.OFF;
        this.webcam = webcam;
        this.telemetry = telemetry;
        aprilTagPipline = new SpeedyAprilTagPipeline(0.0765); /// tagsize is 0.05 for match in meters for the page sized tags
        this.startingActive = startActive;

    }
    public void init() {
        // before calling init the thing will not work and will allow other programs to use the webcam
        webcam.UseCustomPipeline(aprilTagPipline); //THIS IS THE LINE THAT CAUSES THE ILLEGAL ARGUMENT EXCEPTION
        webcam.setGain(webcam.getGain()+20); // Just TURN IT UP (woo woo!)
        if (startingActive) {setState(MechanismState.ON);}
        else {setState(MechanismState.OFF);};
    }

    public void update() {

        if (state == MechanismState.OFF) return;
        ArrayList<AprilTagDetection> detects = aprilTagPipline.getDetectionsUpdate();
        if (detects != null) {
            telemetry.addData("tags seen", detects.size());
        } else {
            telemetry.addData("tags seen", "Null");


        }
        if (detects != null) {
            VisibleTagsStorage.stored_native = detects;
            detectionsAreLatest = true;
        } else {
            detectionsAreLatest = false;
        }
        updateState();
        AprilTagPose guess;
        switch (state) {
            case IDLE:
                break;
            case FAR:
                navigateToPose(convertToCustom(getStrongestDetection(VisibleTagsStorage.stored_native).pose),1.0);
                //navigateToAprilTag(VisibleTagsStorage.stored_native,01.0);
                break;
            case NORMAL:
                //guess = getStrongestDetection(VisibleTagsStorage.stored_native);
                //if (guess == null) return;
                //telemetry.addData("X Rel: ",guess.x);
                //telemetry.addData("Z Rel: ", guess.z);
                //telemetry.addData("Tag Angle", guess.y);
                navigateToPose(convertToCustom(getStrongestDetection(VisibleTagsStorage.stored_native).pose),1.0);
                //navigateToAprilTag(VisibleTagsStorage.stored_native,1.0);

                //AprilTagPose p = guessRequestedPoseFromGotten(VisibleTagsStorage.stored_native,targetID);

                //telemetry.addData("Dynamic K", 0.8 / poseDistance(p));
                //navigateToPose(p,Math.min(0.6/poseDistance(p),1.0));
                break;
            default:
                state = MechanismState.OFF;

        }
    }
    private void updateState() {
        // here we should determine if we are FAR, IDLE, or NORMAL
        // we know that we have just gotten the latest and greatest batch of detections and also if these detections are failures like half of my projects
        if (!detectionsAreLatest) {
            framesWithoutDetections++;
            framesWithDetections = 0;
        }
        else {
            framesWithoutDetections = 0;
            framesWithDetections++;
        }
        // we can now set it to the states said in the first line of the method
        double dist;
        if (!detectionsAreLatest) return;
        if (VisibleTagsStorage.stored_native.size() == 0) {
            visionsInARowWithNoDetections++;
        } else {
            visionsInARowWithNoDetections = 0;
        }
        if (visionsInARowWithNoDetections >= 20) {
            //  we havent seen something in a while , we should just stop and idle
            robot.stop();
            setState(MechanismState.IDLE);
        }
        // if we are far then we need to
        // if we are idling then we need to check if we have seen the apriltags at least once and we just BOOK IT to them
        dist = getStrongestDetectionDist(VisibleTagsStorage.stored_native);
        // if we see nothing then
        if (dist == -1.0) {
            setState(MechanismState.IDLE);
        }// SOmething went wrong and we keep idling
        else if (dist > .8) {
            setState(MechanismState.FAR);
        }
        else  {
            setState(MechanismState.NORMAL);
        }


    }

    @Override
    public void setState(MechanismState state) {
        if (this.state == state) return;// this is so that our updateState script doesn't kill performance , with that setState every frame
        this.state = state;

        switch (state) {
            case OFF:
                webcam.stopStreaming();
                break;
            case ON:
                webcam.resumeStreaming();
                setState(MechanismState.IDLE);
                break;
            case IDLE:
                aprilTagPipline.setDecimation(10.0f);// decimation must be a float but i  dont really know how that works oh well. ¯\_(ツ)_/¯
                webcam.setExposure(7L);
                break;
            case FAR:
                webcam.setExposure(5L);
                aprilTagPipline.setDecimation(1.0f);
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
        final double FORWARD_GAIN = 0.0; // tuned to 1.3
        final double MAX_FORWARD = 0.5; // This should be determined by how fast the robot can move while still having a still image
        final double MAX_STRAFE = 0.5; // This should be determined by how fast the robot can move while still having a still image
        // FORWARD_OFFSET  in meters.  Forward distance between the marker to shoot for
        double forwardError = FORWARD_OFFSET + toDriveTo.pose.z;
        telemetry.addData("f error", forwardError);
        // there is some artifact when we shoot past our offset we actually back up too slowly, so whenever the error is negative we should double it
        if (forwardError < 0) {
            forwardError *= 1.0;
        }
        double addedStrafe = 0.0;
        Orientation rot = Orientation.getOrientation(toDriveTo.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotation

        if (useAngleToStrafe) {
            addedStrafe = rot.firstAngle * 0.9;
        }


        //"we do a little moving" - cai
        robot.emulateController(Math.min(FORWARD_GAIN * Math.tanh(forwardError), MAX_FORWARD)*k, k*(Math.max(-MAX_STRAFE,Math.min(STRAFE_GAIN * -toDriveTo.pose.x, MAX_STRAFE)) + addedStrafe), k*TURN_GAIN * rot.firstAngle);

    }
    public void navigateToPose(AprilTagPoseCustom pose, double k ) { ///////CHANGE THIS ONE
        if (pose == null) return;
        final double yAngle = pose.angleY; // we encode yangle into pose.y in the guesser so now we get it back
        final double TURN_GAIN = 1.0; // tuned to 0.3
        final double STRAFE_GAIN = 4.1; // change this two ^
        final double FORWARD_GAIN = 0.0; // tuned to 1.4
        final double MAX_FORWARD = 0.5; // This should be determined by how fast the robot can move while still having a still image
        final double MAX_STRAFE = 0.5; // This should be determined by how fast the robot can move while still having a still image
        //double FORWARD_OFFSET = 0.2; // in meters.  Forward distance between the marker to shoot for
        double forwardError = pose.z - FORWARD_OFFSET;
        // there is some artifact when we shoot past our offset we actually back up too slowly, so whenever the error is negative we should double it
        if (forwardError < 0) {
            forwardError *= 2.0;
        }
        double addedStrafe = 0.0;

        if (useAngleToStrafe) {
            addedStrafe = yAngle * 0.9;
        }


        //"we do a little moving" - cai probably
        robot.emulateController(Math.min(FORWARD_GAIN * Math.tanh(forwardError), MAX_FORWARD)*k,
                k*(Math.max(-MAX_STRAFE,Math.min(STRAFE_GAIN * pose.x, MAX_STRAFE)) + addedStrafe),
                k*TURN_GAIN * -yAngle);

    }


    // HELPER FUNCTIONS
    private AprilTagPose guessRequestedPoseFromGotten(List<AprilTagDetection> tags, int tagIDToGuess) {
        // This apriltaggueser will try to calculate the pose of requested tagId from a list of given apriltags
        // first check if wanted is in tags list
        if (tags == null || tags.size() == 0) return null;
        for (AprilTagDetection apriltag : tags) {
            if (apriltag.id == tagIDToGuess) {
                return apriltag.pose;
            }
        }
        // now since we have checked all the tags gotten and none match we will guess/ calculate the wanted id

        final double M = 0.2; // distance between tags in meters

        // now we have to chose which to calculate from, we could use all of them and take the weighted average pose using y angle to calulate weights (because the less the angle the more likely it is to be correct
        // we could use : the closest one, the one with the least rotation, the closest one to the requested one
        // for now we are using closest
        //get the closest tag because that is the one that has the least amount of chance to disappear when we move

        AprilTagDetection tag = getStrongestDetection(tags);

        if (tag == null) return null;
        telemetry.addData("Tag Seen is ",tag.id);
        telemetry.addData("tag wanted is", tagIDToGuess);
        int tagsOver =  tagIDToGuess-tag.id ;
        double x = tag.pose.x, y = tag.pose.z; // y = ..z is on purpose
        Orientation rot = Orientation.getOrientation(tag.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotation


        double[] v = getAngleUnitVector(-rot.firstAngle);
        x += v[0] * M * tagsOver;
        y += v[1] * M * tagsOver;

        AprilTagPose guess = new AprilTagPose();
        guess.x = x;
        guess.y = rot.firstAngle;
        guess.z = y;

        guess.R = tag.pose.R;


        return guess;
    }
    private double[] getAngleUnitVector(double angle) {
        return new double[] {Math.cos(angle),Math.sin(angle)};
    }
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
    private AprilTagPoseCustom convertToCustom(AprilTagPose pose) {
        AprilTagPoseCustom custom = new AprilTagPoseCustom();
        custom.x = pose.x;
        custom.z = pose.z;
        Orientation rot = Orientation.getOrientation(pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotation
        custom.angleY = rot.firstAngle;
        return custom;

    }
    private double getStrongestDetectionDist(List<AprilTagDetection> tags) {
        if (tags == null || tags.size() == 0) return -1.0;
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (AprilTagDetection detection : tags) {
            if (poseDistance(detection.pose) < shortestDistance) {
                shortestDistance = poseDistance(detection.pose);
            }
        }
        if (shortestDistance > 1.2) return -1.0; // if the distance is farther than 1.2 meters we dont count it
        return shortestDistance;
    }
    private class AprilTagPoseCustom {
        //protected AprilTagPoseCustom() {}
        double x;
        double z;
        double angleY;
    }
}
