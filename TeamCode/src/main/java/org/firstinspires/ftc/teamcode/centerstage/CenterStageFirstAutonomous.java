package org.firstinspires.ftc.teamcode.centerstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.PoseStorage;
import org.firstinspires.ftc.teamcode.customclasses.Webcam;
import org.firstinspires.ftc.teamcode.customclasses.centerstage.LinearSlides;
import org.firstinspires.ftc.teamcode.customclasses.centerstage.PixelTiltOuttake;
import org.firstinspires.ftc.teamcode.customclasses.centerstage.TeamPropPosDetectPipeline;

import java.util.ArrayList;
import java.util.Arrays;
@Autonomous(name="CenterStageFirstAutonomous")
public class CenterStageFirstAutonomous extends CustomOpMode {

    private int trajectoryIndex = 0;
    private int autoVersion = 0;
    private ArrayList<Trajectory> trajectoriesToFollow = null;
    private ArrayList<Trajectory> defaultTrajectories = null;
    private ArrayList<Trajectory> leftTrajectories = null;
    private ArrayList<Trajectory> centerTrajectories = null;
    private ArrayList<Trajectory> rightTrajectories = null;
    private PoseStorage poseStorage = new PoseStorage();
    private PixelTiltOuttake pixelTiltOuttake = null;
    private LinearSlides linearSlides = null;
    private Webcam webcam = null;
    public void init() {

        super.init();
        //pixelTiltOuttake = new PixelTiltOuttake(hardwareMap);
        //linearSlides = new LinearSlides(hardwareMap, telemetry);
        webcam = new Webcam(hardwareMap);
        webcam.UseCustomPipeline();
        defaultTrajectories = CreateDefaultTrajectories();
        leftTrajectories = CreateLeftTrajectories();
        centerTrajectories = CreateCenterTrajectories();
        rightTrajectories = CreateRightTrajectories();
    }
    public void init_loop(){ autoVersion = AutoVersionUpdate(); }

    protected boolean handleState(RobotState state) {
        return true;
    }

    public void start() {
        switch (autoVersion)
        {
            case 1:
                trajectoriesToFollow = leftTrajectories;
                break;

            case 2:
                trajectoriesToFollow = centerTrajectories;
                break;

            case 3:
                trajectoriesToFollow = rightTrajectories;
                break;

            default:
                trajectoriesToFollow = defaultTrajectories;
        }
        drive.followTrajectoryAsync(trajectoriesToFollow.get(trajectoryIndex));
    }
    protected void onMainLoop() {
        drive.update();
        pixelTiltOuttake.Update();
        linearSlides.Update();
    }
    protected void onNextLoop() {
        trajectoryIndex++;
        drive.followTrajectoryAsync(trajectoriesToFollow.get(trajectoryIndex));
        drive.update();
        robotState = RobotState.MAIN;
    }
    protected void onStopLoop() {
        super.onStopLoop();
        robotState = RobotState.IDLE;
    }
    protected void onIdleLoop() {

    }

    private int AutoVersionUpdate() {
        webcam.teamPropPosDetectPipeline.PrintTelemetry(telemetry);
        return webcam.teamPropPosDetectPipeline.ReturnCurrentTeamPropPos();
    }

    private ArrayList<Trajectory> CreateDefaultTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        Trajectory test;
        //Trajectory test2;

        test = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(10,0),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }


    private ArrayList<Trajectory> CreateLeftTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        Trajectory test;
        //Trajectory test2;

        test = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(10,10),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }


    private ArrayList<Trajectory> CreateCenterTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        Trajectory test;
        //Trajectory test2;

        test = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(0,0),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }


    private ArrayList<Trajectory> CreateRightTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        Trajectory test;
        //Trajectory test2;

        test = drive.trajectoryBuilder(startPose)
                .splineTo(new Vector2d(0,10),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }
}
