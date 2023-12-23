package org.firstinspires.ftc.teamcode.opmodes.preILT;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.TestTwoServoMechanism;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

@TeleOp(name="MechanismServoTuningTeleop")
public class MechanismServoTuningTeleop extends CustomOpMode {

    private TestTwoServoMechanism testTwoServoMechanism;
    private CustomGamepad gamepad1;

    @Override
    public void init(){
        super.init();
        gamepad1 = new CustomGamepad(this,1);
        testTwoServoMechanism = new TestTwoServoMechanism(hardwareMap, gamepad1);
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
    public void mainLoop(){
        gamepad1.update();
        testTwoServoMechanism.update();
        super.loop();
    }

    @Override
    public void stop(){

        super.stop();
    }
}
