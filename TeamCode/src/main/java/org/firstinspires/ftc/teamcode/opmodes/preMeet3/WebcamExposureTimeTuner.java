package org.firstinspires.ftc.teamcode.opmodes.preMeet3;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;

@Disabled
@Autonomous(name="WebcamExposureTuner")
public class WebcamExposureTimeTuner extends CustomOpMode {
    private Webcam webcam;

    protected CustomGamepad gamepad1;
    protected CustomGamepad gamepad2;
    protected  double spin_speed = 0.0;

    public void init() {
        super.init();
        //try {
            webcam = new Webcam(hardwareMap, telemetry, true);
        ///} catch (Exception ignored) {
         //   telemetry.addLine("Exception on Webcam creation");
        //    sleep(500);
        //    telemetry.update();
        //    sleep(1000);

            //requestOpModeStop();
        //}
        gamepad1 = new CustomGamepad(this, 1);
        gamepad2 = new CustomGamepad(this, 2);
    }
    public void initLoop(){
        gamepad1.update();
        if (gamepad1.aDown) {
            spin_speed -= 0.05;
        } else if (gamepad1.xDown) {
            spin_speed += 0.05;
        } else if (gamepad1.yDown) {
            //webcam.SetExposure(webcam.GetExposure()+1);
        } else if (gamepad1.bDown) {
            //webcam.SetExposure(webcam.GetExposure()-1);
        }
        //webcam.GetDetections();
        robot.emulateController(0,0,spin_speed);

        telemetry.addData("Spin Speed: ",spin_speed);
        telemetry.addData("Exposure Time: ",webcam.getExposure());
    }
    protected boolean handleState(RobotState state) {
        return true;
    }
    public void start() {
        requestOpModeStop();
    }
    protected void onMainLoop() {
        gamepad1.update();
        gamepad2.update();
        telemetry.update();
    }
    protected void onNextLoop(){
    }

    protected void onStopLoop() {
        super.onStopLoop();
    }
    protected void onIdleLoop() {

    }
}
