package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import com.qualcomm.robotcore.hardware.Servo;

public abstract class BinaryAngler {
    // mainly used in the "Outtake" class mechanism to reduce the amount of variable constants
    protected double RECEIVE_POSITION = 0.0; // to be tuned
    protected double DROP_POSITION = 0.0; // to be tuned
    Servo servo;

    public void setReceive() {
        servo.setPosition(RECEIVE_POSITION);
    }
    public void setDrop() {
        servo.setPosition(DROP_POSITION);
    }
    public void setPosition(double position) {
        servo.setPosition(position);
    }
    /**
     * Remember that getPosition does NOT return the current, actual position of the servo.
     * It returns the LAST POSITION set in CODE, EVEN IF THE ACTUAL SERVO HASN'T HAD TIME TO MOVE THERE YET!!!
     */
    public double getPosition() { return servo.getPosition(); }
}
