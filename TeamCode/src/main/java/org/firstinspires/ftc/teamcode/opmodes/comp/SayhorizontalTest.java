package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.ActiveIntake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAutoLinear;

@Autonomous(name = "staytest")
public class SayhorizontalTest extends WaitingAutoLinear {
    private static final double TICKS_PER_REVOLUTION = 8134; // was originally 537.7 but a rough estimate by hand was closer to 8,100
    private static final double TURN_DAMPENER = TICKS_PER_REVOLUTION * 5;
    private static final double WHEEL_RADIUS = 0.02;// in meters
    private static final double METERS_PER_TICK = 2.0 * Math.PI  * WHEEL_RADIUS / TICKS_PER_REVOLUTION;

    //private AprilTagAlign tagAlign;
    private final Clock detectionTime = new Clock();

    private int initialLeft,initialRight;

    @Override
    public void initialize() {

    }

    @Override
    public void run() {
        robot.emulateController(.5, 0, 0);
        resetRelatives();
        int initial_encoder = robot.rightBack.getCurrentPosition();
        final double meters = 2.0;
        while (detectionTime.getTimeSeconds() < 999) {
            int error = robot.rightBack.getCurrentPosition() - initial_encoder;
            int avg = (Math.abs(getRelativeLeft()) + Math.abs(getRelativeRight()) )/2;
            double forwardRelative = avg * METERS_PER_TICK ;
            telemetry.addData("forwardRel",forwardRelative);
            double forwardError =  forwardRelative - meters;
            double turnError = getRelativeLeft() - getRelativeLeft();
            robot.emulateController(Math.tanh(forwardError), error * 0.00005, turnError/TURN_DAMPENER);
            telemetry.update();
        }

        //sleep(1700 * 2 / 3);


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