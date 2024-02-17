package org.firstinspires.ftc.teamcode.opmodes.preILT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.TestTwoServoMechanism;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Disabled
@Autonomous(name="TestRoadrunnerAuto")
public class TestRoadrunnerAuto extends CustomOpMode {

    private TestTwoServoMechanism testTwoServoMechanism;

    private TrajectorySequence startTrajectory;
    private TrajectorySequence leftTrajectory;
    private TrajectorySequence centerTrajectory;
    private TrajectorySequence rightTrajectory;

    //Still need to implement April Tag Detection stuff, waiting so we can do that together
    //We also need to do the choosing of the trajectory after we detect the team prop, but that can be a custom finite state machine
    //for this OpMode specifically, we don't need to put it in CustomOpMode

    @Override
    public void init(){
        super.init();
        roadrunnerDrivetrain.setPoseEstimate(new Pose2d(0,0)); //Change this to actual robot starting position
        startTrajectory = CreateStartTrajectory();
        roadrunnerDrivetrain.followTrajectorySequenceAsync(startTrajectory);
        testTwoServoMechanism = new TestTwoServoMechanism(hardwareMap);
    }

    @Override
    public void init_loop(){

        super.init_loop();
    }

    @Override
    public void start(){

    }

    @Override
    public void mainLoop(){
        testTwoServoMechanism.update(telemetry);
        roadrunnerDrivetrain.update();
        super.loop();
    }

    @Override
    public void stop(){

        super.stop();
    }

    private TrajectorySequence CreateStartTrajectory(){
        TrajectorySequence trajectorySequence =
                roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                        .splineTo(new Vector2d(10,10),Math.PI)
                        .UNSTABLE_addTemporalMarkerOffset(0.0, () -> {testTwoServoMechanism.setState(MechanismState.CLOSED);}) //same as addTemporalMarker( () -> {} ) but we have the option for offset
                        .waitSeconds(1)
                        .splineTo(new Vector2d(0,0),Math.PI)
                        .UNSTABLE_addTemporalMarkerOffset(0.0, () -> {testTwoServoMechanism.setState(MechanismState.OPEN);})
                        .build();
        return trajectorySequence;
    }
}
