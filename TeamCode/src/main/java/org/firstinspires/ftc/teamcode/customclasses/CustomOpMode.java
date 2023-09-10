package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;

public abstract class CustomOpMode extends OpMode {
    // we are just using protected because it we don't want it to be accessed by EVERYTHING just its subclasses and friendly files
    protected RobotState autonomousState = RobotState.MAIN;
    protected Robot robot;
    protected SampleMecanumDrive drive;

    protected enum RobotState {
        MAIN,
        NEXT,
        STOP,
        IDLE,
    }
    public void init() {
        robot = new Robot(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
    }
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
        switch (autonomousState) {
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
                return handleState(autonomousState);
        }
    }

    public final void loop() {
        if (!handleStateInternal()) {
            telemetry.setMsTransmissionInterval(0);
            telemetry.addLine("autoState became an unknown value that was not handled in the loop!");
            telemetry.update();
            terminateOpModeNow();
        }
        telemetry.update();
    }
}
