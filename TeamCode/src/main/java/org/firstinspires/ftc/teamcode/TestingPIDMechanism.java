package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.MissingHardware;
import org.firstinspires.ftc.teamcode.customclasses.TestPIDMechanism;

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
        customGamepad1.Update();
        if (customGamepad1.upDown)
        {
            testPIDMechanism.motorState = TestPIDMechanism.MotorState.EXTENDED;
        }

        if (customGamepad1.downDown)
        {
            testPIDMechanism.motorState = TestPIDMechanism.MotorState.RETRACTED;
        }
        if (customGamepad1.up) {
            telemetry.addLine("UP IS BEING PRESSED");
        }
        testPIDMechanism.Update(telemetry);
        telemetry.addData("Encoder Value", testPIDMechanism.motor.getPos());
        telemetry.addData("Attempting State", testPIDMechanism.motorState);
        telemetry.update();
    }
}
