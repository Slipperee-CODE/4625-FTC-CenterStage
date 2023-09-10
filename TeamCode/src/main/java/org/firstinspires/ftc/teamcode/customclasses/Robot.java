package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import java.util.Arrays;
import java.util.List;

public class Robot {

    //initialize all the parts of the robot here
    private DcMotor rightFront = null;
    private DcMotor rightBack = null;
    private DcMotor leftFront = null;
    private DcMotor leftBack = null;

    public CustomDcMotor rF = null;
    public CustomDcMotor rB = null;
    public CustomDcMotor lF = null;
    public CustomDcMotor lB = null;

    public CustomDcMotor[] customMotors;
    private List<DcMotor> motors;

    public Webcam webcam = null;

    HardwareMap hardwareMap = null;
    public ElapsedTime runtime = new ElapsedTime();

    public Robot(HardwareMap hwMap)
    {
        initialize(hwMap);
    }

    private void initialize(HardwareMap hwMap)
    {
        hardwareMap = hwMap;

        //webcam = new Webcam(hardwareMap);

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

        rF = new CustomDcMotor(rightFront);
        rB = new CustomDcMotor(rightBack);
        lF = new CustomDcMotor(leftFront);
        lB = new CustomDcMotor(leftBack);

        customMotors = new CustomDcMotor[]{rF, rB, lF, lB};
    }

    public void setAllMotorsPower(double power) {
        for (CustomDcMotor motor : customMotors) {
            motor.SetPower(power);
        }
    }


    public void SetSpeedConstant(double passedSpeedConstant)
    {
        for (CustomDcMotor customDcMotor : customMotors){ customDcMotor.speedConstant = passedSpeedConstant; }
    }
}
