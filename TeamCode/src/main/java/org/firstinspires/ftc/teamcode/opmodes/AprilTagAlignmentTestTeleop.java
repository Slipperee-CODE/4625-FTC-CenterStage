package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.webcam.AprilTagVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.AprilTagAlign;

@Autonomous(name="AprilTagAlignmentTest")
public class AprilTagAlignmentTestTeleop extends CustomOpMode {
    private Mechanism aprilTagAlign;
    private AprilTagVisionPortalWebcam webcam;

    protected CustomGamepad gamepad1;
    protected CustomGamepad gamepad2;

    public void init() {
        super.init();
        webcam = new AprilTagVisionPortalWebcam(telemetry,hardwareMap);

        gamepad1 = new CustomGamepad(this, 1);
        gamepad2 = new CustomGamepad(this, 2);
        aprilTagAlign = new AprilTagAlign(hardwareMap, telemetry, gamepad1,robot);
        aprilTagAlign.setState(MechanismState.ON);
    }
    public void initLoop(){}
    protected boolean handleState(RobotState state) {
        return true;
    }
    public void start() {
    }
    protected void onMainLoop() {
        gamepad1.update();
        gamepad2.update();
        webcam.GetDetections();
        aprilTagAlign.update();
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
