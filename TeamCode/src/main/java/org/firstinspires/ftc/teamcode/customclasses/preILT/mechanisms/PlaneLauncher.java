package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.MechanismBase;

public class PlaneLauncher extends MechanismBase implements Mechanism {
    private Servo planeLauncher;
    private int safetyFrames = 1;
    private static final int SAFETY_FRAMES = 100;

    public static final double LAUNCHER_CLOSED_POSITION = 0.6; //TO BE TUNED
    public static final double LAUNCHER_OPEN_POSITION = 0.225; //TO BE TUNED
    private CustomGamepad gamepad;
    public PlaneLauncher(HardwareMap hardwareMap, CustomGamepad gamepad) {
        this.gamepad = gamepad;
        planeLauncher = getHardware(Servo.class,"planeLauncher",hardwareMap);
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
