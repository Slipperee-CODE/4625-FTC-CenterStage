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
    private double speedConstant;

    private int direction = 1;

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
    public void switchDirection(){
        setDirection(-direction);
    }
    public void setDirection(int direction) {
        assert (direction == 0 || direction == 1 || direction == -1);
        this.direction = direction;
    }

    /**
     * Will control robot as if they were from the robot controller
     * It will invert the left_y as going up on a controller will be negative
     * and down is positive.
     * @param left_y
     * @param left_x
     * @param right_x
     */
    public void emulateController(double left_y,double left_x,double right_x) {
        double y = left_y * -direction; // Forward-Backward and invert the y direction
        double x = left_x * direction; // Strafe
        double rx = right_x; // Rotate
        baseMoveRobot(x,y,rx);

    }

    public void baseMoveRobot(double x, double y, double rx) {
        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y - x + rx) / denominator;
        double backLeftPower = (y + x + rx) / denominator;
        double frontRightPower = (y + x - rx) / denominator;
        double backRightPower = (y - x - rx) / denominator;

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

    public void setSpeedConstant(double speedConstant){
        this.speedConstant = speedConstant;
    }
}
