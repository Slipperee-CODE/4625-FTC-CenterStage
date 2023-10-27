package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public class IntakeAngler extends MechanismBase implements Mechanism {
    private Servo servo;
    private MechanismState state = MechanismState.IDLE;

    public IntakeAngler(HardwareMap hardwareMap)
    {
        servo = hardwareMap.get(Servo.class,"IntakeAngler");
    }

    public void update()
    {
        switch(state)
        {
            case HIGH:
                servo.setPosition(1);
                break;

            case MID:
                servo.setPosition(.5);
                break;

            case LOW:
                servo.setPosition(0);
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
        this.update();
        telemetry.addData("IntakeAngler State", state.toString());
    }

    public void setState(MechanismState state){
        this.state = state;
    }
}
