package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.customclasses.VisibleTagsStorage;

import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;


public class AprilTagAlign extends MechanismBase{
    private int bDPos;

    private final Robot robot;
    private final float STARTING_ERROR_BETWEEN_TAGS = 10; //in inches

    private final Telemetry telemetry;

    public AprilTagAlign(HardwareMap hardwareMap, Telemetry telemetry, CustomGamepad gamepad,Robot robot)
    {
       // try {
       //     DcMotor m = hardwareMap.get(DcMotor.class, "BLAH BLAH BLAH");
       // } catch (Exception ignored) {
       //     MissingHardware.addMissingHardware("TEST: in AprilTagAlign");
       // }
       this.gamepad = gamepad;
       this.telemetry = telemetry;
       this.robot = robot;
    }

    public void update()
    {
        List<AprilTagDetection> detectedTags = VisibleTagsStorage.stored;
        //change currentDetectedTag to list of all tags detected in frame
        switch (state) {
            case OFF:
                state = MechanismState.IDLE;
                break;
            case ON:
                if (gamepad.leftDown) {
                    bDPos--;
                } else if (gamepad.rightDown) {
                    bDPos++;
                }
                bDPos = Math.min(Math.max(bDPos, 0), 5); //CLIPPING bDPos to the range 0,5

                if (detectedTags.size() != 0) {
                    navigateToAprilTag(robot, bDPos / 2, bDPos, detectedTags);
                } else {
                    robot.stop();
                }
                telemetry.addData("bDPos Position",bDPos);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = MechanismState.IDLE;
        }
    }

    public void navigateToAprilTag(Robot drive, int bDPosMacro, int bDPosMicro, List<AprilTagDetection> currentTags)
    {
        // We are guaranteed that currentTags.size > 0
        int targetID;
        int currentDetectedId = getStrongestDetection(currentTags).id;
        AprilTagDetection toDriveTo = getStrongestDetection(currentTags);

        //double roadRunnerError = 0;
        //if (roadRunnerError == 0) {
        //    prevStrafePos = 0;
        //    roadRunnerError = STARTING_ERROR_BETWEEN_TAGS;
        //}
        double dist = poseDistance(toDriveTo.rawPose); //distance is in meters because pose is meters
        telemetry.addData("Distance: ",dist);
        //HOW TO TUNE GAINS
        // set all to zero except one and change the number until it works
        // save it somewhere else and then repeat for all gains
        final double TURN_GAIN = 0.25; // tuned to 0.25
        final double STRAFE_GAIN = 1.0; // tuned to 1.0
        final double FORWARD_GAIN = 0.7;
        final double MAX_FORWARD = 0.7; // This should be determined by how fast the robot can move while still having a still image
        final double MAX_STRAFE = 0.5; // This should be determined by how fast the robot can move while still having a still image
        double FORWARD_OFFSET = 0.3; // in meters.  Forward distance between the marker to shoot for
        double forwardError = -toDriveTo.rawPose.z - FORWARD_OFFSET;

        //we turn the robot a little bit

        Orientation rot = Orientation.getOrientation(toDriveTo.rawPose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotation
        drive.emulateController(Math.min(FORWARD_GAIN * Math.tanh(forwardError), MAX_FORWARD), Math.max(-MAX_STRAFE,Math.min(STRAFE_GAIN * toDriveTo.rawPose.x, MAX_STRAFE)), TURN_GAIN * rot.firstAngle);

    }


    // HELPER FUNCTIONS
    private double poseDistance(AprilTagPoseRaw pose) {
        return Math.sqrt(pose.x* pose.x + pose.z*pose.z);
    }
    private AprilTagDetection getStrongestDetection(List<AprilTagDetection> tags) {
        if (tags == null) return null;
        if (tags.size() == 0) return null;
        AprilTagDetection strongestDetection = tags.get(0);
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (AprilTagDetection detection : tags) {
            if (poseDistance(detection.rawPose) < shortestDistance) {
                shortestDistance = poseDistance(detection.rawPose);
                strongestDetection = detection;
            }
        }
        return strongestDetection;
    }

}
