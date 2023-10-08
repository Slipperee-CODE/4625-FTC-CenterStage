package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.centerstage.AprilTagAlignmentTestTeleop;
import org.firstinspires.ftc.teamcode.customclasses.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.firstinspires.ftc.vision.apriltag.AprilTagPoseRaw;
import org.firstinspires.ftc.vision.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.List;


public class AprilTagAlign {

    public State state = State.OFF;
    public State prevState = null;
    public enum State {
        OFF,
        ON,
        IDLE,
    }

    private int[] blueIDArray = new int[]{1,2,3};
    private int[] redIDArray = new int[]{4,5,6};
    private int bDPos;

    private float STARTING_ERROR_BETWEEN_TAGS = 10; //in inches

    public Telemetry telemetry;

    public AprilTagAlign(HardwareMap hardwareMap, Telemetry telemetry)
    {
       initialize(hardwareMap);
       this.telemetry = telemetry;
    }


    private void initialize(HardwareMap hardwareMap)
    {

    }


    public void Update(Robot robot, List<AprilTagDetection> detectedTags, CustomGamepad overrideGamepad)
    {
        org.firstinspires.ftc.vision.apriltag.AprilTagDetection currentDetectedTag = null;
        if (detectedTags.size() != 0) currentDetectedTag = detectedTags.get(0);
        //change currentDetectedTag to list of all tags detected in frame
        switch (state) {
            case OFF:
                prevState = State.OFF;
                //STOP ANY SEARCHING FOR APRIL TAGS AND MOVEMENT TOWARDS THEM (MIGHT NOT NEED TO DO ANYTHING)
                state = State.IDLE;
                break;

            case ON:
                prevState = State.ON;

                if (overrideGamepad.leftDown) {
                    bDPos--;
                } else if (overrideGamepad.rightDown) {
                    bDPos++;
                }

                bDPos = Math.min(Math.max(bDPos, 0), 5); //CLIPPING bDPos to the range 0,5

                if (detectedTags.size() != 0) {
                    navigateToAprilTag(robot, ((int) Math.floor(bDPos / 2.0)), bDPos, detectedTags);
                } else {
                    robot.stop();
                }
                telemetry.addData("bDPos Position",bDPos);

                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.OFF;
        }
    }
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
        double goalDist = .35; //
        double TURN_GAIN = 0.25; // tuned to 0.25
        double STRAFE_GAIN = 1.0; // tuned to 1.0
        double FORWARD_GAIN = 0.0;
        double MAX_FORWARD = 0.5;
        double MAX_STRAFE = 0.5;

        //we turn the robot a little bit
        if (dist > goalDist) {
            Orientation rot = Orientation.getOrientation(toDriveTo.rawPose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);
            drive.emulateController(Math.min(FORWARD_GAIN * -toDriveTo.rawPose.z, MAX_FORWARD), Math.max(-MAX_STRAFE,Math.min(STRAFE_GAIN * toDriveTo.rawPose.x, MAX_STRAFE)), TURN_GAIN * rot.firstAngle);
        }
    }

    private double prevStrafePos = 0;

    private double strafePIDFromRoadRunner(SampleMecanumDrive drive, double error, String direction){ //error in inches
        if (prevStrafePos == 0) { drive.getPoseEstimate().getX(); return error;}
        double currentStrafePos = drive.getPoseEstimate().getX();
        double posDifference = Math.abs(prevStrafePos - currentStrafePos);
        switch (direction){
            case ("left"):
            case ("l"):
                error -= posDifference;
                break;

            case ("right"):
            case ("r"):
                error += posDifference;
                break;
        }

        //PUT IMPLEMENTATION OF PID CONTROLLER HERE

        return error;
    }

    private void strafePIDFromAprilTags(int error){ //error in inches
        //Strafe left
        //use RobotAutoDriveToAprilTagOmni methods for this so that heading gets aligned


        //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
        //strafeTowardBd(error);
    }
}
