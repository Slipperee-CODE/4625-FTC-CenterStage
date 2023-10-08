package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.webcam.AprilTagVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.AprilTagAlign;

@Autonomous(name="AprilTagAlignmentTest")
public class AprilTagAlignmentTestTeleop extends CustomOpMode {
    private AprilTagAlign aprilTagAlign;
    private AprilTagVisionPortalWebcam webcam;

    protected CustomGamepad gamepad1;
    protected CustomGamepad gamepad2;


    public void init() {
        super.init();
        webcam = new AprilTagVisionPortalWebcam(telemetry,hardwareMap);

        gamepad1 = new CustomGamepad(this, 0);
        gamepad2 = new CustomGamepad(this, 1);
        aprilTagAlign = new AprilTagAlign(hardwareMap, telemetry);
        aprilTagAlign.state = AprilTagAlign.State.ON;
    }
    public void initLoop(){}
    protected boolean handleState(RobotState state) {
        return true;
    }
    public void start() {

    }
    protected void onMainLoop() {
        gamepad1.Update();
        gamepad2.Update();
        //detectedTag = aprilTagWebcam.detectedTag;
        aprilTagAlign.Update(robot, webcam.GetDetections(), gamepad1);
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
