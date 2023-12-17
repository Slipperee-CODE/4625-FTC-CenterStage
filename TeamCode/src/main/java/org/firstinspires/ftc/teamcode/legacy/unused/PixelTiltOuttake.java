package org.firstinspires.ftc.teamcode.legacy.unused;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class PixelTiltOuttake {

    public State state = State.STORING;
    public State prevState = null;
    public enum State {
        STORING,
        SCORING_TOP,
        SCORING_BOTTOM,
        IDLE,
    }

    private Servo servo = null;
    public final float storingAngle = 0;
    public final float scoringTopAngle = .25f;
    public final float scoringBottomAngle = .5f;

    public PixelTiltOuttake(HardwareMap hardwareMap)
    {
        initialize(hardwareMap);
    }


    private void initialize(HardwareMap hardwareMap)
    {
        servo = hardwareMap.get(Servo.class, "PixelTiltOuttake");
    }


    public void Update()
    {
        switch(state)
        {
            case STORING:
                prevState = State.STORING;
                servo.setPosition(storingAngle);
                state = State.IDLE;
                break;

            case SCORING_TOP:
                prevState = State.SCORING_TOP;
                servo.setPosition(scoringTopAngle);
                state = State.IDLE;
                break;

            case SCORING_BOTTOM:
                prevState = State.SCORING_BOTTOM;
                servo.setPosition(scoringBottomAngle);
                state = State.IDLE;
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.STORING;
        }
    }
}
