package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.PIDMotor;

public class AntiStall extends PIDMotor {
    public static final double TIME_THRESHOLD = 0.3; // how many seconds should pass before a stall is declared
    public static final double STALL_THRESHOLD = 0.2; //percent of expected power before it is counted as a stall
    public static final double MAX_TICKS_PER_SECOND = 1200; // not sure either????? lol  (you can tune this by running the motor at full power in anny direction and finding out the ticks per second that way
    private DcMotor reserve = null;
    private double timeTillDeclareStall = TIME_THRESHOLD;
    public AntiStall(DcMotor motor, double p, double i, double d) {
        super(motor, p, i, d);
    }
    @Override
    public void Update(Telemetry telemetry, double deltaTime) {

        if (reserve != null){
            if (telemetry != null) {
                telemetry.addData("STALL TRIGGERED!: ", reserve.getDeviceName() + " -> Port #" + reserve.getPortNumber());
            }
            return; // we have stalled and therfore are useless so we return
        }
        final double pOutput;
        final double iOutput;
        final double dOutput;

        int error = getError();
        if (error * lastError < 0) {
            // error crossed signs
            errorSum = 0.0;
        }
        pOutput = p * error;

        //Must be negative to "slow" down the effects of a large spike
        dOutput = -d * (error - lastError) / deltaTime;
        if (error - lastError < INTEGRAL_START_THRESHOLD) {
            errorSum += error * deltaTime * i;
        }

        errorSum = clamp(errorSum, -1.0, 1.0);
        iOutput = errorSum;

        final double output = pOutput + iOutput + dOutput;
        if (Math.abs(output) > POWER_THRESHOLD) {
            final double finalPower = Math.tanh(output);
            motor.setPower(finalPower);
            // there the motor should be powered
            //
            final double TICKS_PER_SECOND = Math.abs(error - lastError) / deltaTime;
            final double PROJECTED_TICKS_PER_SECOND = finalPower * MAX_TICKS_PER_SECOND;
            if (TICKS_PER_SECOND < PROJECTED_TICKS_PER_SECOND * STALL_THRESHOLD) {// if we are moving at less than a specificed percentage of the projected ticks per second

                timeTillDeclareStall -= deltaTime;
                if (timeTillDeclareStall < 0 || Math.abs(errorSum) > 0.9) { // if we have an insane amountof error sum but we are still moving pretty slowly then somthing is wrong and we dont gotta wait for the time because it could be dangerous
                    onDetectedStall();

                }


            } else {
                //stall didn't happen so we reset the counter
                timeTillDeclareStall = TIME_THRESHOLD;
            }
        } else {
            motor.setPower(0);
            if (telemetry != null) telemetry.addLine("Output -> P: " + round(pOutput) + "  I: " + round(iOutput) + " D: " + round(dOutput));
        }
    }
    public void onDetectedStall() {
        if (reserve != null) return; // we cant stall more than once
        motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        motor.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        motor.setPower(0);
        reserve = motor;
        motor = new MechanismBase.EmptyDcMotor(); // we give them a fake motor to play with
    }


}
