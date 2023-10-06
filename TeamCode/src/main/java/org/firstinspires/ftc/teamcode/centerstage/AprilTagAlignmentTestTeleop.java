package org.firstinspires.ftc.teamcode.centerstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.checkerframework.checker.units.qual.A;
import org.firstinspires.ftc.teamcode.customclasses.AprilTagVisionPortalWebcam;
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
