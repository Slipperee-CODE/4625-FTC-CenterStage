package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.ActiveIntake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.IntakeAngler;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.LeosAprilTagFun;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.PlaneLauncher;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;

@TeleOp(name="Meet3Teleop")
public class Meet3Teleop extends CustomOpMode
{
    public static final double DPAD_SPEED = 0.25;
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;
    Mechanism activeIntake;
    Mechanism intakeAngler;
    Mechanism outtake;
    DcMotor goUp;
    Servo hooker;
    //LeosAprilTagFun tagAlign;
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
        //Webcam webcam = new Webcam(hardwareMap,telemetry,false);
        goUp = hardwareMap.get(DcMotor.class,"LinearActuator");
        hooker =  hardwareMap.get(Servo.class,"Hooker");
        if (hooker != null) {
            hooker.setPosition(0);
        }

        //tagAlign = new LeosAprilTagFun(telemetry,hardwareMap,robot,webcam,false);
        //tagAlign.init();
        MissingHardware.printMissing(telemetry);
        sleep(1000);
    }

    public void start() {

    }

    protected void onMainLoop() {
        gamepad1.update();
        gamepad2.update();
        if (gamepad2.bToggle) {
            Outtake.DROP_PIXEL_MAX_POSITION = 5_000;
            telemetry.addLine("Linear Slides Safey: OFF!!");
        } else {
            Outtake.DROP_PIXEL_MAX_POSITION = 2_500;
        }
        if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
            double horizontal = 0;
            double vert= 0;
            if (gamepad1.dpad_left) horizontal -= DPAD_SPEED;
            if (gamepad1.dpad_right) horizontal += DPAD_SPEED;
            if (gamepad1.dpad_up) vert += DPAD_SPEED;
            if (gamepad1.dpad_down) vert -= DPAD_SPEED;
            robot.emulateController(vert,horizontal,0);
        } else {
            robot.emulateController(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        }
        activeIntake.update();
        intakeAngler.update();
        outtake.update(telemetry);
       // tagAlign.update();
        planeLauncher.update(telemetry);
        goUp.setPower(gamepad1.gamepad.right_trigger - gamepad1.gamepad.left_trigger);
        if (hooker != null)
            if (gamepad1.yToggle) {
                hooker.setPosition(0.5);
            } else {
                hooker.setPosition(0);
        }
        if (gamepad1.xDown) {
            //tagAlign.setState(gamepad1.xToggle ? MechanismState.ON : MechanismState.OFF );
        }
    }

    protected void initLoop() {}

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }
}
