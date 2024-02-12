package org.firstinspires.ftc.teamcode.opmodes.ILT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.BlueContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes.BlueContourVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.opmodes.preILT.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

/**
 * This version uses Cai's Camera Contour Magic in order to build
 * the trajectory initially and not have to create it on the fly
 */
@Autonomous(name = "Blue Close Auto")
public class BlueFar extends WaitingAuto {
    public enum State {
        DRIVE,
        SCORE_BACKDROP,
        SCORE_SCORE,
    }
    BlueContourVisionProcessor.TeamPropState tpPosition;
    State autoState = State.DRIVE;
    TeamPropDetection teamPropDetection;
    BlueContourVisionPortalWebcam blueContourVisionPortalWebcam;

    Outtake outtake;


    public void init() {
        super.init();
        telemetry.addLine("Not Ready Yet!!");
        telemetry.update();
        //teamPropDetection = new TeamPropDetection(hardwareMap);
        //blueContourVisionPortalWebcam = new BlueContourVisionPortalWebcam(hardwareMap);
        outtake = new Outtake(hardwareMap,new CustomGamepad(gamepad1));
        //roadrunnerDrivetrain.followTrajectorySequenceAsync(buildInitialTrajectories());
    }
    private String makeLoadingString(int maxDots) {
        StringBuilder s = new StringBuilder(maxDots);
        int dots = (int) (2*timer.getTimeSeconds()) % maxDots;
        for (int i = 0 ; i < dots; i++) {
            s.append(".");
        }
        return s.toString();
    }

    public void init_loop() {
        super.init_loop();
        tpPosition = BlueContourVisionProcessor.TeamPropState.CENTER;//blueContourVisionPortalWebcam.GetTeamPropState();
        telemetry.addData("Detected Position", tpPosition);
        telemetry.addLine("Safe To Proceed");
        telemetry.addLine(makeLoadingString(5));
        telemetry.update();
    }
    public void startBeforeWait() {
        roadrunnerDrivetrain.followTrajectorySequenceAsync(buildTrajectory(tpPosition));
    }

    @Override
    protected void update() {
        switch (autoState) {
            case DRIVE:
                roadrunnerDrivetrain.update();
                if (!roadrunnerDrivetrain.isBusy()) {
                    autoState = State.SCORE_BACKDROP;
                }
                telemetry.addData("PoseEstimate",roadrunnerDrivetrain.getPoseEstimate());
                break;
            case SCORE_BACKDROP:
                roadrunnerDrivetrain.update();
                telemetry.addLine("SCOREBACKDROP!!!");
                robotDrivetrain.stop();
                outtake.setLinearSlidesPosition(Outtake.LinearSlidesPosition.FIRST_ROW);
                outtake.setDropPosition();
                outtake.procrastinate(3,this.outtake::drop);
                outtake.update();
                autoState = State.SCORE_SCORE;
                break;
            case SCORE_SCORE:
                telemetry.addLine("SCORE SCORE");
                roadrunnerDrivetrain.update();
                outtake.update();

                break;
        }
    }

    private TrajectorySequence buildTrajectory(BlueContourVisionProcessor.TeamPropState detection) {
        roadrunnerDrivetrain.setPoseEstimate(new Pose2d(-12, -61.0, Math.PI/2));
        TrajectorySequenceBuilder bob = roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                .forward(26)
                .waitSeconds(0.1);

        switch (detection) {
            case LEFT:
                bob.splineTo(new Vector2d(-35,-34),Math.PI)
                    .addTemporalMarker(() -> robotMechanisms.pixelQuickRelease.setDrop())
                    .waitSeconds(0.5);// Dumpy
                break;
            case CENTER:
                bob.splineTo(new Vector2d(-24,-24),Math.PI)
                    .addTemporalMarker(() -> robotMechanisms.pixelQuickRelease.setDrop())
                    .waitSeconds(0.5);// Dumpy
                break;
            case RIGHT:
                bob.turn(Math.PI/2)
                    .back(4)
                    .addTemporalMarker(() -> robotMechanisms.pixelQuickRelease.setDrop())
                    .waitSeconds(.5)// Dumpy
                    .forward(4)
                    .splineTo(new Vector2d(-35,-34),Math.PI);
                break;
        }
        // ENDING
        bob.splineTo(new Vector2d(-44,-34),Math.PI);
        return bob.build();
    }

}
