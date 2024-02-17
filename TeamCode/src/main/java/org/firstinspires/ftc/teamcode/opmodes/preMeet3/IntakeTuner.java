package org.firstinspires.ftc.teamcode.opmodes.preMeet3;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;
@Disabled
@TeleOp(name = "Intake Tuner")
public class IntakeTuner extends CustomOpMode {
    public Servo intakeAngler = null;
    public void init() {
        super.init();
        intakeAngler = hardwareMap.get(Servo.class, "IntakeAngler");

    }
    @Override
    protected void initLoop() {

    }

    @Override
    protected void onMainLoop() {
        intakeAngler.setPosition(intakeAngler.getPosition() +( gamepad1.right_trigger - gamepad1.left_trigger) * 0.001);
        telemetry.addData("IntakeAngler: ",intakeAngler.getPosition());
    }

    @Override
    protected void onNextLoop() {

    }

    @Override
    protected void onIdleLoop() {

    }

    @Override
    protected boolean handleState(RobotState state) {
        return false;
    }

    @Override
    public void start() {

    }
}
