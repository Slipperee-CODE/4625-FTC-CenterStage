package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;


public class LinearSlides {

    public State state = State.READY_TO_RECEIVE_PIXELS;
    public State prevState = null;
    public enum State {
        READY_TO_RECEIVE_PIXELS,
        LOW,
        MID,
        HIGH,
        ABOVE_TRUSS,
        RETURNED,
        OVERRIDE,
        IDLE,
    }

    private CustomGamepad overrideGamepad = null;
    public final float powerConstant = .5f;
    private Telemetry telemetry;
    private PIDMotor pidMotor = null;
    public final int readyToReceivePixelsTarget = 1000;
    public final int lowTarget = 1001;
    public final int midTarget = 1002;
    public final int highTarget = 1003;
    public final int aboveTrussTarget = 1004;
    public final int returnedTarget = 1005;

    public LinearSlides(HardwareMap hardwareMap, Telemetry telemetry)
    {
        initialize(hardwareMap);
        this.telemetry = telemetry;
    }


    private void initialize(HardwareMap hardwareMap)
    {
        pidMotor = new PIDMotor(hardwareMap.get(DcMotor.class, "LinearSlides"), 0.002, 0.0001, 0.000001);
        pidMotor.ResetPID();
    }


    public void Update()
    {
        switch(state)
        {
            case READY_TO_RECEIVE_PIXELS:
                prevState = State.READY_TO_RECEIVE_PIXELS;
                pidMotor.setTarget(readyToReceivePixelsTarget);
                state = LinearSlides.State.IDLE;
                break;

            case LOW:
                prevState = State.LOW;
                pidMotor.setTarget(lowTarget);
                state = LinearSlides.State.IDLE;
                break;

            case MID:
                prevState = State.MID;
                pidMotor.setTarget(midTarget);
                state = LinearSlides.State.IDLE;
                break;

            case HIGH:
                prevState = State.HIGH;
                pidMotor.setTarget(highTarget);
                state = LinearSlides.State.IDLE;
                break;

            case ABOVE_TRUSS:
                prevState = State.ABOVE_TRUSS;
                pidMotor.setTarget(aboveTrussTarget);
                state = LinearSlides.State.IDLE;
                break;

            case RETURNED:
                prevState = State.RETURNED;
                pidMotor.setTarget(returnedTarget);
                state = LinearSlides.State.IDLE;
                break;

            case OVERRIDE:
                prevState = State.OVERRIDE;
                pidMotor.motor.setPower(overrideGamepad.gamepad.left_stick_y*powerConstant);
                state = LinearSlides.State.IDLE;
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = State.READY_TO_RECEIVE_PIXELS;
        }

        if (state != State.OVERRIDE){
            pidMotor.Update(telemetry, .012);
        }
    }

    public void SetOverrideGamepad(CustomGamepad overrideGamepad)
    {
        this.overrideGamepad = overrideGamepad;
    }
}
