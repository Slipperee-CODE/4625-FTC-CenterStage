package org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.RobotDrivetrain;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.Robot;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.VisibleTagsStorage;

import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.List;


public class AprilTagAlign extends MechanismBase{
    private int bDPos;
    private final RobotDrivetrain robot;
    private final float STARTING_ERROR_BETWEEN_TAGS = 10; //in inches
    private final Telemetry telemetry;

    private int targetID = -1;

    private boolean isAligned;


    public AprilTagAlign(HardwareMap hardwareMap, Telemetry telemetry, CustomGamepad gamepad, RobotDrivetrain robot)
    {
       this.gamepad = gamepad;
       this.telemetry = telemetry;
       this.robot = robot;
    }

    public void setTargetID(int num) {
        this.targetID = num;
        targetID = Math.min(targetID,5); //CLIPPING to the range 0,5
    }
    public int getTargetID() { return targetID; }

    public void update()
    {
        List<AprilTagDetection> detectedTags = VisibleTagsStorage.stored;
        //change currentDetectedTag to list of all tags detected in frame
        switch (state) {
            case OFF:
                state = MechanismState.IDLE;
                break;
            case ON:
                if (gamepad != null){
                    if (gamepad.leftDown) {
                        targetID--;
                        targetID = Math.min(Math.max(targetID, 0), 5); //CLIPPING bDPos to the range 0,5
                    } else if (gamepad.rightDown) {
                        targetID++;
                        targetID = Math.min(Math.max(targetID, 0), 5); //CLIPPING bDPos to the range 0,5
                    }
                }
                if (detectedTags.size() == 0) {
                    robot.stop();
                } else {
                    navigateToAprilTag(detectedTags);
                }
                break;
            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = MechanismState.IDLE;
        }
    }

    public void navigateToAprilTag(List<AprilTagDetection> currentTags)
    {
        //HOW TO TUNE GAINS
        // set all to zero except one and change the number until it works
        // save it somewhere else and then repeat for all gains
        final double TURN_GAIN = 0.5; // tuned to 0.3
        final double STRAFE_GAIN = 4.0; // tuned to 3.0
        final double FORWARD_GAIN = 1.4;
        final double MAX_FORWARD = 0.3; // This should be determined by how fast the robot can move while maintaining a still image
        final double MAX_STRAFE = 0.5; // This should be determined by how fast the robot can move while maintaining a still image
        final double FORWARD_OFFSET = 0.3; // in meters.  Target distance between tag and bobot.

        AprilTagDetection toDriveTo = getStrongestDetection(currentTags);

        //if we get here then the variable "toDriveto" should be our target to drive to
        double forwardError = FORWARD_OFFSET-toDriveTo.rawPose.z;
        telemetry.addData("Forward Error: ",forwardError);
        Orientation rot = Orientation.getOrientation(toDriveTo.rawPose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotation
        final double forwardPower = FORWARD_GAIN * Math.tanh(forwardError);
        // + is right, - is left. also the greater for.firstAngle is, the less we wanna strafe
        final double strafePower = (toDriveTo.id == targetID || targetID < 0) ? STRAFE_GAIN * toDriveTo.rawPose.x : 0.3 * (targetID - toDriveTo.id) / rot.firstAngle;
        final double rotPower = TURN_GAIN * rot.firstAngle;

        robot.baseMoveRobot(Range.clip(strafePower,-MAX_STRAFE,MAX_STRAFE),Range.clip(forwardPower,-MAX_FORWARD,MAX_FORWARD),rotPower);
        isAligned = (
                Math.abs(forwardPower) < 0.05 &&
                Math.abs(strafePower) < 0.05 &&
                Math.abs(rotPower) < 0.05
                );
    }
    public boolean isAligned() {
        return isAligned;
    }



    // HELPER FUNCTIONS
    private double poseDistance(AprilTagPoseRaw pose) {
        return Math.sqrt(poseDistanceSqrd(pose));
    }
    private double poseDistanceSqrd(AprilTagPoseRaw pose) {
        return pose.x* pose.x + pose.z*pose.z;
    }
    private AprilTagDetection getStrongestDetection(List<AprilTagDetection> tags) {
        if (tags == null || tags.size() == 0) return null;
        AprilTagDetection strongestDetection = null;
        double shortestDistanceSqrd = Double.POSITIVE_INFINITY;
        for (AprilTagDetection detection : tags) {
            double dist = poseDistanceSqrd(detection.rawPose);
            if (dist < shortestDistanceSqrd) {
                shortestDistanceSqrd = dist;
                strongestDetection = detection;
            }
        }
        return strongestDetection;
    }

}
