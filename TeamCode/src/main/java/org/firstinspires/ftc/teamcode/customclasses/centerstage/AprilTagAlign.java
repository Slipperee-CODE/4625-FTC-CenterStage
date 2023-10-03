package org.firstinspires.ftc.teamcode.customclasses.centerstage;

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
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagPose;

import java.util.ArrayList;


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


    public void Update(Robot robot, ArrayList<AprilTagDetection> detectedTags, CustomGamepad overrideGamepad)
    {
        AprilTagDetection currentDetectedTag = null;
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

                if (detectedTags.size() != 0){
                    navigateToAprilTag(robot, ((int) Math.floor(bDPos/2.0)),bDPos, detectedTags);
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
    private double poseDistance(AprilTagPose pose) {
        return Math.sqrt(pose.x* pose.x + pose.y*pose.y);
    }
    private AprilTagDetection getStrongestDetection(ArrayList<AprilTagDetection> tags) {
        if (tags == null) return null;
        if (tags.size() == 0) return null;
        AprilTagDetection strongestDetection = tags.get(0);
        double shortestDistance = Double.POSITIVE_INFINITY;
        for (AprilTagDetection detection : tags) {
            if (poseDistance(detection.pose) < shortestDistance) {
                shortestDistance = poseDistance(detection.pose);
                strongestDetection = detection;
            }
        }
        return strongestDetection;
    }

    public void navigateToAprilTag(Robot drive, int bDPosMacro, int bDPosMicro, ArrayList<AprilTagDetection> currentTags)
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
        double dist = poseDistance(toDriveTo.pose);
        double goalDist = 1.0;
        double TURN_GAIN = 0.2;
        double STRAFE_GAIN = 0.5;
        double FORWARD_GAIN = .3;
        double MAX_FORWARD = 0.5;
        double MAX_STRAFE = 0.5;

        //we turn the robot a little bit
        if (dist > goalDist) {
            Orientation rot = Orientation.getOrientation(toDriveTo.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS);
            drive.emulateController(Math.min(FORWARD_GAIN * toDriveTo.pose.z, MAX_FORWARD), Math.min(STRAFE_GAIN * toDriveTo.pose.x, MAX_STRAFE), TURN_GAIN * rot.firstAngle);
        }
        /*
        //FIND bP from bDPos (index list)
        if (currentDetectedId > 3){
            targetID = redIDArray[bDPosMacro];
        }
        else {
            targetID = blueIDArray[bDPosMacro];
        }


        //Two Stages
        //Search Stage (Move in Correct Direction to Search for Tag, Check if targetID > or < currently detected tag
        //Get currentStrafePos and prevStrafePos from deadwheel odometry values
        float error = (float) (targetID - currentDetectedId);


        if (targetID > currentDetectedId){
            //roadRunnerError = strafePIDFromRoadRunner(drive, roadRunnerError, "r");
        }
        else if (targetID < currentDetectedId) {
            //roadRunnerError = strafePIDFromRoadRunner(drive, -roadRunnerError, "l");
        }
        else { //Found Stage
            if (bDPosMicro % 2 < 1){
                //Strafe left
                //use RobotAutoDriveToAprilTagOmni methods for this so that heading gets aligned


                //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
                //strafeTowardBd(error);
            }
            else {
                //Strafe right
                //use RobotAutoDriveToAprilTagOmni methods for this so that heading gets aligned


                //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
                //strafeTowardBd(error);
            }
        }

        telemetry.addData("Current Detected ID", currentDetectedId);
        telemetry.addData("Target ID", String.valueOf(targetID));
        */

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
