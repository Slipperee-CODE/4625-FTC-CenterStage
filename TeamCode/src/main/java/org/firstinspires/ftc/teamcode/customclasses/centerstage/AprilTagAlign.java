package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;
import org.openftc.apriltag.AprilTagDetection;


public class AprilTagAlign {

    public State state = State.OFF;
    public State prevState = null;
    private CustomGamepad overrideGamepad = null;
    private double p, i, d;
    public enum State {
        OFF,
        ON,
        IDLE,
    }

    private int[] blueIDArray = new int[]{0,1,2};
    private int[] redIDArray = new int[]{3,4,5};
    private int bDPos;

    private float STARTING_ERROR_BETWEEN_TAGS = 10; //in inches

    public AprilTagAlign(HardwareMap hardwareMap, double p, double i, double d)
    {
        initialize(hardwareMap, p, i, d);
    }


    private void initialize(HardwareMap hardwareMap, double p, double i, double d)
    {
        this.p = p;
        this.i = i;
        this.d = d;
    }


    public void Update(SampleMecanumDrive drive, AprilTagDetection currentDetectedTag)
    {
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

                navigateToAprilTag(drive, bDPos % 3,bDPos, currentDetectedTag);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.OFF;
        }
    }

    public void navigateToAprilTag(SampleMecanumDrive drive, int bDPosMacro, int bDPosMicro, AprilTagDetection currentDetectedTag)
    {
        int targetID;
        int currentDetectedId = currentDetectedTag.id;
        double roadRunnerError = 0;
        if (roadRunnerError == 0) {
            prevStrafePos = 0;
            roadRunnerError = STARTING_ERROR_BETWEEN_TAGS;
        }

        //NAVIGATE TO THE APRIL TAG WITH ID bP.id
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
        if (targetID > currentDetectedId){
            roadRunnerError = strafePIDFromRoadRunner(drive, roadRunnerError, "r");
        }
        else if (targetID < currentDetectedId) {
            roadRunnerError = strafePIDFromRoadRunner(drive, -roadRunnerError, "l");
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


    public void SetOverrideGamepad(CustomGamepad overrideGamepad)
    {
        this.overrideGamepad = overrideGamepad;
    }
}
