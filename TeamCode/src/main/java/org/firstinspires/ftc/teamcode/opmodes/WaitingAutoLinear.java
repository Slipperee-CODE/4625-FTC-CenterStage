package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.Robot;

public abstract class WaitingAutoLinear extends LinearOpMode {
    private final CustomGamepad gamepadOne = new CustomGamepad(gamepad1);
    public Robot robot;
    private final Clock __delayTimer = new Clock(); // we name is something weird so that the subclasses never have issues with creating some other variable called timer or smth
    private double time_to_start = 0.0;
    private boolean waiting = true;
    protected long sleepiness = 10;
    @Override
    public void runOpMode() throws InterruptedException {
        robot = new Robot(hardwareMap);
        initialize();
        while (!isStarted()) {
            gamepadOne.update();
            if (gamepadOne.yDown) time_to_start = 0;
            time_to_start += gamepad1.left_stick_y * 0.001;
            telemetry.addData("Time To Start: ", time_to_start);
            telemetry.addLine("Left Joystick to control");
            telemetry.addLine("Y to Reset to 0");
        }

        while (__delayTimer.getTimeSeconds() < time_to_start) {
            sleep(sleepiness);
        }
        run();
    }
    public abstract void run();
    public abstract void initialize();



}
