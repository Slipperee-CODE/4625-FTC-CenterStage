package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.acmerobotics.roadrunner.trajectory.Trajectory;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

@Autonomous(name = "Blue Close")
public class CompBlueClose extends WaitingAuto {
    private TeamPropDetection teamPropDetection;
    private RobotState autoState = RobotState.DRIVE_TO_TEAM_PROP;
    private Trajectory driveToTeamPropSequence;
    private final ArrayList<MechanismState> detections = new ArrayList<>();
    private Outtake outtake;
    private TrajectorySequence trajectoriesThatAreMid;
    private TrajectorySequence trajectoriesThatArentMidButInsteadAreFinalClassConstructorClassFactoryFactoryFactorySingletonAbstractManager_inator;
    private final Clock detectionTime = new Clock();
    @Override

    public void init() {
        super.init();
        teamPropDetection = new TeamPropDetection(hardwareMap);
        autoState = RobotState.DRIVE_TO_TEAM_PROP;
        robotState = RobotState.MAIN;
        driveToTeamPropSequence =  drive.trajectoryBuilder(new Pose2d(38.0, -61.0, Math.toRadians(-90))).forward(26).addTemporalMarker(4_000,() ->{ autoState = RobotState.DETECT_TEAM_PROP;detectionTime.reset();}).build();
        drive.followTrajectoryAsync(driveToTeamPropSequence);
        outtake = new Outtake(hardwareMap,new CustomGamepad(this,1));
        trajectoriesThatArentMidButInsteadAreFinalClassConstructorClassFactoryFactoryFactorySingletonAbstractManager_inator = buildEnding();

    }
    @Override
    protected void onNextLoop() {

    }

    @Override
    protected void onIdleLoop() {

    }

    @Override
    protected boolean handleState(RobotState state) { return true;}
    @Override
    public void onStopLoop() {
        super.onStopLoop();
        autoState = RobotState.MAIN;
    }
    @Override
    protected void update() {
        switch (autoState) {
            case DRIVE_TO_TEAM_PROP:
                drive.update();
                telemetry.addLine("DRIVING TO TEAM PROP");
                // here we should be following the forward 26 cause why not

            case DETECT_TEAM_PROP:
                robot.stop();
                teamPropDetection.update();
                telemetry.addLine("DETECTING TEAM PROP");
                detections.add(teamPropDetection.state);

                if (detectionTime.getTimeSeconds() > 2.0) {
                    MechanismState mode = getModeOfArray(detections);
                    if (mode == MechanismState.LEFT) {
                        trajectoriesThatAreMid = buildLeft();
                    } else if (mode == MechanismState.RIGHT) {
                        trajectoriesThatAreMid = buildRight();
                    } else if (mode == MechanismState.FORWARD) {
                        trajectoriesThatAreMid = buildForward();
                    }
                    drive.followTrajectorySequenceAsync(trajectoriesThatAreMid);
                    autoState = RobotState.MID;
                    // we get the things we gotta do
                }
            case MID:
                drive.update();
            case FINAL:
                drive.update();
            case MAIN:
                telemetry.addLine("WE ARE DONE! :)");
                telemetry.addLine("WE ARE DUMB! :)");
                robot.stop();
        }
    }
    @Contract(pure = true)
    private MechanismState getModeOfArray(ArrayList<MechanismState> arrayList) {
        int lefts = 0;
        int rights = 0;
        int forwards = 0;
        for (MechanismState state : arrayList) {
            switch (state) {
                case LEFT:
                    lefts++;
                    break;
                case RIGHT:
                    rights++;
                    break;
                case FORWARD:
                    forwards++;
            }
        }
        /// AVERAGE AP COMPUTER SCIENCE - A Student (below)
        if (lefts >= rights && lefts >= forwards) {
            return MechanismState.LEFT;
        }
        if (rights >= lefts && rights >= forwards) {
            return MechanismState.RIGHT;
        }
        return MechanismState.FORWARD;
    }


    private TrajectorySequence buildLeft() {
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                //Left
                .splineTo(new Vector2d(-35,-34),Math.PI)
                .addTemporalMarker(() -> {outtake.setDropLowerPositionWithLidClosed();})
                .waitSeconds(.3)// Dumpy
                .addTemporalMarker(() -> {outtake.drop();})
                .waitSeconds(.2)
                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .addTemporalMarker(() -> {autoState = RobotState.FINAL;drive.followTrajectorySequenceAsync(trajectoriesThatArentMidButInsteadAreFinalClassConstructorClassFactoryFactoryFactorySingletonAbstractManager_inator);})
                .build();
    }
    private TrajectorySequence buildRight() {
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                //Right
                .turn(Math.PI/2)
                .splineTo(new Vector2d(-35,-34),Math.PI)
                .addTemporalMarker(() -> {outtake.setDropLowerPosition();})
                .waitSeconds(.3)// Dumpy
                .addTemporalMarker(() -> {outtake.drop();})
                .waitSeconds(.2)
                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .addTemporalMarker(() -> {autoState = RobotState.FINAL;drive.followTrajectorySequenceAsync(trajectoriesThatArentMidButInsteadAreFinalClassConstructorClassFactoryFactoryFactorySingletonAbstractManager_inator);})

                .build();
    }
    private TrajectorySequence buildForward() {
        return drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                // Center
                .splineTo(new Vector2d(-24,-24),Math.PI)
                .addTemporalMarker(() -> outtake.setDropLowerPosition())
                .waitSeconds(.3)// Dumpy
                .addTemporalMarker(() -> outtake.drop())
                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .addTemporalMarker(() -> {autoState = RobotState.FINAL;drive.followTrajectorySequenceAsync(trajectoriesThatArentMidButInsteadAreFinalClassConstructorClassFactoryFactoryFactorySingletonAbstractManager_inator);})
                .build();

    }
    private TrajectorySequence buildEnding() {
        return drive.trajectorySequenceBuilder(new Pose2d(-44,-34,Math.PI))
                .addTemporalMarker(() -> outtake.setDropNormalPosition())
                //.addTemporalMarker(() -> outtake.SlidesMotor.setRawPower(.3))
                .waitSeconds(.3)
                //.addTemporalMarker(()->outtake.SlidesMotor.setRawPower(0))
                .addTemporalMarker(() ->robot.stop())
                .waitSeconds(.1)
                .addTemporalMarker(() -> outtake.drop())
                .addTemporalMarker(()-> {autoState = RobotState.STOP;})
                .build();
    }
}
