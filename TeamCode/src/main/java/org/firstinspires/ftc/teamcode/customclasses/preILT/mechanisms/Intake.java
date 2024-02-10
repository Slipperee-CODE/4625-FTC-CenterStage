package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;


public class Intake extends MechanismBase {
    private DcMotor motor;
    private Servo intakeRotator;
    private final float powerConstant = 1f;

    private MechanismState state;

    public Intake(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        motor = getHardware(DcMotor.class,"intake",hardwareMap);
        intakeRotator = hardwareMap.get(Servo.class, "intakeRotator");
        this.gamepad = gamepad;
    }


    public void update()
    {
        if (gamepad.downDown){ //Bottom 2 Pixels (1-2)
            intakeRotator.setPosition(0.1); //THIS NEEDS TO BE TUNED
        } else if (gamepad.rightDown){ //Pixels (3-4)
            intakeRotator.setPosition(0.1);  //THIS NEEDS TO BE TUNED
        } else if (gamepad.upDown){ // Pixel 5
            intakeRotator.setPosition(0.1);  //THIS NEEDS TO BE TUNED
        }

        if (gamepad.left_stick_y != 0){
            motor.setPower(gamepad.left_stick_y*powerConstant);
            state = MechanismState.ON;
            return;
        } else if (motor.getPower() != 0) {
            motor.setPower(0.0);
        }

        switch(state)
        {
            case OFF:
                motor.setPower(0);
                state = MechanismState.IDLE;

            case FORWARD:
                motor.setPower(powerConstant);
                state = MechanismState.IDLE;

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
        telemetry.addData("Intake State", state.toString());
    }

    public void setState(MechanismState state){
        this.state = state;
    }

    // I have no clue what this method was supposed to do so I'm commenting it out - Cai
/*
    public void setPower(boolean intaking) {
        if (intaking) {
            motor.setPower(powerConstant);
        } else {
            motor.setPower(-powerConstant);
        }
    }
 */

    public void stop() {motor.setPower(0);}
}
