package org.firstinspires.ftc.teamcode.opmodes.preILT;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.TestTwoServoMechanism;

@TeleOp(name="MechanismServoTuningTeleop")
public class MechanismServoTuningTeleop extends CustomOpMode {

    private TestTwoServoMechanism testTwoServoMechanism;

    @Override
    public void init(){
        super.init();
        testTwoServoMechanism = new TestTwoServoMechanism(hardwareMap);
    }

    @Override
    public void init_loop(){

        super.init_loop();
    }

    @Override
    public void start(){
        testTwoServoMechanism.mechanismTuningUpdate(telemetry);
    }

    @Override
    public void loop(){

        super.loop();
    }

    @Override
    public void stop(){

        super.stop();
    }
}
