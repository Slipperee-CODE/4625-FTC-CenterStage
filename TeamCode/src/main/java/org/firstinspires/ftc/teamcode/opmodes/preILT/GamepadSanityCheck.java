package org.firstinspires.ftc.teamcode.opmodes.preILT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
@TeleOp(name = "Gamepad Test")
public class GamepadSanityCheck extends OpMode {
    protected CustomGamepad cGamepad1;
    protected CustomGamepad cGamepad2;
    private int gIndex;
    @Override
    public void init() {
        gIndex = 0;
        cGamepad1 = new CustomGamepad(gamepad1);
        cGamepad2 = new CustomGamepad(gamepad2);
    }
    @Override
    public void loop() {
        cGamepad1.update();
        cGamepad2.update();
        switch (gIndex) {
            case 1:
                telemetry.addLine("GAMEPAD 1 INFO:");
                telemetry.addLine("X: " + cGamepad1.x + "\tY: " + cGamepad1.y);
                telemetry.addLine("A: " + cGamepad1.a + "\tB: " + cGamepad1.b);
                telemetry.addLine("Left Stick:");
                telemetry.addLine("\tX: " + cGamepad1.left_stick_x);
                telemetry.addLine("\tY: " + cGamepad1.left_stick_y);
                telemetry.addLine("Right Stick:");
                telemetry.addLine("\tX: " + cGamepad1.right_stick_x);
                telemetry.addLine("\tY: " + cGamepad1.right_stick_y);
                telemetry.addLine();
                break;
            case 2:
                telemetry.addLine("GAMEPAD 2 INFO:");
                telemetry.addLine("X: " + cGamepad2.x + "\tY: " + cGamepad2.y);
                telemetry.addLine("A: " + cGamepad2.a + "\tB: " + cGamepad2.b);
                telemetry.addLine("Left Stick:");
                telemetry.addLine("\tX: " + cGamepad2.left_stick_x);
                telemetry.addLine("\tY: " + cGamepad2.left_stick_y);
                telemetry.addLine("Right Stick:");
                telemetry.addLine("\tX: " + cGamepad2.right_stick_x);
                telemetry.addLine("\tY: " + cGamepad2.right_stick_y);
                telemetry.addLine();
                break;
        }

        telemetry.addLine("Use a gamepad to start displaying its values!");
        if (!cGamepad1.gamepad.atRest()) {
            gIndex = 1;
        } else if (!cGamepad2.gamepad.atRest()) {
            gIndex = 2;
        }
        telemetry.update(); // we call this in the end as opposed to the beginning because it holds the telemetry.update call...
    }

}
