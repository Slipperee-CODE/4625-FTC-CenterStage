package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public class IntakeAngler extends MechanismBase {
    private Servo servo;
    private MechanismState state = MechanismState.IDLE;

    public IntakeAngler(HardwareMap hardwareMap)
    {
        try {
            servo = hardwareMap.servo.get("IntakeAngler");
        } catch (Exception ignored) {
            MissingHardware.addMissingHardware("IntakeAngler");
        }
    }

    public void update()
    {
        switch(state)
        {
            case HIGH:
                servo.setPosition(1);
                state = MechanismState.IDLE;
                break;

            case MID:
                servo.setPosition(.5);
                state = MechanismState.IDLE;
                break;

            case LOW:
                servo.setPosition(0);
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
        telemetry.addData("IntakeAngler State", state.toString());
    }

    public void setState(MechanismState state){
        this.state = state;
    }
}
