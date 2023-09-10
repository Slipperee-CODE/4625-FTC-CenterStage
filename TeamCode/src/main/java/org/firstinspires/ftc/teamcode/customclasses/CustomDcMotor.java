package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.hardware.DcMotor;

public class CustomDcMotor
{
    public DcMotor customMotor = null;
    public double speedConstant;


    public CustomDcMotor(DcMotor motor, double speedConstant){ initialize(motor, speedConstant); }


    public CustomDcMotor(DcMotor motor){ initialize(motor, 1); }


    private void initialize(DcMotor passedMotor, double passedSpeedConstant)
    {
        customMotor = passedMotor;
        speedConstant = passedSpeedConstant;

        //customMotor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        //customMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        customMotor.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        customMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        customMotor.setPower(0);
    }


    public void SetPower(double power)
    {
        customMotor.setPower(power * speedConstant);
    }


    public void SetRawPower(double power)
    {
        customMotor.setPower(power);
    }
}
