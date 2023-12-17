package org.firstinspires.ftc.teamcode.customclasses.preMeet3;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;


public abstract class CustomOpMode extends OpMode {
    // we are just using protected because it we don't want it to be accessed by EVERYTHING just its subclasses and friendly files
    protected RobotState robotState = RobotState.MAIN;
    protected Robot robot;
    protected SampleMecanumDrive drive;
    protected Clock timer = new Clock();
    protected final boolean DEBUG = true;


    protected enum RobotState {
        MAIN,
        NEXT,
        STOP,
        IDLE,
        TAG_ALIGN,
        DRIVE_TO_TEAM_PROP,
        DETECT_TEAM_PROP,
        MID,
        FINAL
    }
    public void init() {
        robot = new Robot(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
    }

    public final void init_loop() {

        initLoop();
        telemetry.update();
    }
    protected abstract void initLoop();
    protected abstract void onMainLoop();
    protected abstract void onNextLoop();
    protected abstract void onIdleLoop();
    protected void onStopLoop() {
        robot.setAllMotorsPower(0.0);
    }
    protected abstract boolean handleState(RobotState state);
    public abstract void start();
    // Should return true if state was handled properly else return false;
    private boolean handleStateInternal() {
        PoseStorage.currentPose = drive.getPoseEstimate();
        switch (robotState) {
            case MAIN:
                onMainLoop();
                return true;
            case NEXT:
                onNextLoop();
                return true;
            case STOP:
                onStopLoop();
                return true;
            case IDLE:
                onIdleLoop();
                return true;
            default:
                return handleState(robotState);
        }
    }

    public final void loop() {
        if (!handleStateInternal()) {
            telemetry.setMsTransmissionInterval(5);
            telemetry.addLine("autoState became an unknown value that was not handled in the loop!");
            telemetry.update();
            terminateOpModeNow();
        }
        if (DEBUG) telemetry.addData("Current State:",robotState);
        telemetry.update();
    }

    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

}
