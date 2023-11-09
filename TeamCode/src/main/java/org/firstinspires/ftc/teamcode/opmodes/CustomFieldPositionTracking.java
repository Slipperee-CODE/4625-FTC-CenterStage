package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.customclasses.Robot;

public class CustomFieldPositionTracking extends OpMode {
    private static final double TICKS_PER_REVOLUTION = 537.7;
    private static final double WHEEL_RADIUS = 0.04800101600203201; // in meters
    private static final double METERS_PER_TICK = 2 * Math.PI  * WHEEL_RADIUS / TICKS_PER_REVOLUTION;
    private static final double TRACK_WIDTH = 17.0; // this is the distance between left and right dead wheels
    private static final double TRACK_LENGTH = 11.0; // distance from the middle of the track width to back wheel
    private DcMotor lf, rf, lb, rb;
    private double x_position,y_position, heading;


    private int left_encoder_offset,right_encoder_offset,back_encoder_offset;


    @Override
    public void init() {
        lf = hardwareMap.get(DcMotor.class,"leftFront"); // this has the left encoder
        rf = hardwareMap.get(DcMotor.class,"rightFront"); // this has the right encoder
        lb = hardwareMap.get(DcMotor.class,"leftBack"); // this has the back encoder
        rb = hardwareMap.get(DcMotor.class,"rightBack");
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
    }
    @Override
    public void init_loop() {
        telemetry.addLine("Make sure that the robot is at (0,0)");
        updateController();
        telemetry.update();
    }
    @Override
    public void start() {
        resetEncoders();
    }
    @Override
    public void loop() {
        updateController();
        // Robot Centric Tracking
        final int leftPos = getLeftEncoderPosition();
        final int rightPos = getRightEncoderPosition();
        final int backPos = getBackEncoderPosition();
        final double x_rel =   METERS_PER_TICK * (leftPos + rightPos) / 2.0;
        final double _theta =   (rightPos - leftPos) / TRACK_WIDTH;
        final double y_rel = METERS_PER_TICK * (backPos - TRACK_LENGTH * _theta);
        final double theta_rel = _theta * METERS_PER_TICK;

        // Field Centric Tracking
        x_position = x_position + x_rel * Math.cos(theta_rel) - y_rel * Math.sin(theta_rel);
        y_position = y_position + x_rel * Math.sin(theta_rel) + y_rel * Math.cos(theta_rel);
        heading += theta_rel;



    }
    private int getLeftEncoderPosition() {
        return lf.getCurrentPosition() - left_encoder_offset;
    }
    private int getRightEncoderPosition() {
        return rf.getCurrentPosition() - right_encoder_offset;
    }
    private int getBackEncoderPosition() {
        return lb.getCurrentPosition() - back_encoder_offset;
    }
    private void resetEncoders() {
        left_encoder_offset = lf.getCurrentPosition();
        right_encoder_offset = rf.getCurrentPosition();
        back_encoder_offset = lb.getCurrentPosition();
    }

    private void updateController() {
        emulateController(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);
    }

    private void emulateController(double left_y,double left_x,double right_x) {
        final double y = -left_y; // Forward-Backward
        final double x = left_x; // Strafe
        final double r = right_x; // Rotate

        final double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(r), 1);
        final double frontLeftPower = (y + x + r) / denominator;
        final double backLeftPower = (y - x + r) / denominator;
        final double frontRightPower = (y - x - r) / denominator;
        final double backRightPower = (y + x - r) / denominator;

        rf.setPower(frontRightPower);
        rb.setPower(backRightPower);
        lf.setPower(frontLeftPower);
        lb.setPower(backLeftPower);
    }
}
