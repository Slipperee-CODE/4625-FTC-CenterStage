package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Mechanism {

    MechanismState state = MechanismState.IDLE;
    void update(Telemetry telemetry);
    void update();
    void setState(MechanismState state);
}
