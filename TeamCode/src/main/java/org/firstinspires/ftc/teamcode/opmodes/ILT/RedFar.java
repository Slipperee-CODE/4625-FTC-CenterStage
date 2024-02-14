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
@Autonomous(name = "Red Far Auto")
public class RedFar extends WaitingAuto {
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

        TrajectorySequenceBuilder bob = roadrunnerDrivetrain.trajectorySequenceBuilder(new Pose2d(36, -61.0, -Math.PI/2));

                //DETECT CENTER
        switch (detection) {
            case CENTER:
                return bob.setReversed(true)
                    .splineToLinearHeading(new Pose2d(31,-15,2 *Math.PI/3),Math.PI)
                    .waitSeconds(2)
                    .lineToLinearHeading(new Pose2d(30,-12,Math.PI))
                    .setReversed(false)
                    .splineTo(new Vector2d(-30,-12),Math.PI)
                    .setReversed(true)
                    .turn(Math.PI)
                    .splineTo(new Vector2d(-44,-34),Math.PI)
                    .build();
                //break;
            case LEFT:
                return bob.setReversed(true)
                    .lineToLinearHeading(new Pose2d(36, -34,0))
                    .back(5)
                    .waitSeconds(3)
                    .splineTo(new Vector2d(-44,-34),Math.PI)
                    .build();
            case RIGHT:
                return bob.setReversed(true)
                    .lineToLinearHeading(new Pose2d(36, -34,Math.PI))
                    .back(5)

                    .waitSeconds(3)
                    .forward(5)

                    // ENDING
                    .strafeTo(new Vector2d(36,-12))
                    .setReversed(false)
                    .splineTo(new Vector2d(-30,-12),Math.PI)
                    .turn(Math.PI)
                    .setReversed(true)
                    .splineTo(new Vector2d(-44,-34),Math.PI)
                    .build();
                //  drive.trajectorySequenceBuilder(new Pose2d(36, 61.0, -Math.PI/2))
            //                                //DETECT RIGHT
            //
            //                                .setReversed(true)
            //                                .lineToLinearHeading(new Pose2d(36, 34,Math.PI))
            //                                .back(5)
            //
            //                                .waitSeconds(3)
            //                                .forward(5)
            //
            //                                // ENDING
            //                                .strafeTo(new Vector2d(36,12))
            //                                .setReversed(false)
            //                                .splineTo(new Vector2d(-30,12),Math.PI)
            //                                .turn(Math.PI)
            //                                .setReversed(true)
            //                                .splineTo(new Vector2d(-44,34),Math.PI)
            //
            //                                .build());

        }
        return null;

    }

}
