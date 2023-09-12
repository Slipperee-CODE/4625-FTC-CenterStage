package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;


public class ActiveIntake {

    public State state = State.OFF;
    public State prevState = null;
    public enum State {
        OFF,
        FORWARD,
        REVERSE,
        OVERRIDE,
        PLANE_SHOOTER,
        IDLE,

    }

    private DcMotor motor = null;
    public final float powerConstant = .5f;

    public ActiveIntake(HardwareMap hardwareMap)
    {
        initialize(hardwareMap);
    }


    private void initialize(HardwareMap hardwareMap)
    {
        motor = hardwareMap.get(DcMotor.class, "ActiveIntake");
    }


    public void Update()
    {
        switch(state)
        {
            case OFF:
                prevState = State.OFF;

                state = State.IDLE;
                break;

            case FORWARD:
                prevState = State.FORWARD;

                state = State.IDLE;
                break;

            case REVERSE:
                prevState = State.REVERSE;

                state = State.IDLE;
                break;

            case OVERRIDE:
                prevState = State.OVERRIDE;

                state = State.IDLE;
                break;

            case PLANE_SHOOTER:
                prevState = State.PLANE_SHOOTER;

                state = State.IDLE;
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.OFF;
        }
    }
}
