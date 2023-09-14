package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

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

    private BackdropPosition[] blueBdPosArray = new BackdropPosition[]{}; //when indexing subtract 1 from id to find the position in the list
    private BackdropPosition[] redBdPosArray = new BackdropPosition[]{}; //make these lists sets 3 of BackdropPositions which reference eachother
    private int bDPos;
    private float prevStrafePos;
    private float movementSinceStartOfStrafe;
    private float STARTING_ERROR_BETWEEN_TAGS = 10;

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

                bDPos = Math.min(Math.max(bDPos, 6), 1); //CLIPPING bDPos to the range 1,6

                //navigateToAprilTag([Find a way to make 1,2,3 and 4,5,6 map to 1,2,3] ,bDPos, currentDetectedTag);
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
        BackdropPosition bP;
        int currentDetectedId = currentDetectedTag.id;

        //NAVIGATE TO THE APRIL TAG WITH ID bP.id
        //FIND bP from bDPos (index list)
        if (currentDetectedId > 3){
            bP = redBdPosArray[bDPosMacro-1];
        }
        else {
            bP = blueBdPosArray[bDPosMacro-1];
        }

        //Two Stages
        //Search Stage (Move in Correct Direction to Search for Tag, Check if bP.id > or < currently detected tag
        //Get currentStrafePos and prevStrafePos from deadwheel odometry values
        if (bP.id > currentDetectedId){
            //float error = STARTING_ERROR_BETWEEN_TAGS;
            //movementSinceStartOfStrafe += abs(prevStrafePos - currentStrafePos)
            //prevStrafePos = currentStrafePos
            //error -= movementSinceStartOfStrafe;
            //strafeTowardsBd(error);
        }
        else if (bP.id < currentDetectedId) {
            //float error = STARTING_ERROR_BETWEEN_TAGS;
            //movementSinceStartOfStrafe += abs(prevStrafePos - currentStrafePos)
            //prevStrafePos = currentStrafePos
            //error -= movementSinceStartOfStrafe;
            //strafeTowardsBd(-error);
        }
        else { //Found Stage
            if (bDPosMicro < 1){
                //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
                //strafeTowardBd(error);
            }
            else {
                //float error = TARGET_DISTANCE_TO_LEFT - CURRENT_VALUE_BASED_OFF_APRIL_TAG_POSE;
                //strafeTowardBd(error);
            }
        }
    }

    private void strafeTowardsBd(float error){ //error in inches
        //PID CONTROLLER FOR MOTOR POWER STRAFING BASED OFF ERROR OF DISTANCE FROM TAG (WHEN TAG NOT SEEN ESTIMATE ERROR USING REAL WORLD MEASUREMENTS, ONCE VISIBLE, USE ACTUAL NUMBER)
    }

    public void SetOverrideGamepad(CustomGamepad overrideGamepad)
    {
        this.overrideGamepad = overrideGamepad;
    }
}
