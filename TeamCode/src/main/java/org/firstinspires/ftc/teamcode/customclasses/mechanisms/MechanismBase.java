package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;

public abstract class MechanismBase implements Mechanism {
    public MechanismState state;
    CustomGamepad gamepad;

    public abstract void update();

    public void update(Telemetry telemetry){
        update();
    }

    public void setState(MechanismState state) {
        this.state = state;
    }

    protected <T> T getHardware(Class<? extends T> classOrInterface, String deviceName, HardwareMap hardwareMap) {

        T hw = hardwareMap.tryGet(classOrInterface, deviceName.trim());
        if (hw == null) {
            MissingHardware.addMissingHardware(deviceName);
        }
        return hw;

    }
}
