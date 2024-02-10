package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

public class PixelQuickRelease extends MechanismBase {
    private Servo servo;
    public final static double OPEN_POS = 0.0;
    public final static double CLOSED_POS = 0.72;

    private MechanismState state;

    public PixelQuickRelease(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        servo = getHardware(Servo.class,"pixelQuickRelease",hardwareMap);
        this.gamepad = gamepad;
    }


    public void update()
    {
        if (gamepad.bToggle){
            state = MechanismState.CLOSED;
        } else {
            state = MechanismState.OPEN;
        }

        switch(state)
        {
            case OPEN:
                servo.setPosition(OPEN_POS);
                state = MechanismState.IDLE;

            case CLOSED:
                servo.setPosition(CLOSED_POS);
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
        telemetry.addLine("PQR Active");
    }

    public void setState(MechanismState state){
        this.state = state;
    }
}
