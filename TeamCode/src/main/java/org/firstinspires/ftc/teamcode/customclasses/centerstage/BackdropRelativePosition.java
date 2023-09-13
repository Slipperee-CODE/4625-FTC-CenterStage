package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class BackdropRelativePosition {

    public State state = State.NONE;
    public State prevState = null;
    public enum State {
        NONE,
        FIRST_COLUMN,
        SECOND_COLUMN,
        THIRD_COLUMN,
        FOURTH_COLUMN,
        FIFTH_COLUMN,
        SIXTH_COLUMN,
        IDLE,
    }

    public final float distanceFromBackdrop = 0;

    public BackdropRelativePosition(HardwareMap hardwareMap)
    {
        initialize(hardwareMap);
    }


    private void initialize(HardwareMap hardwareMap)
    {

    }


    public void Update()
    {
        switch(state)
        {
            case NONE:
                prevState = State.NONE;
                //STOP ANY SEARCHING FOR APRIL TAGS AND MOVEMENT TOWARDS THEM (MIGHT NOT NEED TO DO ANYTHING)
                state = State.IDLE;
                break;

            //ADD CODE FOR NAVIGATING TO EACH APRIL TAG BASED OFF STATE (FIND ID OF APRIL TAG AND STRAFE ROBOT ACCORDINGLY TO CLOSE IN ON A CERTAIN LATERAL AND HORIZONTAL DISTANCE FROM THE TAG

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.NONE;
        }
    }
}
