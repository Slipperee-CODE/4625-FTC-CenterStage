package org.firstinspires.ftc.teamcode.opmodes.ILT.testing;

import static org.firstinspires.ftc.teamcode.customclasses.preILT.Clock.sleep;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;


import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MissingHardware;

@TeleOp(name="JavaBotsScrimmagePushBot")
public class JavaBotsScrimmagePushBot extends CustomOpMode
{
    public static final double DPAD_SPEED = 0.25;
    CustomGamepad gamepad1;
    CustomGamepad gamepad2;


    public void init(){
        super.init();
        robotDrivetrain.setSpeedConstant(0.85);
        gamepad1 = new CustomGamepad(this,1);
        gamepad2 = new CustomGamepad(this, 2);
        MissingHardware.printMissing(telemetry);
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
            double vert= 0;
            if (gamepad1.dpad_left) horizontal -= DPAD_SPEED;
            if (gamepad1.dpad_right) horizontal += DPAD_SPEED;
            if (gamepad1.dpad_up) vert += DPAD_SPEED;
            if (gamepad1.dpad_down) vert -= DPAD_SPEED;
            robotDrivetrain.emulateController(vert,horizontal,0);
        } else {
            robotDrivetrain.emulateController(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x  * 0.5);
        }


    }

}
