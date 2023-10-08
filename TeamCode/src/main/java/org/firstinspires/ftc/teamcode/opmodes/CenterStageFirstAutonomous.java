package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.unused.Webcam;
import org.firstinspires.ftc.teamcode.customclasses.webcam.ComplicatedPosPipeline;

import java.util.ArrayList;
import java.util.Collections;

@Autonomous(name="CenterStageFirstAutonomous")
public class CenterStageFirstAutonomous extends CustomOpMode {
    //Roadrunner Stuff
    private ArrayList<Trajectory> trajectoriesToFollow = null;
    private ArrayList<Trajectory> defaultTrajectories = null;
    private ArrayList<Trajectory> leftTrajectories = null;
    private ArrayList<Trajectory> centerTrajectories = null;
    private ArrayList<Trajectory> rightTrajectories = null;
    private boolean onBiasDone = false;
    //private PoseStorage poseStorage = new PoseStorage();
    private int timesTuned = 0;

    // Mechanisms || Webcams || Timers
    private Clock clock = null;
    //private final PixelTiltOuttake pixelTiltOuttake = null;
    //private final LinearSlides linearSlides = null;
    private Webcam webcam = null;

    // Miss
    private int trajectoryIndex = 0;
    private int autoVersion = 0;
    private double MaxBiasFixingTime = 10.0; // How much maximum time it should take to tune the bias. in seconds
    private boolean tuningBias = true;

    public void init() {
        super.init();
        telemetry.setMsTransmissionInterval(0);
        //pixelTiltOuttake = new PixelTiltOuttake(hardwareMap);
        //linearSlides = new LinearSlides(hardwareMap, telemetry);
        webcam = new Webcam(hardwareMap);
        webcam.UseCustomPipeline(new ComplicatedPosPipeline("Blue"));
        defaultTrajectories = CreateDefaultTrajectories();
        leftTrajectories = CreateLeftTrajectories();
        centerTrajectories = CreateCenterTrajectories();
        rightTrajectories = CreateRightTrajectories();

        clock = new Clock();
    }
    private void tuneBias() {
        // Assume that the field is empty
        // First step is to get the process the current frame
        if (webcam.pipeline.tuneBias()) {timesTuned ++;}
        webcam.pipeline.PrintTelemetry(telemetry);
        telemetry.addLine("Tuning... ") ;
    }
    protected void initLoop() {
        if (tuningBias) {
            MaxBiasFixingTime -= clock.getDeltaSeconds();
            telemetry.addData("Time Left", MaxBiasFixingTime);
            if (MaxBiasFixingTime < 0) { tuningBias = false; }
            tuneBias();
        }
        else {
            if(!onBiasDone) {
                webcam.pipeline.manualTuneBias(0,-0.04,0);
                onBiasDone = true;
                webcam.pipeline.setDebug(false);
            }
            telemetry.addData("Times Tuned:",timesTuned);
            autoVersion = AutoVersionUpdate();
        }
    }

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
        //pixelTiltOuttake.Update();
        //linearSlides.Update();
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

    protected void onIdleLoop() {}

    private int AutoVersionUpdate() {
        webcam.pipeline.PrintTelemetry(telemetry);
        return webcam.pipeline.ReturnCurrentTeamPropPos();
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

        return new ArrayList<>(Collections.singletonList(test));
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

        return new ArrayList<>(Collections.singletonList(test));
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

        return new ArrayList<>(Collections.singletonList(test));
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

        return new ArrayList<>(Collections.singletonList(test));
    }
}
