package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

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
}
