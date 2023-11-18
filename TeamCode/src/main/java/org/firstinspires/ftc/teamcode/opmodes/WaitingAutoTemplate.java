package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;


@Autonomous(name="RoadRunnerRectangleyo")
public class WaitingAutoTemplate extends CustomOpMode {
    CustomGamepad gamepadOne;

    private final Clock timer = new Clock();
    private double time_to_start = 0.0;
    private boolean waiting = true;
    public void init() {
        super.init();
        gamepadOne = new CustomGamepad(this,1);
    }

    public void initLoop() {
        gamepadOne.update();

        if (gamepadOne.yDown) {
            time_to_start = 0.0;
        }
        time_to_start += gamepad1.left_stick_y * 0.001;
        telemetry.addData("Time To Start: ",time_to_start);
        telemetry.addLine("Left Joystick to control");
        telemetry.addLine("Y to Reset to 0");
    }

    protected boolean handleState(RobotState state) {
        return true;
    }

    public void start() {
        timer.reset();
    }

    protected void onMainLoop() {
        if (waiting){
            waiting = timer.getTime() < time_to_start;
            return;
        }
        drive.update();

        telemetry.addLine("MainRunning");
        telemetry.update();
        //Update any other mechanisms
    }

    protected void onNextLoop() {
        drive.update();
        robotState = RobotState.MAIN;
    }

    protected void onStopLoop() {
        super.onStopLoop();
        robotState = RobotState.IDLE;
    }

    protected void onIdleLoop() {

    }
}
