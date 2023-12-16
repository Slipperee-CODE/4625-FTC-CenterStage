package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.customclasses.Robot;
@Disabled
@TeleOp(name = "Field Tracking")
public class CustomFieldPositionTracking extends OpMode {
    private static final double TICKS_PER_REVOLUTION = 8134; // was originally 537.7 but a rough estimate by hand was closer to 8,100
    private static final double WHEEL_RADIUS = 0.02;// in meters
    private static final double METERS_PER_TICK = 2.0 * Math.PI  * WHEEL_RADIUS / TICKS_PER_REVOLUTION;
    private static final double TRACK_WIDTH = .36; // this is the distance between left and right dead wheels
    private static final double TRACK_LENGTH = .16; // distance from the middle of the track width to back wheel
    private DcMotor lf, rf, lb, rb;
    private DcMotor leftEncoder, rightEncoder,backEncoder;
    private double x_position,y_position, heading;

    private static final int updateEvery = 10;
    private int frame= 0;

    private int left_encoder_offset,right_encoder_offset,back_encoder_offset;
    private int left_encoder_og,right_encoder_og,back_encoder_og;


    @Override
    public void init() {
        lf = hardwareMap.get(DcMotor.class,"leftFront");
        rf = hardwareMap.get(DcMotor.class,"rightFront");
        lb = hardwareMap.get(DcMotor.class,"leftBack");
        rb = hardwareMap.get(DcMotor.class,"rightBack");
        lf.setDirection(DcMotorSimple.Direction.REVERSE);
        lb.setDirection(DcMotorSimple.Direction.REVERSE);
        lf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rf.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        lb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        rb.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        leftEncoder = lf;
        rightEncoder = rf;
        backEncoder = rb;
    }
    @Override
    public void init_loop() {
        telemetry.addLine("Make sure that the robot is at (0,0)");
        updateController();
        telemetry.update();
    }
    @Override
    public void start() {
        left_encoder_og = getLeftEncoderPosition();
        right_encoder_og = getRightEncoderPosition();
        back_encoder_og = getBackEncoderPosition();
        resetEncoders();


    }
    @Override
    public void loop() {
        frame++;
        updateController();
        // Robot Centric Tracking
        final int leftPos = -getLeftEncoderPosition();
        final int rightPos = getRightEncoderPosition();
        final int backPos = getBackEncoderPosition();

        final double x_rel =   METERS_PER_TICK * (leftPos + rightPos) / 2.0;
        final double theta =    METERS_PER_TICK * (rightPos - leftPos) / TRACK_WIDTH;
        final double y_rel = METERS_PER_TICK * (backPos -  (rightPos - leftPos) * TRACK_LENGTH / TRACK_WIDTH);
        resetEncoders();
        // Field Centric Tracking
        x_position += y_rel;
        y_position += x_rel;
        heading += theta;

        if (frame % updateEvery == 0) {
            updateTelemetry();
        }

    }

    private void updateTelemetry() {
        telemetry.addData("Left Encoder: ",leftEncoder.getCurrentPosition() - left_encoder_og);
        telemetry.addData("Right Encoder: ", rightEncoder.getCurrentPosition() - right_encoder_og);
        telemetry.addData("Back Encoder: ",backEncoder.getCurrentPosition() - back_encoder_og);
        telemetry.addData("X Position: ",x_position);
        telemetry.addData("Y Position: ",y_position);
        telemetry.addData("Heading: ",heading);
        telemetry.update();
    }
    private int getLeftEncoderPosition() {
        return leftEncoder.getCurrentPosition() - left_encoder_offset;
    }
    private int getRightEncoderPosition() {
        return rightEncoder.getCurrentPosition() - right_encoder_offset;
    }
    private int getBackEncoderPosition() {
        return backEncoder.getCurrentPosition() - back_encoder_offset;
    }
    private void resetEncoders() {
        left_encoder_offset = leftEncoder.getCurrentPosition();
        right_encoder_offset = rightEncoder.getCurrentPosition();
        back_encoder_offset = backEncoder.getCurrentPosition();
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
