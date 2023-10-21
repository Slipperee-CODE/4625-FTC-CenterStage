package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.ActiveIntake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.IntakeAngler;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.LeosAprilTagFun;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.webcam.Webcam;

@TeleOp(name="Meet0Teleop")
public class Meet0Teleop extends CustomOpMode
{
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;

    Mechanism activeIntake;
    Mechanism intakeAngler;

    public void init(){
        super.init();
        robot.SetSpeedConstant(0.75);
        gamepad1 = new CustomGamepad(this,1);
        gamepad2 = new CustomGamepad(this, 2);
        activeIntake = new ActiveIntake(hardwareMap, gamepad2);
        intakeAngler = new IntakeAngler(hardwareMap);
        MissingHardware.printMissing(telemetry);
    }

    public void start() {

    }

    protected void initLoop() {}

    protected void onMainLoop() {
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

        intakeAngler.update();


        telemetry.update();
    }

    protected void onNextLoop() {}

    protected void onIdleLoop() {}

    protected boolean handleState(RobotState state) { return true; }
}
