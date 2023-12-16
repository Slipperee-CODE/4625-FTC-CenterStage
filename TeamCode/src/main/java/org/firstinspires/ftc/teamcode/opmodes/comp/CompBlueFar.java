package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.teamcode.customclasses.Clock;

import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAuto;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;
@Disabled
@Autonomous(name="BlueFar Meet1")

public class CompBlueFar extends WaitingAuto {
    //Roadrunner Stuff
    // Conventions to Follow : the back of the field is the side with the scoring boards, front is the other side with the big apriltags
    // Remember that the when centered to field and heading is 0 then the robot is facing the right because the heading 0 is to the right on a unit circle

    // Mechanisms || Webcams || Timers

    //private final PixelTiltOuttake pixelTiltOuttake = null;
    //private final LinearSlides linearSlides = null;
    //private Webcam webcam = null;

    //private LeosAprilTagFun tagAlign = null;
    // Miss
    private Clock timer;

    public void init() {
        super.init();
        telemetry.setMsTransmissionInterval(6);
        //pixelTiltOuttake = new PixelTiltOuttake(hardwareMap);
        //linearSlides = new LinearSlides(hardwareMap, telemetry);
        MissingHardware.printMissing(telemetry);
        sleep(1000);

    }
    public void startBeforeWait() {
        TrajectorySequence trajectoryToFollow = CreateDefaultTrajectories();
        drive.followTrajectorySequenceAsync(trajectoryToFollow);
    }



    @Override
    protected void update() {

    }

    protected void onNextLoop() {

    }

    protected void onStopLoop() {
        super.onStopLoop();
        robotState = RobotState.IDLE;
    }

    @Override
    protected boolean handleState(RobotState state) {
        switch (state) {
            case DRIVE_TO_TEAM_PROP:
                drive.followTrajectorySequenceAsync(
                    drive.trajectorySequenceBuilder(new Pose2d(-12, -61.0, Math.PI/2))
                            .forward(26)
                            .addTemporalMarker(() -> this.robotState = RobotState.DETECT_TEAM_PROP)
                            .build()
                );
                drive.update();
                return true;
            case DETECT_TEAM_PROP:
                return true;

        }
        return false;
    }

    protected void onIdleLoop() {    }

    private TrajectorySequence CreateDefaultTrajectories() {
        return CreateCenterTrajectories();
    }

    private TrajectorySequence CreateCenterTrajectories() {
        TrajectorySequence trajectory1 =
                drive.trajectorySequenceBuilder(new Pose2d(38.0, -61.0, Math.toRadians(-90)))
                        .back(20)
                        .splineTo(new Vector2d(38, -15),3 * Math.PI/4)
                        .splineTo(new Vector2d(0,-10),Math.PI)
                        .splineTo(new Vector2d(-32,-15),Math.toRadians(20+180))
                        .back(14)

                        .build();
        drive.setPoseEstimate(trajectory1.start());
        return trajectory1;
    }
}
