package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.ColorSensorMechanism;
@Disabled
@TeleOp(name = "Color Sensor Test")
public class TeamPropTestColorSensor extends CustomOpMode {
    ColorSensorMechanism colorSensorLeft;
    ColorSensorMechanism colorSensorRight;
    boolean left,right;
    double redThreshold = .1;
    public void init() {
        super.init();
        colorSensorLeft = new ColorSensorMechanism(hardwareMap,"LeftColor",2.0);
        colorSensorRight = new ColorSensorMechanism(hardwareMap,"RightColor",2.0);

    }
    @Override
    protected void initLoop() {}

    @Override
    protected void onMainLoop() {
        colorSensorLeft.update();
        colorSensorRight.update();
        double shifted = (colorSensorLeft.hue + 0.5) % 1;
        // now red should be contained at the middle of the spectrum
        left = Math.abs(shifted - 0.5) < redThreshold;
        shifted = (colorSensorRight.hue + 0.5) % 1;
        // now red should be contained at the middle of the spectrum
        right = Math.abs(shifted - 0.5) < redThreshold;

        telemetry.addData("Detected on Left: ",left);
        telemetry.addData("Detected on Right: ", right);

    }

    @Override
    protected void onNextLoop() {}
    @Override
    protected void onIdleLoop() {}

    @Override
    protected boolean handleState(RobotState state) {return false;}

    @Override
    public void start() {


    }
}
