package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.zip.ZipInputStream;

public class PIDMotor {
    private final Clock clock = new Clock();
    private final DcMotor motor;
    private final double p, i, d;
    private double errorSum;
    private double lastError = Double.NaN;
    private int target;
    private double sampleTime = 0.005;

    public void setSampleTime(double sampleTime) {
        this.sampleTime = sampleTime;
    }
    private double clamp(double x, double min, double max) {
        return Math.min(Math.max(x,min),max);
    }
    private double round(double x) {
        return Math.round(x*1000)/1000.0;
    }

    public PIDMotor(DcMotor motor,double p, double i, double d)
    {
        this.motor = motor;
        motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        this.p = p; this.i = i; this.d = d;


    }

    public void setTarget(int target) { if (target != this.target) {this.target = target; this.errorSum = 0;} }
    public int getTarget() {return target;}
    public int getPos() {return motor.getCurrentPosition();}
    public void ResetPID()
    {
        // Will reset
        target = 0;
        errorSum = 0;

        motor.setMode(RunMode.STOP_AND_RESET_ENCODER);
        motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
        motor.setPower(0); //Added this for safety, it may not be necessary - Cai
    }

    public double[] getPID() {
        return new double[]{p,i,d};
    }

    public int getError()
    {
        final int error = target - motor.getCurrentPosition();
        if (Double.isNaN(lastError)) {
            lastError = error;
        } else {
            if(Math.signum((float) lastError) != Math.signum((float)error)){
                onErrorCrossSign();
            }
        }
        return error;
    }

    public void Update() { Update(null,(double)clock.tick()); }

    public void Update(double deltaTime) { Update(null,deltaTime);}

    public void Update(Telemetry telemetry) { Update(telemetry,(double) clock.tick());}

    public void Update(double deltaTime, Telemetry telemetry) {Update(telemetry,deltaTime);}
    private void onErrorCrossSign() {
        errorSum = 0.0;
    }
    public void Update(Telemetry telemetry,double deltaTime)
    {
        final double pOutput;
        final double iOutput;
        final double dOutput;

        int error = getError();

        pOutput = p * error;

        //Must be negative to "slow" down the effects of a large spike
        dOutput = -d * (error - lastError) / deltaTime;
        lastError = error;

        errorSum += error * deltaTime * i;

        errorSum = clamp(errorSum,-1.0,1.0);
        iOutput = errorSum;

        final double output = pOutput + iOutput + dOutput;

        motor.setPower(Math.tanh(output));
        if (telemetry != null) {
            telemetry.addLine("Output -> P: " + round(pOutput) + "  I: " + round(iOutput) + " D: " + round(dOutput));
        }
    }

}
