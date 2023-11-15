package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;

public class LinearSlides extends MechanismBase {
    private PIDMotor pidMotor;

    private final float powerConstant = .5f;

    public final int readyToReceivePixelsTarget = 1000;
    public final int lowTarget = 1001;
    public final int midTarget = 1002;
    public final int highTarget = 1003;

    public LinearSlides(HardwareMap hardwareMap, CustomGamepad gamepad){
        try {
            pidMotor = new PIDMotor(hardwareMap.get(DcMotor.class, "LinearSlides"), 0.002, 0.0001, 0.000001);
        }  catch (Exception ignored){
            MissingHardware.addMissingHardware("LinearSlides");
        }

        this.gamepad = gamepad;
        pidMotor.ResetPID();
    }

    //THE ENCODER VALUES NEED CAREFUL TESTING TO MAKE SURE THEY ARE RIGHT - CAI
    //TO DO:
        //IMPLEMENT PIXEL TILTER INSIDE THIS CLASS SO THAT WHEN YOU SET A HEIGHT IT AUTOMATICALLY HANDLES THE PIXEL TILTER
    public void update()
    {
        if (gamepad.right_stick_y != 0){
            pidMotor.motor.setPower(gamepad.right_stick_y*powerConstant);
            state = MechanismState.OVERRIDE;
            return;
        }

        switch(state)
        {
            case READY_TO_RECEIVE_PIXELS:
                pidMotor.setTarget(readyToReceivePixelsTarget);
                break;

            case LOW:
                pidMotor.setTarget(lowTarget);
                break;

            case MID:
                pidMotor.setTarget(midTarget);
                break;

            case HIGH:
                pidMotor.setTarget(highTarget);
                break;

            case OVERRIDE:
                pidMotor.setTarget(pidMotor.getPos());
                state = MechanismState.IDLE;
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = MechanismState.IDLE;
        }

        pidMotor.Update(.012);
    }

    public void update(Telemetry telemetry){
        update();
        telemetry.addData("LinearSlides State", state.toString());
    }
}
