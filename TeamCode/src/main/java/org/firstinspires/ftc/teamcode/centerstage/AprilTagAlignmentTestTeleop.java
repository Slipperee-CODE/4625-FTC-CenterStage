package org.firstinspires.ftc.teamcode.centerstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.customclasses.AprilTagWebcam;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.customclasses.Webcam;
import org.firstinspires.ftc.teamcode.customclasses.centerstage.AprilTagAlign;
import org.openftc.apriltag.AprilTagDetection;

import java.util.ArrayList;
import java.util.Arrays;
@Autonomous(name="AprilTagAlignmentTest")
public class AprilTagAlignmentTestTeleop extends CustomOpMode {
    private AprilTagAlign aprilTagAlign;
    private AprilTagDetection detectedTag;
    private Webcam webcam;
    private AprilTagWebcam aprilTagWebcam;
    protected CustomGamepad gamepad1;
    protected CustomGamepad gamepad2;


    public void init() {
        super.init();
        webcam = new Webcam(hardwareMap);
        aprilTagWebcam = new AprilTagWebcam(webcam.camera, telemetry);
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
        aprilTagWebcam.DetectTags();
        detectedTag = aprilTagWebcam.detectedTag;
        aprilTagAlign.Update(robot, aprilTagWebcam.GetDetections(), gamepad1);
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
