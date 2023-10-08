package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public abstract class MechanismBase implements Mechanism {
    public MechanismState state;
    CustomGamepad gamepad;

    public void update(){

    }

    public void update(Telemetry telemetry){
        update();
    }
}
