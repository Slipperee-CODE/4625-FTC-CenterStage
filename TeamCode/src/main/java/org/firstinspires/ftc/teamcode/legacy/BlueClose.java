package org.firstinspires.ftc.teamcode.legacy;

import static org.firstinspires.ftc.teamcode.customclasses.preILT.Clock.sleep;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.preILT.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Disabled
@Autonomous(name = "(Deprecated2) Blue Close Auto")
public class BlueClose extends WaitingAuto {
    public enum State {
        INITIAL,
        DETECTION,
        FINAL,
        LEFT,
        RIGHT,
        CENTER
    }
    TeamPropDetection teamPropDetection;
    State autoState = State.INITIAL;
    public void init() {
        super.init();
        teamPropDetection = new TeamPropDetection(hardwareMap);
        roadrunnerDrivetrain.followTrajectorySequenceAsync(buildInitialTrajectories());
    }
    private int framesToDetect = 20;
    @Override
    protected void update() {
        roadrunnerDrivetrain.update();
        switch (autoState) {
            case INITIAL:
                if (!roadrunnerDrivetrain.isBusy()) {
                    autoState = State.DETECTION;
                }
                break;
            case DETECTION:
                framesToDetect --;
                teamPropDetection.update(telemetry);
                telemetry.addData("Frames Left:",framesToDetect);
                if (framesToDetect == 0){
                    roadrunnerDrivetrain.followTrajectorySequenceAsync(buildCenterTrajectories());
                    autoState = State.CENTER;
                }
                break;
            case CENTER:
                if (!roadrunnerDrivetrain.isBusy()) {
                    roadrunnerDrivetrain.followTrajectorySequenceAsync(buildFinalTrajectories());
                    autoState = State.FINAL;
                }
                break;
            case FINAL:
                if (!roadrunnerDrivetrain.isBusy()) {
                    telemetry.addLine("FINISHED!!!");
                }
                robotDrivetrain.setAllMotorPowers(0);
                break;
        }
    }
    private TrajectorySequence buildInitialTrajectories() {
        roadrunnerDrivetrain.setPoseEstimate(new Pose2d(-12, -61.0, Math.PI/2));
        return roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                // BLUE CLOSE SIDE
                .forward(26)
                .waitSeconds(1)
                .build();
    }
    private TrajectorySequence buildLeftTrajectories() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                .build();
    }
    private TrajectorySequence buildCenterTrajectories() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                .splineTo(new Vector2d(-24,-24),Math.PI)
                .build();

    }
    private TrajectorySequence buildFinalTrajectories() {
        return roadrunnerDrivetrain.trajectorySequenceBuilder(roadrunnerDrivetrain.getPoseEstimate())
                .splineTo(new Vector2d(-44,-34),Math.PI)
                .build();
    }
}
