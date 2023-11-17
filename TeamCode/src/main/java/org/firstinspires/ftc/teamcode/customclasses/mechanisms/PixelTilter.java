package org.firstinspires.ftc.teamcode.customclasses.mechanisms;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;

public class PixelTilter extends MechanismBase implements Mechanism {
    private Servo frontServo;
    private Servo roofServo;
    private Servo angleServo;
    private MechanismState state = MechanismState.IDLE;

    public PixelTilter(HardwareMap hardwareMap)
    {
        frontServo = hardwareMap.get(Servo.class,"PixelTilterDropper");
        roofServo = hardwareMap.get(Servo.class,"PixelTilterRoof");
        angleServo = hardwareMap.get(Servo.class,"PixelTilterAngle");
    }

    //ALL OF THE FOLLOWING NUMBERS NEED CAREFUL TESTING TO MAKE SURE THEY ARE RIGHT, I LITERALLY JUST GUESSED THE FOR NOW - CAI
    public void update()
    {
        switch(state)
        {
            case READY_TO_RECEIVE_PIXELS:
                angleServo.setPosition(0);
                roofServo.setPosition(0);
                frontServo.setPosition(0);
                break;

            case STORING:
                angleServo.setPosition(.7);
                roofServo.setPosition(.2);
                frontServo.setPosition(1);
                break;

            case SCORING:
                angleServo.setPosition(.7);
                roofServo.setPosition(.2);
                frontServo.setPosition(0);
                break;

            case IDLE:
                //WAITING FOR NEXT STATE
                break;

            default:
                state = MechanismState.IDLE;
        }
    }

    public void update(Telemetry telemetry)
    {
        this.update();
        telemetry.addData("PixelTilter State", state.toString());
    }

    public void setState(MechanismState state){
        this.state = state;
    }
}
