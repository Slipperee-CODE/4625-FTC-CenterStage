package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public class ActiveIntake extends MechanismBase {
    private DcMotor motor;
    private final float powerConstant = .5f;

    public ActiveIntake(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        try {
            motor = hardwareMap.get(DcMotor.class, "ActiveIntake");
        } catch (Exception ignored) {
            MissingHardware.addMissingHardware("ActiveIntake");
        }

        this.gamepad = gamepad;
    }

    public void update()
    {
        if (gamepad.right_stick_y != 0){
            motor.setPower(gamepad.right_stick_y*powerConstant);
            state = MechanismState.OFF;
            return;
        }

        switch(state)
        {
            case OFF:
                motor.setPower(0);
                state = MechanismState.IDLE;
                break;

            case FORWARD:
                motor.setPower(powerConstant);
                break;

            case REVERSE:
                motor.setPower(-powerConstant);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = MechanismState.IDLE;
        }
    }

    public void update(Telemetry telemetry)
    {
        update();
        telemetry.addData("ActiveIntake State", state.toString());
    }
}
