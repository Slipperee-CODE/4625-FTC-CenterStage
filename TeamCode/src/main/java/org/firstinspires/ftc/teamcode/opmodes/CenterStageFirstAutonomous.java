package org.firstinspires.ftc.teamcode.opmodes;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;
import org.firstinspires.ftc.teamcode.customclasses.webcam.ComplicatedPosPipeline;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

@Autonomous(name="BlueCenterStageFirstAutonomous")
public class CenterStageFirstAutonomous extends CustomOpMode {
    //Roadrunner Stuff
    // Conventions to Follow : the back of the field is the side with the scoring boards, front is the other side with the big apriltags
    // Remember that the when centered to field and heading is 0 then the robot is facing the right because the heading 0 is to the right on a unit circle

    private ArrayList<TrajectorySequence> trajectoriesToFollow = null;
    private ArrayList<TrajectorySequence> defaultTrajectories = null;
    private ArrayList<TrajectorySequence> leftTrajectories = null;
    private ArrayList<TrajectorySequence> centerTrajectories = null;
    private ArrayList<TrajectorySequence> rightTrajectories = null;
    private boolean onBiasDone = false;
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
        //defaultTrajectories = CreateDefaultTrajectories();
        //leftTrajectories = CreateLeftTrajectories();
        //centerTrajectories = CreateCenterTrajectories();
        //rightTrajectories = CreateRightTrajectories();

        clock = new Clock();
        MissingHardware.printMissing(telemetry);
        sleep(1000);

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

    protected boolean handleState(RobotState state) { return true; }

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
        // Just getting a test auto
        trajectoriesToFollow = CreateLeftTrajectories();
        drive.followTrajectorySequenceAsync(trajectoriesToFollow.get(trajectoryIndex));
    }
    protected void onMainLoop() {
        drive.update();
        //pixelTiltOuttake.Update();
        //linearSlides.Update();
    }
    protected void onNextLoop() {
        trajectoryIndex++;
        drive.followTrajectorySequenceAsync(trajectoriesToFollow.get(trajectoryIndex));
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

    private ArrayList<TrajectorySequence> CreateDefaultTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        TrajectorySequence test;
        //Trajectory test2;

        test = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(10,0),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }

    private ArrayList<TrajectorySequence> CreateLeftTrajectories()
    {
       // Remember that we are on the left side and towards the front facing in the PI direction
       // the team prop was detected at the left side so that means that we have to align ourselves with
        // APRIL TAG 1 (LEFT MOST APRIL TAG)

        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(180));
        drive.setPoseEstimate(startPose);

        TrajectorySequence test;
        //Trajectory test2;

        test = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(-12,0),Math.toRadians(180))
                .addDisplacementMarker(() -> {sleep(1000L);})
                .splineTo(new Vector2d(-10,0),Math.toRadians(180))
                .turn(Math.toRadians(90))
                .splineTo(new Vector2d(-10,0),Math.toRadians(270))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }

    private ArrayList<TrajectorySequence> CreateCenterTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        TrajectorySequence test;
        //Trajectory test2;

        test = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(0,0),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }

    private ArrayList<TrajectorySequence> CreateRightTrajectories()
    {
        Pose2d startPose = new Pose2d(0, -10, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        TrajectorySequence test;
        //Trajectory test2;

        test = drive.trajectorySequenceBuilder(startPose)
                .splineTo(new Vector2d(0,10),Math.toRadians(0))
                .build();

        return new ArrayList<>(Arrays.asList(test));
    }
}
