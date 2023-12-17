package org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;

public class ColorSensorMechanism extends MechanismBase implements Mechanism{
    ColorSensor colorSensor;
    double gain;

    public final double[] rgb = {0,0,0}; // [0,255]
    public double hue = 0.0; // [0,1]

    public int hueDegrees = 0; // [0,360]

    public ColorSensorMechanism(HardwareMap hardwareMap, String name,double gain ) {
        colorSensor = getHardware(ColorSensor.class, name, hardwareMap);
        this.gain = gain;
        this.state = MechanismState.ON;

    }
    @Override
    public void setState(MechanismState state) {
        this.state = state;
        colorSensor.enableLed(state == MechanismState.ON);
        if (state != MechanismState.ON && state != MechanismState.OFF) {
            throw new RuntimeException("Color sensor was set to an invalid state.");
        }
    }

    @Override
    public void update() {
        if (this.state == MechanismState.OFF) return;
        rgb[0] = Math.max(0,Math.min(1,colorSensor.red() * gain));
        rgb[1] = Math.max(0,Math.min(1,colorSensor.green() * gain));
        rgb[2] = Math.max(0,Math.min(1,colorSensor.blue() * gain));

        double h = calculateHue(rgb[0],rgb[1],rgb[2]);
        hueDegrees = (int) h;
        hue = h/360.0;
    }
    private double calculateHue(double R, double G, double B) {
        // restrict domain from [0,255] -> [0,1]
        final double r = R / 255.0;
        final double g = G / 255.0;
        final double b = B / 255.0;
        final double cmax = Math.max(r, Math.max(g, b)); // maximum of r, g, b
        final double cmin = Math.min(r, Math.min(g, b)); // minimum of r, g, b
        final double diff = cmax - cmin; // diff of cmax and cmin.
        if (cmax == cmin)
            return 0;
        else if (cmax == r)
            return (60 * ((g - b) / diff) + 360) % 360;
        else if (cmax == g)
            return (60 * ((b - r) / diff) + 120) % 360;
        else if (cmax == b)
            return (60 * ((r - g) / diff) + 240) % 360;
        else  {
            return -1; // something is seriously wrong
        }
    }


}
