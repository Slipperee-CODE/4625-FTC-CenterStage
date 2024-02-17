package org.firstinspires.ftc.teamcode.opmodes.ILT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.BlueContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testing.RedContourVisionProcessor;
import org.firstinspires.ftc.teamcode.opmodes.ILT.testingOpmodes.BlueContourVisionPortalWebcam;
import org.firstinspires.ftc.teamcode.opmodes.preILT.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
/**
 * This version uses Cai's Camera Conour Magic in order to build
 * the trajectory initially and not have to create it on the fly
 */
@Autonomous(name = "(Deprecated) Blue Close Auto")
public class BlueCloseNew extends WaitingAuto {
    public enum State {
        INITIAL,

        FINAL,
        LEFT,
        RIGHT,
        CENTER
    }
    TeamPropDetection teamPropDetection;
    BlueContourVisionPortalWebcam blueContourVisionPortalWebcam;


    State autoState = State.INITIAL;
    public void init() {
        super.init();
        telemetry.addLine("Not Ready Yet!!");
        telemetry.update();
        teamPropDetection = new TeamPropDetection(hardwareMap);
        blueContourVisionPortalWebcam = new BlueContourVisionPortalWebcam(hardwareMap);
        //roadrunnerDrivetrain.followTrajectorySequenceAsync(buildInitialTrajectories());
    }
    private String makeLoadingString(int maxDots) {
        StringBuilder s = new StringBuilder(maxDots);
        int dots = (int) timer.getTimeSeconds() % maxDots;
        for (int i = 0 ; i < dots; i++) {
            s.append(".");
        }
        return s.toString();
    }
    public void init_loop() {
        super.init_loop();
        BlueContourVisionProcessor.TeamPropState tpPosition = blueContourVisionPortalWebcam.GetTeamPropState();
        telemetry.addData("Detected Position", tpPosition);
        telemetry.addLine("Safe To Proceed");
        telemetry.addLine(makeLoadingString(5));
        telemetry.update();
    }

    @Override
    protected void update() {
        roadrunnerDrivetrain.update();
    }

    private TrajectorySequence buildCenterTrajectory() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(new Pose2d(-12, -61.0, Math.PI/2))
                .forward(26)
                .waitSeconds(0.1) // Magic
                //IF WE DETECT:
                // Center
                .splineTo(new Vector2d(-24,-24),Math.PI)

                .addTemporalMarker(() -> robotMechanisms.pixelQuickRelease.setDrop())
                .waitSeconds(0.1)// Dumpy
                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .build();
    }
    private TrajectorySequence buildLeftTrajectory() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(new Pose2d(-12, -61.0, Math.PI/2))
                // BLUE CLOSE SIDE
                .forward(26)
                .waitSeconds(0.1) //DETECTY

                //IF WE DETECT:
                //Left
                .splineTo(new Vector2d(-35,-34),Math.PI)
                .addTemporalMarker(() -> robotMechanisms.pixelQuickRelease.setDrop())
                .waitSeconds(0.1)// Dumpy
                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .build();
    }
    private TrajectorySequence buildRightTrajectory() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(new Pose2d(-12, -61.0, Math.PI/2))
                // BLUE CLOSE SIDE
                .forward(26)
                .waitSeconds(1) //DETECTY

                //IF WE DETECT:
                //Right
                .turn(Math.PI/2)
                .back(4)
                .waitSeconds(1)// Dumpy

                .forward(4)
                .splineTo(new Vector2d(-35,-34),Math.PI)

                // ENDING
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .build();

    }
    private TrajectorySequence buildFinalTrajectories() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .build();
    }
}
