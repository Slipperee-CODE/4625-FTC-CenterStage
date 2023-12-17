package org.firstinspires.ftc.teamcode.opmodes.preMeet3.comp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.ActiveIntake;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.IntakeAngler;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.LeosAprilTagFun;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.PlaneLauncher;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;

@TeleOp(name="Meet2Teleop")
public class Meet2Teleop extends CustomOpMode
{
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;
    Mechanism activeIntake;
    Mechanism intakeAngler;
    Mechanism outtake;
    LeosAprilTagFun tagAlign;
    Mechanism planeLauncher;

    public void init(){
        super.init();
        robot.SetSpeedConstant(0.85);
        gamepad1 = new CustomGamepad(this,1);
        gamepad2 = new CustomGamepad(this, 2);
        activeIntake = new ActiveIntake(hardwareMap, gamepad2);
        intakeAngler = new IntakeAngler(hardwareMap, gamepad2);
        outtake = new Outtake(hardwareMap, gamepad2);
        planeLauncher = new PlaneLauncher(hardwareMap,gamepad1);
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
        if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
            double horiztonal = 0;
            double vert= 0;
            if (gamepad1.dpad_left) {
                horiztonal -= 0.25;
            } if (gamepad1.dpad_right) {
                horiztonal += .25;
            }
            if (gamepad1.dpad_up) {
                vert += 0.25;
            }
            if (gamepad1.dpad_down) {
                vert -=0.25;
            }
            robot.emulateController(vert,horiztonal,0);
        } else {
            robot.emulateController(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
        activeIntake.update();
        intakeAngler.update();
        outtake.update(telemetry);
        tagAlign.update();
        planeLauncher.update(telemetry);
        if (gamepad1.xDown) {
            tagAlign.setState(gamepad1.xToggle ? MechanismState.ON : MechanismState.OFF );
        }
    }

    protected void initLoop() {}

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }
}
