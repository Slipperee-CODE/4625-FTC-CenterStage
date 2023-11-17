package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;

public class Outtake extends MechanismBase {
    private static final float OVERRIDE_SPEED = 10.0f;

    public static final int DROP_PIXEL_MIN_POSITION = 500;
    public static final int DROP_PIXEL_MAX_POSITION = 3000;
    private boolean dropping = false;
    public final int readyToReceivePixelsTarget = 0;
    public final int readyToDropPixelsTarget = 1000;
    private PIDMotor SlidesMotor;
    private Servo DropAngler;
    private Servo Dropper;
    public final int lowTarget = 1001;
    public final int midTarget = 1002;
    public final int highTarget = 1003;

    public Outtake(HardwareMap hardwareMap, CustomGamepad gamepad){
        SlidesMotor = new PIDMotor(getHardware(DcMotor.class,"LinearSlides",hardwareMap),0.002,0.0001,0.000001);
        //try {
        //    SlidesMotor = new PIDMotor(hardwareMap.get(DcMotor.class, "LinearSlides"), 0.002, 0.0001, 0.000001);
        //}  catch (Exception ignored){
        //    MissingHardware.addMissingHardware("LinearSlides");
        //}
        try {
            DropAngler = hardwareMap.get(Servo.class, "OuttakeAngler");
        }  catch (Exception ignored){
            MissingHardware.addMissingHardware("OuttakeAngler");
        }
        try {
            Dropper = hardwareMap.get(Servo.class, "OuttakeDropper");
        }  catch (Exception ignored){
            MissingHardware.addMissingHardware("OuttakeDropper");
        }
        this.gamepad = gamepad;
        SlidesMotor.ResetPID();
    }
    public void setState(MechanismState newState) {
        this.state = newState;
        switch(state) {
            case LOW:
                SlidesMotor.setTarget(lowTarget);
                break;
            case MID:
                SlidesMotor.setTarget(midTarget);
                break;
            case HIGH:
                SlidesMotor.setTarget(highTarget);
                break;
        }
    }

    public void update()
    {
        if (gamepad.xDown) {
            dropping = !dropping;
            if (dropping) {
                SlidesMotor.setTarget(readyToDropPixelsTarget);
            } else {
                SlidesMotor.setTarget(readyToReceivePixelsTarget);
            }
        }
        if (dropping) {
            if (gamepad.left_stick_y != 0) {
                if (state != MechanismState.OVERRIDE) {
                    // this if statement is just so that the linear slides directly stop where they are at instead of trying to go to their previous target
                    SlidesMotor.setTarget(SlidesMotor.getPos());
                    setState(MechanismState.OVERRIDE);
                }
                SlidesMotor.setTarget(SlidesMotor.getTarget() + (int) (gamepad.left_stick_y * OVERRIDE_SPEED));
            }
        }
        SlidesMotor.Update();
    }

    public void update(Telemetry telemetry){
        update();
        telemetry.addData("LinearSlides State", state.toString());
    }
}
