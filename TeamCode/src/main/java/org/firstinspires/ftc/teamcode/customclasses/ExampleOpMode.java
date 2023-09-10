package org.firstinspires.ftc.teamcode.customclasses;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.RoadRunnerTesting3;

import java.util.ArrayList;
import java.util.Arrays;
@Autonomous(name="RoadRunnerRectangle")
public class ExampleOpMode extends CustomOpMode{
    private TestRRMechanism testRRMechanism;
    private int trajectoryIndex;
    private ArrayList<Trajectory> trajectoriesToFollow;
    public void init() {

        super.init();
        testRRMechanism = new TestRRMechanism(hardwareMap);
        trajectoriesToFollow = CreateDefaultTrajectories();

    }
    protected boolean handleState(RobotState state) {
        return true;
    }
    public void start() {
        drive.followTrajectoryAsync(trajectoriesToFollow.get(trajectoryIndex));
    }
    protected void onMainLoop() {
        drive.update();
        testRRMechanism.Update();
        telemetry.addLine("MainRunning");
        telemetry.addData("Mechanism isBusy -> ", testRRMechanism.customMotor.isBusy());
        telemetry.addData("Mechanism isOn -> ", testRRMechanism.motorState);
        telemetry.update();
        //Update any other mechanisms
    }
    protected void onNextLoop() {
        trajectoryIndex++;
        drive.followTrajectoryAsync(trajectoriesToFollow.get(trajectoryIndex));
        drive.update();
        autonomousState = RobotState.MAIN;
    }
    protected void onStopLoop() {
        super.onStopLoop();
        autonomousState = RobotState.IDLE;
    }
    protected void onIdleLoop() {

    }
    private ArrayList<Trajectory> CreateDefaultTrajectories()
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
