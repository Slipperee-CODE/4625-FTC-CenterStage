package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAuto;

@Autonomous(name="(Blue||Red)Close Meet1")

public class CompForward5 extends WaitingAuto {
    //Roadrunner Stuff
    // Conventions to Follow : the back of the field is the side with the scoring boards, front is the other side with the big apriltags
    // Remember that the when centered to field and heading is 0 then the robot is facing the right because the heading 0 is to the right on a unit circle


    // Mechanisms || Webcams || Timers
    private Clock clock = null;
    //private final PixelTiltOuttake pixelTiltOuttake = null;
    //private final LinearSlides linearSlides = null;
    //private Webcam webcam = null;

    //private LeosAprilTagFun tagAlign = null;
    private CustomGamepad gamepadOne;
    // Miss

    public static double FORWARD_TIME = 3.0;

    public void init() {
        super.init();
        telemetry.setMsTransmissionInterval(6);
        //pixelTiltOuttake = new PixelTiltOuttake(hardwareMap);
        //linearSlides = new LinearSlides(hardwareMap, telemetry);
        gamepadOne = new CustomGamepad(this,1);
        clock = new Clock();
        timer = new Clock();
        MissingHardware.printMissing(telemetry);
        sleep(1000);

    }



    protected boolean handleState(RobotState state) {return false;    }

    public void startAfterWait() {
        clock.reset();
    }

    protected void update() {

        if (clock.getTimeSeconds() < FORWARD_TIME) {
            robot.emulateController(-.356,0,0);
        } else {
            robot.stop();
        }
    }

    protected void onNextLoop() {

    }

    protected void onIdleLoop() {    }

}
