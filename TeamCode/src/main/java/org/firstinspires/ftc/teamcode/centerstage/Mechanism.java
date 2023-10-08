package org.firstinspires.ftc.teamcode.centerstage;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public interface Mechanism {

    MechanismState state = MechanismState.IDLE;
    void Update(Telemetry telemetry);
    void Update();
}
