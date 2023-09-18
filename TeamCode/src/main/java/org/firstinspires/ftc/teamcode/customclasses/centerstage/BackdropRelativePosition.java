package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.openftc.apriltag.AprilTagDetection;


public class BackdropRelativePosition {

    public State state = State.OFF;
    public State prevState = null;
    private CustomGamepad overrideGamepad = null;
    public enum State {
        OFF,
        ON,
        IDLE,
    }

    private int[] blueIDArray = new int[]{0,1,2};
    private int[] redIDArray = new int[]{3,4,5};
    private int bDPos;
    private float prevStrafePos;
    private float movementSinceStartOfStrafe;
    private float STARTING_ERROR_BETWEEN_TAGS = 10; //in inches

    public BackdropRelativePosition(HardwareMap hardwareMap)
    {
        initialize(hardwareMap);
    }


    private void initialize(HardwareMap hardwareMap)
    {

    }


    public void Update(AprilTagDetection currentDetectedTag)
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

                navigateToAprilTag(bDPos % 3,bDPos, currentDetectedTag);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.OFF;
        }
    }

    public void navigateToAprilTag(int bDPosMacro, int bDPosMicro, AprilTagDetection currentDetectedTag)
    {
        int targetID;
        int currentDetectedId = currentDetectedTag.id;

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
            //float error = STARTING_ERROR_BETWEEN_TAGS;
            //movementSinceStartOfStrafe += abs(prevStrafePos - currentStrafePos)
            //prevStrafePos = currentStrafePos
            //error -= movementSinceStartOfStrafe;
            //strafeTowardsBd(error);
        }
        else if (targetID < currentDetectedId) {
            //float error = STARTING_ERROR_BETWEEN_TAGS;
            //movementSinceStartOfStrafe += abs(prevStrafePos - currentStrafePos)
            //prevStrafePos = currentStrafePos
            //error -= movementSinceStartOfStrafe;
            //strafeTowardsBd(-error);
        }
        else { //Found Stage
            if (bDPosMicro % 2 < 1){
                //Strafe left
                //use RobotAutoDriveToAprilTagOmni methods for this


                //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
                //strafeTowardBd(error);

            }
            else {
                //Strafe right
                //use RobotAutoDriveToAprilTagOmni methods for this


                //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
                //strafeTowardBd(error);
            }
        }
    }

    private void strafeTowardsBd(float error){ //error in inches
        //PID CONTROLLER FOR MOTOR POWER STRAFING BASED OFF ERROR OF DISTANCE FROM TAG (WHEN TAG NOT SEEN ESTIMATE ERROR USING REAL WORLD MEASUREMENTS)
    }

    public void SetOverrideGamepad(CustomGamepad overrideGamepad)
    {
        this.overrideGamepad = overrideGamepad;
    }
}
