package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.sun.tools.javac.Main;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.TeamPropTestColorSensor;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceRunner;
import org.jetbrains.annotations.Contract;

import java.util.ArrayList;

import kotlin.OverloadResolutionByLambdaReturnType;

@Autonomous(name = "Blue Close")
public class CompBlueClose extends WaitingAuto {
    private TeamPropDetection teamPropDetection;
    private RobotState autoState = RobotState.DRIVE_TO_TEAM_PROP;
    private TrajectorySequence driveToTeamPropSequence;
    private final ArrayList<MechanismState> detections = new ArrayList<>();
    private Outtake outtake;
    private TrajectorySequence finishingTrajectories;
    private final Clock detectionTime = new Clock();
    @Override

    public void init() {
        super.init();
        teamPropDetection = new TeamPropDetection(hardwareMap);
        robotState = RobotState.MAIN;
        driveToTeamPropSequence =  drive.trajectorySequenceBuilder(new Pose2d(38.0, -61.0, Math.toRadians(-90))).forward(26).addTemporalMarker(() -> autoState = {RobotState.DETECT_TEAM_PROP;detectionTime.reset();}).build();
        drive.followTrajectorySequenceAsync(driveToTeamPropSequence);
        outtake = new Outtake(hardwareMap,new CustomGamepad(this,1));
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
    protected void update() {
        switch (autoState) {
            case DRIVE_TO_TEAM_PROP:
                drive.update();
                // here we should be following the forward 26 cause why not

            case DETECT_TEAM_PROP:
                robot.stop();
                teamPropDetection.update();

                detections.add(teamPropDetection.state);

                if (detectionTime.getTimeSeconds() > 2.0) {
                    MechanismState mode = getModeOfArray(detections);
                    if (mode == MechanismState.LEFT) {
                        finishingTrajectories = buildLeft();
                    } else if (mode == MechanismState.RIGHT) {
                        finishingTrajectories = buildRight();
                    } else if (mode == MechanismState.FORWARD) {
                        finishingTrajectories = buildForward();
                    }
                    autoState = RobotState.MAIN;
                    // we get the things we gotta do
                }
            case MAIN:
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
        if (lefts >= rights && lefts >= forwards) {
            return MechanismState.LEFT;
        }
        if (rights >= lefts && rights >= forwards) {
            return MechanismState.RIGHT;
        }
        return MechanismState.FORWARD;
    }


    private TrajectorySequence buildLeft() {
        drive.trajectorySequenceBuilder(drive.getPoseEstimate())
                //Left
                .splineTo(new Vector2d(-35,-34),Math.PI)
                .addTemporalMarker(() -> {outtake.setDropLowerPositionWithLidClosed();})
                .waitSeconds(.3)// Dumpy
                .addTemporalMarker(() -> {outtake.drop();})
                .waitSeconds(.2)
                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .addTemporalMarker(() -> {outtake.setDropNormalPosition();})
                .addTemporalMarker(() -> outtake.)
                .build();

    }
}
