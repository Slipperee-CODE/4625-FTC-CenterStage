package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;
import java.lang.Runnable;
import java.util.ArrayList;
import java.util.List;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.Clock;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.PIDMotor;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismBase;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState;


public class Outtake extends MechanismBase {
    private class LeftAngler extends BinaryAngler {
        protected double getRECEIVE_POSITION() {return 0.0;} // to be tuned
        protected double getDROP_POSITION() {return 0.0;} // to be tuned
    }
    private class RightAngler extends BinaryAngler {
        protected double getRECEIVE_POSITION() {return 0.0;} // to be tuned
        protected double getDROP_POSITION() {return 0.0;} // to be tuned
    }
    private static final float SPEED = 50.0f;
    public static final int DROP_PIXEL_MIN_POSITION = -200; // position for linear slides
    public static int DROP_PIXEL_MAX_POSITION = 2_500; // position for linear slides


   // public static final double OUTTAKE_RECEIVE_ANGLER_POSITION = 0.9; // position for dropAngler
   // public static final double OUTTAKE_DROP_ANGLER_POSITION_LOWER = 0.7; // position for dropAngler
   // public static final double OUTTAKE_DROP_ANGLER_POSITION_NORMAL = 0.15; // position for dropAngler
    private static class DropperPosition {
        public static final double HALFWAY = 0.1; // not used right now
        public static final double OPEN = 0.2;
        public static final double CLOSED = 0;
    }
    private static class CapperPosition {
        public static final double CAPPING =  0.5;
        public static final double NO_CAP = 0.0;
    }
    private final TouchSensor touchSensor;
    public static final float STARTING_JOYSTICK_THRESHOLD = 0.2f;
    public static int  STARTING_SLIDES_MOTOR_TICK;
    private boolean slidesUp;
    private boolean receivingPixel;
    private boolean shouldGoDown = false;
    private final PIDMotor slidesMotorRight;
    private final PIDMotor slidesMotorLeft;
    private final LeftAngler leftAngler = new LeftAngler();
    private final RightAngler rightAngler = new RightAngler();
    private final Servo Dropper;
    private final Servo Capper;

    //private final DistanceSensor distanceSensor;
    private final Clock timer = new Clock();

    private final CustomGamepad gamepad;




    private static final double TOLERANCE = .01;

    public static final double p = 0.0045;
    public static final double i = 0.00001;
    public static final double d = 0.00;
    public final List<Pair<Double,Runnable>> procrastinationList = new ArrayList<>();
    private final Clock procrastinationTimer = new Clock();

    public Outtake(HardwareMap hardwareMap, CustomGamepad gamepad){
        slidesMotorRight = new PIDMotor(getHardware(DcMotor.class,"idunno",hardwareMap),p,i,d);
        slidesMotorLeft = new PIDMotor(getHardware(DcMotor.class,"rightLinearSlides",hardwareMap),p,i,d);
        slidesMotorLeft.motor.setDirection(DcMotorSimple.Direction.REVERSE);
        leftAngler.servo = getHardware(Servo.class,"OuttakeLeftAngler",hardwareMap);
        rightAngler.servo = getHardware(Servo.class,"OuttakeRightAngler",hardwareMap);
        slidesMotorRight.motor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        Dropper = getHardware(Servo.class, "OuttakeDropper",hardwareMap);
        Capper = getHardware(Servo.class, "OuttakeAntiAircraft",hardwareMap);
        touchSensor = getHardware(TouchSensor.class,"OuttakeTouchSensor",hardwareMap);
        this.gamepad = gamepad;
        slidesUp = false;
        receivingPixel = true;
        STARTING_SLIDES_MOTOR_TICK = slidesMotorRight.motor.getCurrentPosition();
        slidesMotorLeft.ResetPID();
        slidesMotorRight.ResetPID();
        setReceivePosition();

        /// SCAREY FUNCTION DONT UNLEASH UNLESS YOU TALKED TO LEO FIRST
        //StartLinearSlides();
    }
    private void StartLinearSlides() {
        // this method should only be called from the constructor
        while (!getFullyDown()) {
            slidesMotorLeft.motor.setPower(-.1); // i dont actually know if this goes the right way
            slidesMotorRight.motor.setPower(0.1);
            CustomOpMode.sleep(1); // give it time to breath so it can exit as fast as possible
        }
        // if we are here then we are fully down
        slidesMotorRight.ResetPID(); // we reset pid and stuff to make this point the new zero
        slidesMotorLeft.ResetPID();
    }
    public void setState(MechanismState newState) {
        this.state = newState;
    }
    public void setReceivePosition() {
        //SlidesMotor.setTarget(readyToReceivePixelsTarget);
        leftAngler.setReceive();
        rightAngler.setReceive();
        Dropper.setPosition(DropperPosition.OPEN);
        Capper.setPosition(CapperPosition.CAPPING);
    }
    public void setDropPosition() {
        leftAngler.setDrop();
        rightAngler.setDrop();
        Dropper.setPosition(DropperPosition.CLOSED); // here we can directly open the outtake because we are sure that we are in the correct spot to drop it
        Capper.setPosition(CapperPosition.NO_CAP);
    }

    public void drop() {
        Dropper.setPosition(DropperPosition.OPEN);
    }

    public boolean getFullyDown() {return touchSensor.isPressed();}

    public void resetOuttake() {
        slidesMotorLeft.setTarget(0);
        slidesMotorRight.setTarget(0);
        receivingPixel = true;
        procrastinate(0.5, this::setReceivePosition);// we do this to make sure that our slides try to go down first because we dont want to interfere with that, if it turns out that its not needed we just dont procrastinate on that.
    }

    public void update()
    {
        if (gamepad.xDown) {
            if ((!receivingPixel)) { // set them down
                if (dropperIsOpen()) {
                    // if the dropper is open then the next step is to put the outtake down;
                    resetOuttake();
                } else if (dropperIsClosed()) {
                    drop();
                }
            } else { // if we are recieving the pixel then as a sanity check we make sure the slides are down
                // here since the use hasn't done anything to point that they want to go up we assume that they just want to deposit them down
                receivingPixel = false;
                setDropPosition();
            }
        }
        if (gamepad.bDown) {
            // we should definitely try to go back to recieve position
            resetOuttake();
        }
        float right_stick_y = -gamepad.right_stick_y;

        if (!slidesUp) { // means we are at are either recieving or dropping from the lower position, either way we now want to
            if (right_stick_y > STARTING_JOYSTICK_THRESHOLD) {
                slidesUp = true;
                receivingPixel = false;
                setDropPosition();
            }
        } else {
            if (right_stick_y != 0) {
            //slidesMotorRight.motor.setPower(gamepad.right_stick_y);
            //slidesMotorLeft.motor.setPower(-gamepad.right_stick_y);
                int targetLeft = slidesMotorLeft.getTarget() - (int) (gamepad.right_stick_y * SPEED);
                int targetRight = slidesMotorRight.getTarget() + (int) (gamepad.right_stick_y * SPEED);
                int clippedRight = Range.clip(targetRight,-DROP_PIXEL_MAX_POSITION,-DROP_PIXEL_MIN_POSITION);
                int clippedLeft = Range.clip(targetLeft,DROP_PIXEL_MIN_POSITION,DROP_PIXEL_MAX_POSITION);

                slidesMotorLeft.setTarget(clippedLeft);
                slidesMotorRight.setTarget(clippedRight);
            }

        }
        // stuff below is for proper procrastination, a very important part of our robot.
        double currentTime = procrastinationTimer.getTimeSeconds();
        for (int i = procrastinationList.size() - 1; i >=0;i--) { //technically this sucks performance-wise but its not like were gonna have more than 3 things ever in the list so why not
            if (procrastinationList.get(i).x <= currentTime) {
                procrastinationList.get(i).y.run();
                procrastinationList.remove(i);
            }
        }
        slidesMotorLeft.Update();
        slidesMotorRight.Update();
    }

    public void update(Telemetry telemetry){
        update();
        //telemetry.addData("LinearSlides State", state.toString());
        telemetry.addData("slidesLeftTarget",slidesMotorLeft.getTarget());
        telemetry.addData("slidesRightTarget",slidesMotorRight.getTarget());
        //telemetry.addData("Dropper Boolean", (Dropper.getPosition() == OUTTAKE_OPEN_POSITION));
    }

    public boolean somewhatEquals(double one, double two){
        double difference = Math.abs(one - two);
        return difference < TOLERANCE;
    }
    private boolean dropperIsClosed() {
        return somewhatEquals(Dropper.getPosition(),DropperPosition.CLOSED);
    }
    private boolean dropperIsOpen() {
        return somewhatEquals(Dropper.getPosition(),DropperPosition.OPEN);
    }
    private void procrastinate(double inHowManySeconds, Runnable callback) {
        procrastinationList.add(new Pair<>(inHowManySeconds + procrastinationTimer.getTimeSeconds(),callback));
    }
}
