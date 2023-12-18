package org.firstinspires.ftc.teamcode.customclasses.preILT;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class RobotDrivetrain {
    //preILT -> RobotDrivetrain
    // - This is a renamed version of the Robot class
    // - Removed methods with 0 usages
    // - Removed variables with 0 usages
    // - Speed Constant (speedConstant) can be set through using setSpeedConstant(float speedConstant) in any OpMode we need to do so in
    public DcMotor rightFront = null;
    public DcMotor rightBack = null;
    public DcMotor leftFront = null;
    public DcMotor leftBack = null;
    private float speedConstant;

    public RobotDrivetrain(HardwareMap hwMap){
        rightFront = hwMap.get(DcMotor.class, "rightFront");
        rightBack = hwMap.get(DcMotor.class, "rightBack");
        leftFront = hwMap.get(DcMotor.class, "leftFront");
        leftBack = hwMap.get(DcMotor.class, "leftBack");

        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void emulateController(double left_y,double left_x,double right_x) {
        double y = -left_y; // Forward-Backward
        double x = left_x; // Strafe
        double rx = -right_x; // Rotate

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x - rx) / denominator;
        double backLeftPower = (y - x - rx) / denominator;
        double frontRightPower = (y - x + rx) / denominator;
        double backRightPower = (y + x + rx) / denominator;

        rightFront.setPower(frontRightPower * speedConstant);
        rightBack.setPower(backRightPower * speedConstant);
        leftFront.setPower(frontLeftPower * speedConstant);
        leftBack.setPower(backLeftPower * speedConstant);
    }

    public void setAllMotorPowers(double power) {
        rightFront.setPower(power);
        rightBack.setPower(power);
        leftFront.setPower(power);
        leftBack.setPower(power);
    }

    public void setSpeedConstant(float speedConstant){
        this.speedConstant = speedConstant;
    }
}
