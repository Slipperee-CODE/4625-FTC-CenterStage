package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public class IntakeAngler extends MechanismBase implements Mechanism {
    private Servo servo;
    private MechanismState state = MechanismState.IDLE;

    public IntakeAngler(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        servo = getHardware(Servo.class,"IntakeAngler",hardwareMap);
        this.gamepad = gamepad;
        servo.setPosition(0);
    }

    public void update()
    {
        if (gamepad.upDown){
            //this.setState(MechanismState.HIGH);
            servo.setPosition(servo.getPosition()+0.1);
        }
        else if (gamepad.downDown){
            //this.setState(MechanismState.LOW);
            servo.setPosition(servo.getPosition()-0.1);
        }
    }

    public void update(Telemetry telemetry)
    {
        this.update();
        //telemetry.addData("IntakeAngler State", state.toString());
        telemetry.addData("INTAKE ANGLER SERVO POS",servo.getPosition());
    }

    public void setState(MechanismState state){
        this.state = state;
        switch(state)
        {
            case HIGH:
                servo.setPosition(0);
                break;

            case MID:
                servo.setPosition(.5);
                break;

            case LOW:
                servo.setPosition(.85);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = MechanismState.IDLE;
        }
    }
}
