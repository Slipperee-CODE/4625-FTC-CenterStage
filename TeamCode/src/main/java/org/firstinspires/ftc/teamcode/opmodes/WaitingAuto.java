package org.firstinspires.ftc.teamcode.opmodes;


import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;


public abstract class WaitingAuto extends CustomOpMode {
    CustomGamepad gamepadOne;

    private final Clock __delayTimer = new Clock(); // we name is something weird so that the subclasses never have issues with creating some other variable called timer or smth
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

    protected final boolean handleState(RobotState state) {
        return true;
    }

    public final void start() {
        __delayTimer.reset();
    }

    protected final void onMainLoop() {
        if (waiting){
            waiting = __delayTimer.getTime() < time_to_start;
            return;
        }
        update();
    }

    protected abstract void update();


}
