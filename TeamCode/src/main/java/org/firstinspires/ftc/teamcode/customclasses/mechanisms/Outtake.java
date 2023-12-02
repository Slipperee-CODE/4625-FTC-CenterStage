package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;

public class Outtake extends MechanismBase {
    private static final float OVERRIDE_SPEED = 50.0f;

    public static final int DROP_PIXEL_MIN_POSITION = 1_000; // position for linear slides
    public static final int DROP_PIXEL_MAX_POSITION = 3_000_000; // position for linear slides
    public final int readyToReceivePixelsTarget = 0; // position for linear slides
    public final int readyToDropPixelsTarget = 1000; // position for linear slides
    public static final double OUTTAKE_RECEIVE_ANGLER_POSITION = 0.851; // position for dropAngler
    public static final double OUTTAKE_DROP_ANGLER_POSITION_LOWER = 0.7; // position for dropAngler
    public static final double OUTTAKE_DROP_ANGLER_POSITION_NORMAL = 0.15; // position for dropAngler
    public static final double OUTTAKE_ANGLER_RECHAMBER_POSITION = 0.25; // position for dropAnger
    public static final double LID_RECEIVE_POSITION = .0895; // position for LidAngler
    public static final double LID_DROP_POSITION  = 0.00; // position for LidAngler
    public static final double LID_DROP_SLIGHTLY_OPEN = 0.05;// guesstimation for LidAngler
    public static final double OUTTAKE_CLOSED_POSITION = .6; // position for Dropper
    public static final double OUTTAKE_OPEN_POSITION = .2; // position for Dropper
    public static final double OUTTAKE_RECEIVE_POSITION = .35; // position for Dropper
    public static final float STARTING_JOYSTICK_THRESHOLD = 0.2f;
    private boolean slidesUp;
    private boolean receivingPixel;
    private boolean chambering = false;
    private final PIDMotor SlidesMotor;
    private final Servo DropAngler;
    private final Servo LidAngler;
    private final Servo Dropper;
    //private final DistanceSensor distanceSensor;
    private final Clock timer = new Clock();
    public static final double CHAMBERING_TIME = 0.15;
    public double chamber_start_time = -1.0;




    private static final double tolerance = .01;

    public Outtake(HardwareMap hardwareMap, CustomGamepad gamepad){
        SlidesMotor = new PIDMotor(getHardware(DcMotor.class,"idunno",hardwareMap),0.001,0.00001,0.0);
        LidAngler = getHardware(Servo.class,"OuttakeLidAngler",hardwareMap);
        //distanceSensor = getHardware(DistanceSensor.class,"distanceSensor",hardwareMap);
        SlidesMotor.motor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        DropAngler = getHardware(Servo.class, "OuttakeAngler",hardwareMap);
        Dropper = getHardware(Servo.class, "OuttakeDropper",hardwareMap);

        this.gamepad = gamepad;
        slidesUp = false;
        receivingPixel = true;
        setReceivePosition();
    }
    public void setState(MechanismState newState) {
        this.state = newState;

    }
    private void setReceivePosition() {
        //SlidesMotor.setTarget(readyToReceivePixelsTarget);
        DropAngler.setPosition(OUTTAKE_RECEIVE_ANGLER_POSITION);
        LidAngler.setPosition(LID_RECEIVE_POSITION);
        Dropper.setPosition(OUTTAKE_RECEIVE_POSITION);
    }
    private void setDropLowerPosition() {
        //SlidesMotor.setTarget(readyToDropPixelsTarget);
        DropAngler.setPosition(OUTTAKE_DROP_ANGLER_POSITION_LOWER);
        LidAngler.setPosition(LID_DROP_SLIGHTLY_OPEN);
        Dropper.setPosition(OUTTAKE_CLOSED_POSITION); // here we can directly open the outtake because we are sure that we are in the correct spot to drop it
    }
    private void setDropNormalPosition() {
        //SlidesMotor.setTarget(Math.max(SlidesMotor.getTarget(),DROP_PIXEL_MIN_POSITION));
        LidAngler.setPosition(LID_DROP_POSITION);

        DropAngler.setPosition(OUTTAKE_DROP_ANGLER_POSITION_NORMAL);
        // here we CANNOT open the dropper as we still have to align ourselves to where we want to drop it at
        Dropper.setPosition(OUTTAKE_CLOSED_POSITION); // we close it just to make sure that out pixel doesn't fall before we are at the right spot
    }
    private void startChambering() {
        Dropper.setPosition(OUTTAKE_CLOSED_POSITION);
        LidAngler.setPosition(LID_DROP_SLIGHTLY_OPEN);
        DropAngler.setPosition(OUTTAKE_ANGLER_RECHAMBER_POSITION);
    }
    private void stopChambering() {
        Dropper.setPosition(OUTTAKE_CLOSED_POSITION);
        LidAngler.setPosition(LID_DROP_POSITION);
        DropAngler.setPosition(OUTTAKE_DROP_ANGLER_POSITION_NORMAL);
    }




    public void update()
    {
        if (gamepad.yDown) {
            if (!chambering) {
                if (slidesUp && !receivingPixel) {
                    chambering = true;
                    chamber_start_time = timer.getTimeSeconds();
                    startChambering();
                }
            }
        }
        if (chambering) {
            if (timer.getTimeSeconds() - chamber_start_time > CHAMBERING_TIME) {
                stopChambering();
                chambering = false;
            }
        }
        if (gamepad.xDown) {
            if ((slidesUp || !receivingPixel)) { // set them down
                if (somewhatEquals(Dropper.getPosition(),OUTTAKE_OPEN_POSITION)) {
                    slidesUp = false;
                    receivingPixel = true;
                    setReceivePosition();
                } else if (somewhatEquals(Dropper.getPosition(),OUTTAKE_CLOSED_POSITION)) {
                    Dropper.setPosition(OUTTAKE_OPEN_POSITION);
                }
            } else { // if we are recieving the pixel then as a sanity check we make sure the slides are down
                // here since the use hasn't done anything to point that they want to go up we assume that they just want to deposit them down
                receivingPixel = false;
                setDropLowerPosition();
            }
        }
        float right_stick_y = -gamepad.right_stick_y;

        if (!slidesUp) { // means we are at are either recieving or dropping from the lower position, either way we now want to
            if (right_stick_y > STARTING_JOYSTICK_THRESHOLD) {
                slidesUp = true;
                receivingPixel = false;
                setDropNormalPosition();
            }
        } else {
            //if (right_stick_y != 0) {
            SlidesMotor.motor.setPower(gamepad.right_stick_y);
                //int target = SlidesMotor.getTarget() + (int) (gamepad.right_stick_y * OVERRIDE_SPEED);
                //int clipped_target = Math.max(Math.min(target, DROP_PIXEL_MAX_POSITION), DROP_PIXEL_MIN_POSITION);
                //SlidesMotor.setTarget(clipped_target);
            //}

        }
        //SlidesMotor.Update();
    }

    public void update(Telemetry telemetry){
        update();
        //telemetry.addData("LinearSlides State", state.toString());
        telemetry.addData("Dropper Angle", Dropper.getPosition());
        telemetry.addData("SlidesUp: ", slidesUp);
        telemetry.addData("Recieving: ", receivingPixel);
        //telemetry.addData("Dropper Boolean", (Dropper.getPosition() == OUTTAKE_OPEN_POSITION));
    }

    public boolean somewhatEquals(double one, double two){
        double difference = Math.abs(one - two);
        return difference < tolerance;
    }
}
