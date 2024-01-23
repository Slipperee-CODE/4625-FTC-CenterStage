package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;

import java.util.ArrayList;

public class TestTwoServoMechanism extends MechanismBase implements Mechanism {
    //Simple mechanism so that I can test roadrunner and the temporal markers

    public Servo servo1;
    public Servo servo2;
    private ServoTuner servoTuner;

    public static final int SERVO1_OPEN = 0;
    public static final int SERVO1_CLOSED = 1;
    public static final int SERVO2_OPEN = 0;
    public static final int SERVO2_CLOSED = 1;


    public TestTwoServoMechanism(HardwareMap hwMap){
        servo1 = hwMap.get(Servo.class,"servo1");
        servo2 = hwMap.get(Servo.class,"servo2");

        ArrayList<Servo> servoList = new ArrayList<Servo>();
        servoList.add(servo1);
        servoList.add(servo2);

        setState(MechanismState.OPEN);
    }

    public TestTwoServoMechanism(HardwareMap hwMap, CustomGamepad customGamepad){

        servo1 = hwMap.get(Servo.class,"servo1");
        servo2 = hwMap.get(Servo.class,"servo2");
        this.gamepad = gamepad;

        ArrayList<Servo> servoList = new ArrayList<Servo>();
        servoList.add(servo1);
        servoList.add(servo2);

        setState(MechanismState.OPEN);
        servoTuner = new ServoTuner(servoList, gamepad);
    }


    @Override
    public void update() {

    }

    @Override
    public void update(Telemetry telemetry){
        this.update();

        telemetry.addData("TestTwoServoMechanism State",state);
    }

    @Override
    public void setState(MechanismState state){
        this.state = state;

        switch (state){
            case OPEN:
                servo1.setPosition(SERVO1_OPEN);
                servo2.setPosition(SERVO2_OPEN);
                break;

            case CLOSED:
                servo1.setPosition(SERVO1_CLOSED);
                servo2.setPosition(SERVO2_CLOSED);
                break;
        }
    }

    @Override
    public void mechanismTuningUpdate(Telemetry telemetry){
        servoTuner.update(telemetry);
    }
}
