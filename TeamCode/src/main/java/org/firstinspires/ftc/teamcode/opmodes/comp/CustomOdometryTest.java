package org.firstinspires.ftc.teamcode.opmodes.comp;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.PIDController;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAutoLinear;

@Autonomous(name = "staytest")
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


    }

    private void forward(double inches, double seconds) {
        final double POSITION_THRESHOLD = 0.1;
        final PIDController strafeFixer = new PIDController(0.00005,0.000001,0);
        int initial_encoder = robot.rightBack.getCurrentPosition();
        resetRelatives();
        double forwardError,strafeError, turnError;
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

    }

    private void turnLeft90() {
        robot.emulateController(0, 0, -.5);
        sleep(1_200);
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