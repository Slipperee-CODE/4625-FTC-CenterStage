package org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

public class ActiveIntake extends MechanismBase {
    private DcMotor motor;
    private final float powerConstant = 1f;
    private MechanismState state = MechanismState.IDLE;

    public ActiveIntake(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        motor = getHardware(DcMotor.class,"ActiveIntake",hardwareMap);
        this.gamepad = gamepad;
    }


    public void update()
    {
        if (gamepad.left_stick_y != 0){
            motor.setPower(gamepad.left_stick_y*powerConstant);
            state = MechanismState.IDLE;
            return;
        } else if (motor.getPower() != 0) {
            motor.setPower(0.0);
        }

        switch(state)
        {
            case OFF:
                motor.setPower(0);
                state = MechanismState.IDLE;
                break;

            case FORWARD:
                motor.setPower(powerConstant);
                state = MechanismState.IDLE;
                break;

            case REVERSE:
                motor.setPower(-powerConstant);
                state = MechanismState.IDLE;
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

    public void setState(MechanismState state){
        this.state = state;
    }
    public void setPower(boolean intaking) {
        if (intaking) {
            motor.setPower(powerConstant);
        } else {
            motor.setPower(-powerConstant);
        }
    }
    public void stop() {motor.setPower(0);}
}
