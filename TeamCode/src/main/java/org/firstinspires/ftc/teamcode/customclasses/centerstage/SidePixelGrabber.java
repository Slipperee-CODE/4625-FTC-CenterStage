package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class SidePixelGrabber {

    public State state = State.GRABBING;
    public State prevState = null;
    public enum State {
        GRABBING,
        RETRACTED,
        IDLE,
    }

    private Servo servo = null;
    public final float retractedAngle = 0;
    public final float grabbingAngle = .25f;

    public SidePixelGrabber(HardwareMap hardwareMap)
    {
        initialize(hardwareMap);
    }


    private void initialize(HardwareMap hardwareMap)
    {
        servo = hardwareMap.get(Servo.class, "SidePixelGrabber");
    }


    public void Update()
    {
        switch(state)
        {
            case GRABBING:
                prevState = State.GRABBING;
                servo.setPosition(grabbingAngle);
                state = State.IDLE;
                break;

            case RETRACTED:
                prevState = State.RETRACTED;
                servo.setPosition(retractedAngle);
                state = State.IDLE;
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.RETRACTED;
        }
    }
}
