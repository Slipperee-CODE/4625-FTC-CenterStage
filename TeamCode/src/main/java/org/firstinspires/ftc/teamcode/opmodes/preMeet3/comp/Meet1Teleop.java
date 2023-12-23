package org.firstinspires.ftc.teamcode.opmodes.preMeet3.comp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.ActiveIntake;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.IntakeAngler;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.LeosAprilTagFun;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;
@Disabled
@TeleOp(name="Meet1Teleop")
public class Meet1Teleop extends CustomOpMode
{
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;

    Mechanism activeIntake;
    Mechanism intakeAngler;
    Mechanism linearSlides;

    LeosAprilTagFun tagAlign;

    public void init(){
        super.init();
        robot.SetSpeedConstant(0.85);
        gamepad1 = new CustomGamepad(this,1);
        gamepad2 = new CustomGamepad(this, 2);
        activeIntake = new ActiveIntake(hardwareMap, gamepad2);
        intakeAngler = new IntakeAngler(hardwareMap, gamepad2);
        linearSlides = new Outtake(hardwareMap, gamepad2);
        Webcam webcam = new Webcam(hardwareMap,telemetry,false);

        tagAlign = new LeosAprilTagFun(telemetry,hardwareMap,robot,webcam,false);
        tagAlign.init();
        MissingHardware.printMissing(telemetry);
        sleep(1000);
    }

    public void start() {

    }

    protected void onMainLoop() {
        gamepad1.update();
        gamepad2.update();
        robot.emulateController(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);
        activeIntake.update();
        intakeAngler.update();
        linearSlides.update(telemetry);
        tagAlign.update();
        if (gamepad1.xDown) {
            if (gamepad1.xToggle) {
                tagAlign.setState(MechanismState.ON);
            } else {
                tagAlign.setState(MechanismState.OFF);
            }
        }
    }

    protected void initLoop() {

    }

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }
}
