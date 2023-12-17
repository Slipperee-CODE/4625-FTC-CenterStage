package org.firstinspires.ftc.teamcode.opmodes.preMeet3.comp;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.Clock;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.PIDController;
import org.firstinspires.ftc.teamcode.opmodes.preMeet3.WaitingAutoLinear;

@Autonomous(name = "Custom Odometry Test")
public class CustomOdometryTest extends WaitingAutoLinear {
    private static final double MASTER_FORWARD_SPEED = 0.5;
    private static final double TICKS_PER_REVOLUTION = 8134;
    private static final double WHEEL_RADIUS = 0.688975;// in inches
    private static final double INCHES_PER_TICK = 2.0 * Math.PI  * WHEEL_RADIUS / TICKS_PER_REVOLUTION;
    private final Clock detectionTime = new Clock();
    private int initialLeft,initialRight;

    @Override
    public void initialize() {}

    @Override
    public void run() {
        forward(24,10);
        sleep(1_000);
    }

    private void forward(double inches, double seconds) {
        final double POSITION_THRESHOLD = 0.1; // to make it always wait for <seconds> make this value negative
        final PIDController strafeFixer = new PIDController(0.00005,0.000001,0);
        int initial_encoder = robot.rightBack.getCurrentPosition();
        resetRelatives();
        double forwardError;
        double strafeError;
        double turnError;
        detectionTime.reset();
        do {
            strafeError = (robot.rightBack.getCurrentPosition() - initial_encoder) * INCHES_PER_TICK;
            int avg = (Math.abs(getRelativeLeft()) + Math.abs(getRelativeRight()) )/2;
            double forwardRelative = avg * INCHES_PER_TICK ;
            telemetry.addData("forwardRel",forwardRelative);
            telemetry.addData("Time Left",seconds - detectionTime.getTimeSeconds());
            forwardError =  forwardRelative - inches;
            turnError = (getRelativeLeft() - getRelativeLeft()) * INCHES_PER_TICK;
            robot.emulateController(Math.tanh(forwardError)*MASTER_FORWARD_SPEED, strafeFixer.Update(strafeError), turnError/ 10);
            telemetry.update();
        } while (detectionTime.getTimeSeconds() < seconds && Math.sqrt(forwardError*forwardError + strafeError *strafeError + turnError*turnError) > POSITION_THRESHOLD);
        robot.stop();
        telemetry.addData("Finished in: ",detectionTime.getTimeSeconds());
        telemetry.update();
    }
    private void strafe(double inchesRight, double seconds) {
        final double POSITION_THRESHOLD = 0.1; // to make it always wait for <seconds> make this value negative
        final PIDController forwardFixer = new PIDController(0.5,0.0001,0);
        int initial_encoder = robot.rightBack.getCurrentPosition();
        resetRelatives();
        double forwardError;
        double strafeError;
        double turnError;
        detectionTime.reset();
        do {
            double strafeRelative =  (robot.rightBack.getCurrentPosition() - initial_encoder) * INCHES_PER_TICK;
            strafeError = inchesRight - strafeRelative;
            int avg = ((getRelativeLeft())-(getRelativeRight()) )/2;
            double forwardRelative = avg * INCHES_PER_TICK ;
            telemetry.addData("strafeRel stra",strafeRelative);
            telemetry.addData("Time Left",seconds - detectionTime.getTimeSeconds());
            forwardError =  forwardRelative;
            turnError = (getRelativeLeft() - getRelativeLeft()) * INCHES_PER_TICK;
            robot.emulateController(Math.tanh(forwardError)*MASTER_FORWARD_SPEED, forwardFixer.Update(strafeError), turnError/ 10);
            telemetry.update();
        } while (detectionTime.getTimeSeconds() < seconds && Math.sqrt(forwardError*forwardError + strafeError *strafeError + turnError*turnError) > POSITION_THRESHOLD);
        robot.stop();
        telemetry.addData("Finished in: ",detectionTime.getTimeSeconds());
        telemetry.update();
    }
    private void turnLeft90() {
        robot.emulateController(0, 0, -.5);
        sleep(1_100);
        robot.stop();
    }
    public int getRelativeLeft() {
        return robot.leftFront.getCurrentPosition() - initialLeft;
    }
    public int getRelativeRight() {
        return robot.rightFront.getCurrentPosition() - initialRight;
    }
    public void powerLeft(double power) {
        robot.leftFront.setPower(power);
        robot.rightBack.setPower(power);
    }
    public void powerRight(double power) {
        robot.rightBack.setPower(power);
        robot.rightFront.setPower(power);
    }
    public void resetRelatives() {
        initialRight = robot.rightFront.getCurrentPosition();
        initialLeft = robot.leftFront.getCurrentPosition();
    }
}