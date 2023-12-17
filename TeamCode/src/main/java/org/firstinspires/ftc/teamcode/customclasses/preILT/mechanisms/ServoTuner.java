package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.CustomGamepad;

import java.util.ArrayList;

public class ServoTuner extends MechanismBase implements Mechanism{
    //I don't know enough about keywords to make sure I'm putting the right ones here
    private ArrayList<Servo> servoList;
    private int servoIndex;
    private Servo currentServo;

    public ServoTuner(ArrayList<Servo> servoList, CustomGamepad gamepad){
        this.servoList = servoList;
        this.gamepad = gamepad;
    }

    @Override
    public void update() {
        if (gamepad.leftDown && servoIndex > 0){
            servoIndex--;
            currentServo = servoList.get(servoIndex);
        } else if (gamepad.rightDown && servoIndex < servoList.size() - 1) {
            servoIndex++;
            currentServo = servoList.get(servoIndex);
        }

        if (gamepad.left_stick_y != 0){
            currentServo.setPosition(currentServo.getPosition() + gamepad.left_stick_y/10 ); //Should give us a decent range of movement speed to test
        } else if (gamepad.right_stick_y != 0) {
            currentServo.setPosition(currentServo.getPosition() + gamepad.right_stick_y/100 ); //No reason not to make the right stick more precise
        }
    }

    @Override
    public void update(Telemetry telemetry) {
        this.update();

        telemetry.addData("Current Servo Name (Not the Configured One)", currentServo.getDeviceName());
        telemetry.addLine("" + currentServo.getController() + "" + currentServo.getPortNumber());
        telemetry.addData("Direction", currentServo.getDirection());
        telemetry.addData("THE IMPORTANT ONE --> Servo Position", currentServo.getPosition());
    }
}
