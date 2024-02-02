package org.firstinspires.ftc.teamcode.customclasses.preILT;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.BinaryAngler;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.MechanismBase;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Outtake;

public class RobotMechanisms extends MechanismBase {
    private Outtake outtake;

    public void update() {}

    public static class PixelQuickRelease extends BinaryAngler {
        protected double getRECEIVE_POSITION() {return 0;}
        protected double getDROP_POSITION() {return 0;}
    }
    public PixelQuickRelease pixelQuickRelease;
    public RobotMechanisms(HardwareMap hardwareMap) {

        pixelQuickRelease = new PixelQuickRelease();
        pixelQuickRelease.servo = getHardware(Servo.class, "PixelQuickRelease",hardwareMap);

    }
}
