package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

public abstract class MechanismBase implements Mechanism {
    //Note: We are gonna need to be careful about importing the correct stuff because the file names are the same
    //Left out your missing hardware handling code because I have no clue how it works so I will wait till I have your help to
    //implement it
    //All overrides are of the Mechanism interface
    public MechanismState state;
    CustomGamepad gamepad;

    @Override
    public abstract void update();

    @Override
    public void update(Telemetry telemetry){
        update();
    }

    @Override
    public void setState(MechanismState state) {
        this.state = state;
    }

    @Override
    public void mechanismTuningUpdate(Telemetry telemetry) {}
}
