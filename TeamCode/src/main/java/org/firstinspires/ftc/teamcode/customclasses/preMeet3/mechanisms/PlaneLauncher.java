package org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

public class PlaneLauncher extends MechanismBase implements Mechanism{
    private Servo planeLauncher;
    private int safetyFrames = 1;
    private static final int SAFETY_FRAMES = (300);

    public static final double LAUNCHER_CLOSED_POSITION = 0.6;
    public static final double LAUNCHER_OPEN_POSITION = 0.225;
    private CustomGamepad gamepad;
    public PlaneLauncher(HardwareMap hardwareMap, CustomGamepad gamepad) {
        this.gamepad = gamepad;
        planeLauncher = getHardware(Servo.class,"PlaneLauncher",hardwareMap);
    }
    @Override
    public void update(Telemetry telemetry) {
        update();
        telemetry.addData("COUNT DOWN: ", safetyFrames);
    }
    @Override
    public void update() {
        if (gamepad.gamepad.back) {
            safetyFrames--;
        } else {
            safetyFrames = SAFETY_FRAMES;
        }
        if (safetyFrames <= 0) {
            planeLauncher.setPosition(LAUNCHER_OPEN_POSITION);
        } else {
            planeLauncher.setPosition(LAUNCHER_CLOSED_POSITION);
        }
    }
}
