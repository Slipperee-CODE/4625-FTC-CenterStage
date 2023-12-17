package org.firstinspires.ftc.teamcode.legacy.offseason;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.legacy.unused.TestPIDMechanism;

@TeleOp(name="TestingPIDMechanism", group="Iterative Opmode")

public class TestingPIDMechanism extends OpMode
{
    private TestPIDMechanism testPIDMechanism = null;

    private CustomGamepad customGamepad1;

    @Override
    public void init()
    {
        try {
            testPIDMechanism = new TestPIDMechanism(hardwareMap);
        } catch (Exception e) {
            e.printStackTrace();
        }
        customGamepad1 = new CustomGamepad(this, 1);
        MissingHardware.printMissing(telemetry);
    }

    @Override
    public void loop()
    {
        customGamepad1.update();
        if (customGamepad1.upDown)
        {
            testPIDMechanism.motorState = TestPIDMechanism.MotorState.EXTENDED;
        }

        if (customGamepad1.downDown)
        {
            testPIDMechanism.motorState = TestPIDMechanism.MotorState.RETRACTED;
        }
        if (customGamepad1.dpad_up) {
            telemetry.addLine("UP IS BEING PRESSED");
        }
        testPIDMechanism.Update(telemetry);
        telemetry.addData("Encoder Value", testPIDMechanism.motor.getPos());
        telemetry.addData("Attempting State", testPIDMechanism.motorState);
        telemetry.update();
    }
}
