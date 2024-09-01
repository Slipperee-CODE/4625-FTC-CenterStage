package org.firstinspires.ftc.teamcode.opmodes.ILT;

import static org.firstinspires.ftc.teamcode.customclasses.preILT.Clock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.customclasses.preILT.ContourAndAprilTagWebcam;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Hanging;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Intake;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.PixelQuickRelease;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.PlaneLauncher;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.AprilTagAlign;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MissingHardware;

import java.lang.annotation.Target;

@TeleOp(name="ILTTeleop")
public class ILTTeleop extends CustomOpMode
{
    public static final double DPAD_SPEED = 0.5;
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;

    private AprilTagAlign tagAlign;

    private Outtake outtake;
    private Hanging hanging;
    private Intake intake;
    private PixelQuickRelease pixelQuickRelease;
    private PlaneLauncher planeLauncher;

    private ContourAndAprilTagWebcam multipurposeWebcam;

    public void init(){
        super.init();
        robotDrivetrain.setSpeedConstant(0.85);
        gamepad1 = new CustomGamepad(this,1);
        gamepad2 = new CustomGamepad(this, 2);
        tagAlign = new AprilTagAlign(hardwareMap,telemetry,gamepad1,robotDrivetrain);
        MissingHardware.printMissing(telemetry);

        multipurposeWebcam = new ContourAndAprilTagWebcam(hardwareMap);
        multipurposeWebcam.setActiveProcessor(ContourAndAprilTagWebcam.Processor.APRIL_TAG);
        multipurposeWebcam.setExposure(2); //was 6
        multipurposeWebcam.setGain(0); //was 50

        outtake = new Outtake(hardwareMap, gamepad2);
        hanging = new Hanging(hardwareMap, gamepad2);
        intake = new Intake(hardwareMap, gamepad2);
        pixelQuickRelease = new PixelQuickRelease(hardwareMap, gamepad2,true);
        //planeLauncher = new PlaneLauncher(hardwareMap, gamepad2);
    }

    public void start() {

    }

    public void mainLoop() {
        gamepad1.update();
        gamepad2.update();

        if (gamepad1.guideDown) {
            robotDrivetrain.switchDirection();
        }
        if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
            double horizontal = 0;
            double vert = 0;
            if (gamepad1.dpad_left) horizontal += DPAD_SPEED;
            if (gamepad1.dpad_right) horizontal -= DPAD_SPEED;
            if (gamepad1.dpad_up) vert -= DPAD_SPEED;
            if (gamepad1.dpad_down) vert += DPAD_SPEED;
            robotDrivetrain.emulateController(vert,horizontal,0);
        } else {
            robotDrivetrain.emulateController(gamepad1.left_stick_y, -gamepad1.left_stick_x, gamepad1.right_stick_x  * 0.5);
        }
        if (gamepad1.gamepad.right_trigger > 0.3 ) {
            telemetry.addLine("THIS HAPPENED");
            tagAlign.setState(MechanismState.NEAREST);
        } else {
            telemetry.addLine("THIS DID NOT HAPPEN");
            tagAlign.setState(MechanismState.OFF);
        }
        multipurposeWebcam.update();
        tagAlign.update();
        //HANDLE ALL MECHANISMS HERE:
        outtake.update(telemetry);
        hanging.update();
        intake.update(telemetry);
        pixelQuickRelease.update();
        //planeLauncher.update();
    }

}
