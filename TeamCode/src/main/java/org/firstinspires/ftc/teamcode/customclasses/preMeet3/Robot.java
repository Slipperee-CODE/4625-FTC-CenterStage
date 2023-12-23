package org.firstinspires.ftc.teamcode.customclasses.preMeet3;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomDcMotor;

import java.util.List;

public class Robot {

    //initialize all the parts of the robot here


    public DcMotor rightFront = null;
    public DcMotor rightBack = null;
    public DcMotor leftFront = null;
    public DcMotor leftBack = null;

    //public CustomDcMotor rF = null;
    //public CustomDcMotor rB = null;
    //public CustomDcMotor lF = null;
    //public CustomDcMotor lB = null;

    public CustomDcMotor[] customMotors;
    private List<DcMotor> motors;


    HardwareMap hardwareMap = null;
    public ElapsedTime runtime = new ElapsedTime();

    public Robot(HardwareMap hwMap)
    {
        initialize(hwMap);
    }

    private void initialize(HardwareMap hwMap)
    {
        hardwareMap = hwMap;


        //Assign all the Parts of the Robot Here
        rightFront = hardwareMap.get(DcMotor.class, "rightFront");
        rightBack = hardwareMap.get(DcMotor.class, "rightBack");
        leftFront = hardwareMap.get(DcMotor.class, "leftFront");
        leftBack = hardwareMap.get(DcMotor.class, "leftBack");

        //motors = Arrays.asList(rightFront, rightBack, leftFront, leftBack);

        //Set Motor Directions
        rightFront.setDirection(DcMotorSimple.Direction.FORWARD);
        rightBack.setDirection(DcMotorSimple.Direction.FORWARD);
        leftFront.setDirection(DcMotorSimple.Direction.REVERSE);
        leftBack.setDirection(DcMotorSimple.Direction.REVERSE);
        //rF = new CustomDcMotor(rightFront);
        //rB = new CustomDcMotor(rightBack);
        //lF = new CustomDcMotor(leftFront);
        //lB = new CustomDcMotor(leftBack);
        customMotors  = new CustomDcMotor[ ] {};
        //customMotors = new CustomDcMotor[]{rF, rB, lF, lB};
    }

    public void setAllMotorsPower(double power) {
        for (CustomDcMotor motor : customMotors) {
            motor.SetPower(power);
        }
    }
    public void stop() {
        /*for (CustomDcMotor motor : customMotors) {
            motor.SetPower(0);
        }
        */
         leftFront.setPower(0);
         leftBack.setPower(0);
         rightFront.setPower(0);
         rightBack.setPower(0);
    }
    public void driveAngleKeepHeading(double power, double angle) {
        //This has no real guarantee of keeping the heading, we only move the robot without rotating it. so in a perfect world the heading wouldn't change.

        emulateController(power * Math.sin(angle), power * Math.cos(angle), 0);
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

        rightFront.setPower(frontRightPower * .85);
        rightBack.setPower(backRightPower * .85);
        leftFront.setPower(frontLeftPower * .85);
        leftBack.setPower(backLeftPower * .85);
    }


    public void SetSpeedConstant(double passedSpeedConstant)
    {
        for (CustomDcMotor customDcMotor : customMotors){ customDcMotor.speedConstant = passedSpeedConstant; }
    }
}
