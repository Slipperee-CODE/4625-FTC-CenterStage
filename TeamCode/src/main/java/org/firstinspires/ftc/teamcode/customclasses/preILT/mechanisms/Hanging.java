package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

public class Hanging extends MechanismBase {
    private DcMotor motor;
    private final double powerConstant = 0.5f;

    private MechanismState state;

    public Hanging(HardwareMap hardwareMap, CustomGamepad gamepad)
    {
        motor = getHardware(DcMotor.class,"hanging",hardwareMap);
        this.gamepad = gamepad;
    }

    public void update()
    {
        if (gamepad.gamepad.right_trigger > 0) {
            motor.setPower(powerConstant);
        } else if (gamepad.gamepad.left_trigger > 0) {
            motor.setPower(-powerConstant);
        } else if (gamepad.a){
            motor.setPower(-1);
        } else {
            motor.setPower(0);
        }
    }

    public void update(Telemetry telemetry)
    {
        update();
        telemetry.addLine("Hanging Active");
    }

    public void setState(MechanismState state){
        this.state = state;
    }
}
