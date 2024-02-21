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

    private static final float FULL_UP = 1f;
    private static final float FULL_DOWN = 0.47f;
    private static final float PIXEL2HEIGHT = 0.47f + 0.05f;
    private static final float PIXEL3HEIGHT = 0.47f + 0.1f;
    private static final float PIXEL4HEIGHT = 0.47f + 0.15f;

    private float[] PIXEL_HEIGHTS = {FULL_DOWN, PIXEL2HEIGHT, PIXEL3HEIGHT, PIXEL4HEIGHT, FULL_UP};

    private int pixelHeightIndex = 4;


    private MechanismState state = MechanismState.IDLE;

    public Intake(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        motor = getHardware(DcMotor.class,"intake",hardwareMap);
        intakeRotator = hardwareMap.get(Servo.class, "intakeRotator");
        intakeRotator.setPosition(FULL_UP);
        this.gamepad = gamepad;
    }


    public void update()
    {
        if (gamepad.downDown){ //Bottom 2 Pixels (1-2)
            if (pixelHeightIndex > 0){
                pixelHeightIndex--;
            }
            intakeRotator.setPosition(PIXEL_HEIGHTS[pixelHeightIndex]);
        } else if (gamepad.upDown) {
            if (pixelHeightIndex < PIXEL_HEIGHTS.length-1){
                pixelHeightIndex++;
            }
            intakeRotator.setPosition(PIXEL_HEIGHTS[pixelHeightIndex]);
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
        telemetry.addData("Intake Level", pixelHeightIndex);
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
