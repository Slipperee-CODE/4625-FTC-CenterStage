package org.firstinspires.ftc.teamcode.disabled;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

import java.util.ArrayList;

@Autonomous(name = "RoadRunnerTesting")
@Disabled
public class RoadRunnerTesting extends LinearOpMode
{
    @Override
    public void runOpMode()
    {
        SampleMecanumDrive drive = new SampleMecanumDrive(hardwareMap);
        Robot robot = new Robot(hardwareMap);
        int autoVersion = 0;


        Pose2d startPose = new Pose2d(0, 0, Math.toRadians(0));
        drive.setPoseEstimate(startPose);


        Trajectory test = drive.trajectoryBuilder(new Pose2d())
                .splineToSplineHeading(new Pose2d(10,10),Math.toRadians(180))
                .addDisplacementMarker(() -> {
                    //Run Robot Code to Start/Stop Systems Here Mid Trajectory
                })
                .splineToSplineHeading(new Pose2d(20,20),Math.toRadians(180))
                .build();

        Trajectory test2 = drive.trajectoryBuilder(test.end())
                .splineToSplineHeading(new Pose2d(30,30),Math.toRadians(180))
                .addDisplacementMarker(() -> {
                    //Run Robot Code to Start/Stop Systems Here Mid Trajectory
                })
                .splineToSplineHeading(new Pose2d(0,0),Math.toRadians(180))
                .build();


        while (!isStarted() && !isStopRequested()) //INIT LOOP
        {
            autoVersion = AutoVersionUpdate();
        }


        switch (autoVersion)
        {
            case 1:
                //case 1
            case 2:
                //case 2
            case 3:
                //case 3

            default:
                drive.followTrajectory(test);
                //Run Robot Code to Start/Stop Systems Here In-Between Trajectories
                drive.followTrajectory(test2);
        }
    }


    private int AutoVersionUpdate()
    {
        //Update the Auto Version Here (April Tags, Color Detection, etc.)
        return 0;
    }
}

