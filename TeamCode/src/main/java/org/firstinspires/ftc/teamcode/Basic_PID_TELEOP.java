package org.firstinspires.ftc.teamcode;


import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;

@TeleOp(name = "pid teleop :)")
public class Basic_PID_TELEOP extends OpMode {

    PIDMotor motor = null;
    public void init() {
        telemetry.setMsTransmissionInterval(11);
        motor = new PIDMotor(hardwareMap.get(DcMotor.class,"testMotor"),1,0,0); //Changed the motor name to match the one configured rn - Cai
        Test();
        motor.ResetPID();
        motor.setTarget(10_000);
    }
    public void Test() {
        double start = getRuntime();
        for (int i = 0; i < 1_000_000;i++){
            motor.Update();
        }
        double end = getRuntime();
        telemetry.addData("Seconds per 1 million updates",end-start);
        telemetry.update();
        sleep(5000);
        motor.ResetPID();
    }
    public void loop() {
        motor.Update(telemetry);

        telemetry.update();
    }


    public final void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
