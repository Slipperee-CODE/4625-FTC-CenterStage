package org.firstinspires.ftc.teamcode.opmodes.ILT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.preILT.Clock;
import org.firstinspires.ftc.teamcode.customclasses.preILT.ContourAndAprilTagWebcam;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.PixelQuickRelease;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.AprilTagAlign;
import org.firstinspires.ftc.teamcode.customclasses.webcam.AprilTagVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.BlueContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.ContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes.BlueContourVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.opmodes.preILT.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

/**
 * This version uses Cai's Camera Contour Magic in order to build
 * the trajectory initially and not have to create it on the fly
 */
@Autonomous(name = "Blue Far Auto")
public class BlueFar extends WaitingAuto {
    public enum State {
        DRIVE,
        SCORE_BACKDROP,
        SCORE_SCORE,
    }
    State autoState = State.DRIVE;

    ContourAndAprilTagWebcam multipurposeWebcam;
    AprilTagAlign aprilTagAlign;
    PixelQuickRelease pixelQuickRelease;
    ContourVisionProcessor.TeamPropState tpPosition;
    Clock timer;
    Outtake outtake;


    public void init() {
        super.init();

        telemetry.addLine("Not Ready Yet!!");
        telemetry.update();
        timer = new Clock();
        //teamPropDetection = new TeamPropDetection(hardwareMap);
        multipurposeWebcam = new ContourAndAprilTagWebcam(hardwareMap);
        multipurposeWebcam.setActiveProcessor(ContourAndAprilTagWebcam.Processor.CONTOUR);
        multipurposeWebcam.setExposure(16);
        multipurposeWebcam.setGain(100);
        multipurposeWebcam.SetContourColor(ContourVisionProcessor.Color.BLUE);
        // aprilTagVisionPortalWebcam = new AprilTagVisionPortalWebcam(telemetry,hardwareMap);
        //blueContourVisionPortalWebcam = new BlueContourVisionPortalWebcam(hardwareMap);
        outtake = new Outtake(hardwareMap,new CustomGamepad(gamepad1));
        pixelQuickRelease = new PixelQuickRelease(hardwareMap,new CustomGamepad(gamepad2),false);
        pixelQuickRelease.setState(MechanismState.CLOSED);
        aprilTagAlign = new AprilTagAlign(hardwareMap,telemetry,null,robotDrivetrain);
        aprilTagAlign.setState(org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState.OFF);
    }
    private String makeLoadingString(int maxDots) {
        StringBuilder s = new StringBuilder(maxDots);
        int dots = (int) (2*timer.getTimeSeconds()) % maxDots;
        for (int i = 0 ; i < dots; i++) {
            s.append("*");
        }
        return s.toString();
    }

    public void init_loop() {
        super.init_loop();
        tpPosition = multipurposeWebcam.getTeamPropPosition();
        telemetry.addData("Detected Position", tpPosition);
        telemetry.addLine("Safe To Proceed");
        telemetry.addLine(makeLoadingString(5));
        telemetry.update();
        pixelQuickRelease.setState(MechanismState.CLOSED);
    }
    public void startBeforeWait() {
        roadrunnerDrivetrain.followTrajectorySequenceAsync(buildTrajectory(tpPosition));
        switch (tpPosition){
            case LEFT:
                aprilTagAlign.setTargetID(1);
                break;
            case CENTER:
                aprilTagAlign.setTargetID(2);
                break;
            case RIGHT:
                aprilTagAlign.setTargetID(3);
                break;
        }
        multipurposeWebcam.setActiveProcessor(ContourAndAprilTagWebcam.Processor.NONE);
    }

    @Override
    protected void update() {
        pixelQuickRelease.update();
        switch (autoState) {
            case DRIVE:
                roadrunnerDrivetrain.update();
                if (!roadrunnerDrivetrain.isBusy()) {
                    autoState = State.SCORE_BACKDROP;
                    multipurposeWebcam.setActiveProcessor(ContourAndAprilTagWebcam.Processor.APRIL_TAG);
                    multipurposeWebcam.setExposure(6);
                    multipurposeWebcam.setGain(50);

                    aprilTagAlign.setState(org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState.ON);
                    outtake.setLinearSlidesPosition(Outtake.LinearSlidesPosition.SECOND_ROW);
                    timer.reset();
                }
                telemetry.addData("PoseEstimate",roadrunnerDrivetrain.getPoseEstimate());
                break;
            case SCORE_BACKDROP:
                multipurposeWebcam.update();
                aprilTagAlign.update();
                roadrunnerDrivetrain.updatePoseEstimate();
                if (aprilTagAlign.isAligned() || timer.getTimeSeconds() > 3) {
                    outtake.setDropPosition();
                    robotDrivetrain.stop();
                    outtake.startDropSequence();
                    outtake.update();
                    autoState = State.SCORE_SCORE;
                }
                break;
            case SCORE_SCORE:
                telemetry.addLine("SCORE SCORE");
                roadrunnerDrivetrain.update();
                outtake.update();
                break;

        }
    }
    private TrajectorySequence buildTrajectory(ContourVisionProcessor.TeamPropState detection) {
        roadrunnerDrivetrain.setPoseEstimate(new Pose2d(36, -61.0, -Math.PI/2));
        TrajectorySequenceBuilder bob = roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate());

        //DETECT CENTER
        switch (detection) {
            case CENTER:
                return bob.setReversed(true)
                    .lineToLinearHeading(new Pose2d(39, -16, 3 * Math.PI / 4))
                    .lineToLinearHeading(new Pose2d(35,-15,Math.toRadians(100)))
                    .addTemporalMarker(() -> {pixelQuickRelease.setState(MechanismState.OPEN);})
                    .waitSeconds(1)
                    .lineToLinearHeading(new Pose2d(30, -12, Math.PI))
                    .setReversed(false)
                    .splineTo(new Vector2d(-30, -12), Math.PI)
                    .turn(Math.PI)
                    .lineToLinearHeading(new Pose2d(-39, -34,0))
                    .build();
                //break;
            case LEFT:
                return bob.setReversed(true)
                    .lineToLinearHeading(new Pose2d(36, -31, 0))
                    .addTemporalMarker(() -> {pixelQuickRelease.setState(MechanismState.OPEN);})
                    .waitSeconds(1)
                    .forward(5)
                    .strafeTo(new Vector2d(36,-13))
                        .setReversed(false)
                    .splineTo(new Vector2d(-38,-13),Math.PI)
                        .setReversed(true)
                    .strafeRight(28)
                    .build();
            case RIGHT:
                return bob.setReversed(true)
                    .lineToLinearHeading(new Pose2d(36, -32, Math.PI))
                    .addTemporalMarker(() -> {pixelQuickRelease.setState(MechanismState.OPEN);})
                    .waitSeconds(1)
                        .forward(5)
                    // ENDING
                    .strafeTo(new Vector2d(36, -12))
                    .setReversed(false)
                    .splineTo(new Vector2d(-38, -12), Math.PI)
                    .turn(Math.PI)
                    .setReversed(true)
                    .strafeRight(28)
                    .build();

        }
        return null;

    }

}
