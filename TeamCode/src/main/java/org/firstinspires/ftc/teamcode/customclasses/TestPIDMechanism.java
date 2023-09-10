package org.firstinspires.ftc.teamcode.customclasses;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import android.sax.StartElementListener;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class TestPIDMechanism
{
    public PIDMotor motor = null;
    private boolean failedInit = false;

    public MotorState motorState = MotorState.IDLE;
    public enum MotorState {
        EXTENDED,
        RETRACTED,
        IDLE
    }


    public TestPIDMechanism(HardwareMap hardwareMap) throws Exception {
        initialize(hardwareMap);
    }


    private void initialize(HardwareMap hardwareMap) throws Exception {
        try {
            motor = new PIDMotor(hardwareMap.get(DcMotor.class, "testMotor"), 0.002, 0.0001, 0.000001);
        } catch (Exception err) {
            failedInit = true;
            MissingHardware.addMissingHardware("PID MOTOR!");
        }
    }


    public void Update(Telemetry telemetry)
    {
        if (failedInit) return;
        switch(motorState)
        {
            case EXTENDED:
                motor.setTarget(1000);
                motorState = MotorState.IDLE;
                break;

            case RETRACTED:
                motor.setTarget(-1000);
                motorState = MotorState.IDLE;
                break;

            case IDLE:
                //Do nothing just so the program isn't constantly setting the target, we can talk about whether or not this is necessary - Cai
                break;

            default:
                motorState = MotorState.IDLE;
        }

        motor.Update(telemetry,.012);
    }
}
