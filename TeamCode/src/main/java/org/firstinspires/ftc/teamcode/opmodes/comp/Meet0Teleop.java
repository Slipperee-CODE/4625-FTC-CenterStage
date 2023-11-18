package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.ActiveIntake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.AprilTagAlign;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.IntakeAngler;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.LeosAprilTagFun;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;

@TeleOp(name="Meet0Teleop")
@Disabled
public class Meet0Teleop extends CustomOpMode
{
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;

    Mechanism activeIntake;
    Mechanism intakeAngler;

    LeosAprilTagFun tagAlign;

    public void init(){
        super.init();
        robot.SetSpeedConstant(0.85);
        gamepad1 = new CustomGamepad(this,1);
        gamepad2 = new CustomGamepad(this, 2);
        activeIntake = new ActiveIntake(hardwareMap, gamepad2);
        intakeAngler = new IntakeAngler(hardwareMap);
        //Webcam webcam = new Webcam(hardwareMap);
        //tagAlign = new LeosAprilTagFun(telemetry,hardwareMap,robot,webcam,false);
        //tagAlign.init();
         if (MissingHardware.printMissing(telemetry))
            sleep(1000);
         else
             sleep(2000);
    }

    public void start() {

    }

    protected void onMainLoop() {}

    protected void initLoop() {
        gamepad1.update();
        gamepad2.update();
        robot.emulateController(gamepad1.left_stick_y,gamepad1.left_stick_x,gamepad1.right_stick_x);

        //MECHANISMS:

        //ACTIVE INTAKE
        if (gamepad2.upDown){
            activeIntake.setState(MechanismState.FORWARD);
        }
        else if (gamepad2.downDown){
            activeIntake.setState(MechanismState.REVERSE);
        }
        else if (gamepad2.leftDown){
            activeIntake.setState(MechanismState.OFF);
        }

        activeIntake.update();

        //INTAKE ANGLER
        if (gamepad2.yDown){
            intakeAngler.setState(MechanismState.HIGH);
        }
        else if (gamepad2.xDown){
            intakeAngler.setState(MechanismState.MID);
        }
        else if (gamepad2.aDown){
            intakeAngler.setState(MechanismState.LOW);
        }
        else {
            intakeAngler.setState(MechanismState.IDLE);
        }

        intakeAngler.update();

/*
        if (gamepad1.aDown) {
            if (gamepad1.aToggle) {
                tagAlign.setState(MechanismState.ON);
            } else {
                tagAlign.setState(MechanismState.OFF);
            }
        }
        telemetry.addData("State:", tagAlign.state.toString());

        tagAlign.update();*/
    }

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }
}
