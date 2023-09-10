package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.PoseStorage;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.customclasses.TestRRMechanism;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "RoadRunnerTesting3")
public class RoadRunnerTesting3 extends LinearOpMode
{
    private SampleMecanumDrive drive = null;
    private Robot robot = null;

    private TestRRMechanism testRRMechanism = null;


    private int autoVersion = 0;
    private ArrayList<Trajectory> trajectoriesToFollow;
    private int trajectoryIndex = 0;
    private PoseStorage poseStorage = new PoseStorage();

    private AutoState autoState = AutoState.MAIN;
    private enum AutoState {
        MAIN,
        NEXT,
        STOP,
        IDLE,
    }

    private ArrayList<Trajectory> defaultTrajectories;


    @Override
    public void runOpMode()
    {
        OnInit();

        while (!isStarted() && !isStopRequested()) //INIT LOOP
        {
            InitLoop();
        }
        OnStart();

        while(!isStopRequested())
        {
            Loop();
        }
        OnStop();
    }


    private void OnInit()
    {
        drive = new SampleMecanumDrive(hardwareMap);
        robot = new Robot(hardwareMap);
        testRRMechanism = new TestRRMechanism(hardwareMap);


        defaultTrajectories = CreateDefaultTrajectories();
        //Create other case trajectories here
    }


    private void InitLoop()
    {
        autoVersion = AutoVersionUpdate();
    }


    private void OnStart()
    {
        switch (autoVersion)
        {
            case 1:
                //case 1
                //Follow case trajectories here

            case 2:
                //case 2
                //Follow case trajectories here

            case 3:
                //case 3
                //Follow case trajectories here

            default:
                trajectoriesToFollow = defaultTrajectories;

            drive.followTrajectoryAsync(trajectoriesToFollow.get(trajectoryIndex));
        }
    }


    private void Loop()
    {
        switch (autoState)
        {
            case MAIN:
                drive.update();
                testRRMechanism.Update();
                telemetry.addLine("MainRunning");
                telemetry.addData("Mechanism isBusy -> ", testRRMechanism.customMotor.isBusy());
                telemetry.addData("Mechanism isOn -> ", testRRMechanism.motorState);
                telemetry.update();
                //Update any other mechanisms

                break;

            case NEXT:
                trajectoryIndex++;
                drive.followTrajectoryAsync(trajectoriesToFollow.get(trajectoryIndex));
                drive.update();
                autoState = AutoState.MAIN;
                break;

            case STOP:
                drive.setMotorPowers(0,0,0,0);
                autoState = AutoState.IDLE;
                break;

            case IDLE:
                //Don't do anything because the auto is over
                break;
        }
        poseStorage.currentPose = drive.getPoseEstimate(); //Always set pose estimate just in case auto stops early
    }


    private void OnStop()
    {

    }


    private ArrayList<Trajectory> CreateDefaultTrajectories()
    {
        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));
        drive.setPoseEstimate(startPose);

        Trajectory test;
        Trajectory test2;

        test = drive.trajectoryBuilder(new Pose2d())
                .splineTo(new Vector2d(10,10),Math.toRadians(0))
                .addDisplacementMarker(() -> {
                    //Run Robot Code to Start/Stop Systems Here Mid Trajectory
                    testRRMechanism.motorState = TestRRMechanism.MotorState.ON;
                })
                .splineTo(new Vector2d(20,20),Math.toRadians(0))
                .addDisplacementMarker(() -> {
                    testRRMechanism.motorState = TestRRMechanism.MotorState.OFF;
                    autoState = AutoState.NEXT;
                })
                .build();

        test2 = drive.trajectoryBuilder(test.end())
                .splineTo(new Vector2d(30,30),Math.toRadians(0))
                .addDisplacementMarker(() -> {
                    //Run Robot Code to Start/Stop Systems Here Mid Trajectory
                })
                .splineTo(new Vector2d(0,0),Math.toRadians(0))
                .addDisplacementMarker(() -> {
                    drive.setMotorPowers(0,0,0,0);
                    autoState = AutoState.STOP; //End Auto
                })
                .build();


        return new ArrayList<>(Arrays.asList(test,test2));
    }


    private int AutoVersionUpdate()
    {
        //Update the Auto Version Here (April Tags, Color Detection, etc.)

        return 0;
    }
}

