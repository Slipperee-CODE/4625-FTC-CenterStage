package org.firstinspires.ftc.teamcode.legacy.unused;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;


public class ActiveIntake {

    public State state = State.OFF;
    public State prevState = null;
    public enum State {
        OFF,
        FORWARD,
        REVERSE,
        OVERRIDE,
        IDLE,

    }

    private DcMotor motor = null;
    private CustomGamepad overrideGamepad = null;
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
                motor.setPower(0);
                state = State.IDLE;
                break;

            case FORWARD:
                prevState = State.FORWARD;
                motor.setPower(1 * powerConstant);
                state = State.IDLE;
                break;

            case REVERSE:
                prevState = State.REVERSE;
                motor.setPower(-1 * powerConstant);
                state = State.IDLE;
                break;

            case OVERRIDE:
                prevState = State.OVERRIDE;
                motor.setPower(overrideGamepad.gamepad.right_stick_y*powerConstant);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.OFF;
        }
    }

    public void SetOverrideGamepad(CustomGamepad overrideGamepad)
    {
        this.overrideGamepad = overrideGamepad;
    }
}
