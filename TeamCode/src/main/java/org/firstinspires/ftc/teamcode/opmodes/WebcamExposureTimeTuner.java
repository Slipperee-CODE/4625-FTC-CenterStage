package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.AprilTagAlign;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.webcam.AprilTagVisionPortalWebcam;

@Autonomous(name="ExposureTimeTuner")
public class WebcamExposureTimeTuner extends CustomOpMode {
    private AprilTagVisionPortalWebcam webcam;

    protected CustomGamepad gamepad1;
    protected CustomGamepad gamepad2;
    protected  double spin_speed = 0.1;

    public void init() {
        super.init();
        webcam = new AprilTagVisionPortalWebcam(telemetry,hardwareMap);

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
            webcam.SetExposure(webcam.GetExposure()+1);
        } else if (gamepad1.bDown) {
            webcam.SetExposure(webcam.GetExposure()-1);
        }
        webcam.GetDetections();
        robot.emulateController(0,0,spin_speed);

        telemetry.addData("Spin Speed: ",spin_speed);
        telemetry.addData("Exposure Time: ",webcam.GetExposure());
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
        webcam.GetDetections();
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
