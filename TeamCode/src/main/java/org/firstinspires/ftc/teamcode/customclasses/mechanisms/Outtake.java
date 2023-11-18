package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;

public class Outtake extends MechanismBase {
    private static final float OVERRIDE_SPEED = 10.0f;

    public static final int DROP_PIXEL_MIN_POSITION = 500; // position for linear slides
    public static final int DROP_PIXEL_MAX_POSITION = 3000; // position for linear slides
    public final int readyToReceivePixelsTarget = 0; // position for linear slides
    public final int readyToDropPixelsTarget = 1000; // position for linear slides
    public static final double OUTTAKE_RECIEVE_ANGLER_POSITION = 1;//1.0; // position for dropAngler
    public static final double OUTTAKE_DROP_ANGLER_POSITION_LOWER = 0.7; // position for dropAngler
    public static final double OUTTAKE_DROP_ANGLER_POSITION_NORMAL = 0.196; // position for dropAngler
    public static final double LID_RECIVE_POSITION  = .61; // position for LidAngler
    public static final double LID_DROP_POSITION  = .466; // position for LidAngler
    public static final double OUTTAKE_CLOSED_POSITION = .445; // position for Dropper
    public static final double OUTTAKE_OPEN_POSITION = 0.075; // position for Dropper
    public static final float STARTING_JOYSTICK_THRESHOLD = 0.2f;
    private boolean slidesUp = false;
    private boolean recievingPixel = false;
    private DcMotor SlidesMotor;
    private Servo DropAngler;
    private Servo LidAngler;
    private Servo Dropper;
    public final int lowTarget = 1001;
    public final int midTarget = 1002;
    public final int highTarget = 1003;

    public Outtake(HardwareMap hardwareMap, CustomGamepad gamepad){
        SlidesMotor = getHardware(DcMotor.class,"idunno",hardwareMap);
        SlidesMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LidAngler = getHardware(Servo.class,"OuttakeLidAngler",hardwareMap);
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
        setReceivePosition();
    }
    public void setState(MechanismState newState) {
        this.state = newState;

    }
    private void setReceivePosition() {
        //SlidesMotor.setTarget(readyToReceivePixelsTarget);
        DropAngler.setPosition(OUTTAKE_RECIEVE_ANGLER_POSITION);
        LidAngler.setPosition(LID_RECIVE_POSITION);
        Dropper.setPosition(OUTTAKE_OPEN_POSITION);
    }
    private void setDropLowerPosition() {
        //SlidesMotor.setTarget(readyToDropPixelsTarget);
        DropAngler.setPosition(OUTTAKE_DROP_ANGLER_POSITION_LOWER);
        LidAngler.setPosition(LID_DROP_POSITION);
        Dropper.setPosition(OUTTAKE_OPEN_POSITION); // here we can directly open the outtake because we are sure that we are in the correct spot to drop it
    }
    private void setDropNormalPosition() {
        //SlidesMotor.setTarget(Math.max(SlidesMotor.getTarget(),DROP_PIXEL_MIN_POSITION));
        LidAngler.setPosition(LID_DROP_POSITION);
        DropAngler.setPosition(OUTTAKE_DROP_ANGLER_POSITION_NORMAL);
        // here we CANNOT open the dropper as we still have to align ourselves to where we want to drop it at
        Dropper.setPosition(OUTTAKE_CLOSED_POSITION); // we close it just to make sure that out pixel doesn't fall before we are at the right spot
    }


    public void update()
    {
        if (gamepad.xDown ) {
            if ((slidesUp || !recievingPixel)) { // set them down
                if (Dropper.getPosition() == OUTTAKE_OPEN_POSITION) {
                    slidesUp = false;
                    recievingPixel = true;
                    setReceivePosition();
                } else if (Dropper.getPosition() == OUTTAKE_CLOSED_POSITION) {
                    Dropper.setPosition(OUTTAKE_OPEN_POSITION);
                }
            } else if(recievingPixel && !slidesUp ) { // if we are recieving the pixel then as a sanity check we make sure the slides are down
                // here since the use hasn't done anything to point that they want to go up we assume that they just want to deposit them down
                recievingPixel = false;
                setDropLowerPosition();
            }
        }
        if (!slidesUp) { // means we are at are either recieving or dropping from the lower position, either way we now want to
            if (gamepad.left_stick_y > STARTING_JOYSTICK_THRESHOLD) {
                slidesUp = true;
                recievingPixel = false;
                setDropNormalPosition();
            }
        } else {
            if (gamepad.left_stick_y != 0) {
                //int target =  SlidesMotor.getTarget()+ (int)(gamepad.left_stick_y * OVERRIDE_SPEED);
                //int clipped_target = Math.max(Math.min(target,DROP_PIXEL_MAX_POSITION),DROP_PIXEL_MIN_POSITION);
                //SlidesMotor.setTarget(clipped_target);
                SlidesMotor.setPower(gamepad.left_stick_y);
            }
            SlidesMotor.setPower(0.0);
        }
        //SlidesMotor.Update();
    }

    public void update(Telemetry telemetry){
        update();
        telemetry.addData("LinearSlides State", state.toString());
    }
}
