package org.firstinspires.ftc.teamcode.opmodes.ILT.testing;

import android.text.method.Touch;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.TouchSensor;

import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms.ServoTuner;

import java.util.ArrayList;

@TeleOp(name="Check Everything")
public class CheckEverything extends CustomOpMode {

    private Servo intakeRotator;
    private Servo pixelQuickRelease;

    private Servo outtakeServoLeft;
    private Servo outtakeServoRight;

    private Servo capper;
    private Servo dropper;
    private TouchSensor touchSensor1;
    private TouchSensor touchSensor2;
    private ServoTuner servoTuner;
    private CustomGamepad customGamepad1;
    private CustomGamepad customGamepad2;
    private ArrayList<Servo> servoList = new ArrayList<Servo>();

    private DcMotor intakeMotor;
    private DcMotor hangingMotor;
    public static final double DPAD_SPEED = 0.25;

    private Outtake outtake;

    @Override
    public void init() {
        super.init();
        robotDrivetrain.setSpeedConstant(0.85);
        customGamepad2 = new CustomGamepad(this, 2);
        customGamepad1 = new CustomGamepad(this, 1);
        intakeRotator = hardwareMap.get(Servo.class, "intakeRotator"); //FULL DOWN POS: TBD  FULL UP POS: TBD
        pixelQuickRelease = hardwareMap.get(Servo.class, "pixelQuickRelease"); //UP POS: 0  DOWN POS: .72
        touchSensor1 = hardwareMap.get(TouchSensor.class, "touchSensor1");
        touchSensor2 = hardwareMap.get(TouchSensor.class, "touchSensor2");
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        hangingMotor = hardwareMap.get(DcMotor.class, "hanging");
        outtakeServoLeft = hardwareMap.get(Servo.class, "outtakeServoLeft"); //OUT: 0 IN: .98
        outtakeServoLeft.setDirection(Servo.Direction.REVERSE);
        outtakeServoRight = hardwareMap.get(Servo.class, "outtakeServoRight"); //OUT: 0 IN: .98

        dropper = hardwareMap.get(Servo.class, "dropper"); //FULL IN: 1 HALF IN: .95 FULL OUT: .8488
        capper = hardwareMap.get(Servo.class, "capper"); //BLOCKING: 0 NOT BLOCKING: .085

        servoList.add(intakeRotator);
        servoList.add(pixelQuickRelease);
        servoList.add(dropper);
        servoList.add(capper);
        //servoList.add(outtakeServoLeft);
        //servoList.add(outtakeServoRight);
        servoTuner = new ServoTuner(servoList, customGamepad1);

        //outtakeServoLeft.setPosition(.98);
        //outtakeServoRight.setPosition(.98);

        outtake = new Outtake(hardwareMap, customGamepad2);
    }

    @Override
    public void mainLoop() {
        customGamepad1.update();
        customGamepad2.update();
        servoTuner.update(telemetry);
        telemetry.addData("Touch Sensor 1 State", touchSensor1.isPressed());
        telemetry.addData("Touch Sensor 2 State", touchSensor2.isPressed());

        //outtake.update(telemetry);

        /*
        if (customGamepad1.aDown) {
            outtakeServoLeft.setPosition(0);
            outtakeServoRight.setPosition(0);
        } else if (customGamepad1.yDown) {
            outtakeServoLeft.setPosition(.98);
            outtakeServoRight.setPosition(.98);
        }
        */



        /*
        if (customGamepad2.right_stick_y != 0) {
            outtakeServoLeft.setPosition(outtakeServoLeft.getPosition() + customGamepad2.right_stick_y / 100);
            outtakeServoRight.setPosition(outtakeServoRight.getPosition() + customGamepad2.right_stick_y / 100);
            telemetry.addData("outtakeServoLeft pos", outtakeServoLeft.getPosition());
            telemetry.addData("outtakeServoRight pos", outtakeServoRight.getPosition());
        }
        */


        /*
        if (gamepad1.dpad_down || gamepad1.dpad_up || gamepad1.dpad_left || gamepad1.dpad_right) {
            double horizontal = 0;
            double vert= 0;
            if (gamepad1.dpad_left) horizontal -= DPAD_SPEED;
            if (gamepad1.dpad_right) horizontal += DPAD_SPEED;
            if (gamepad1.dpad_up) vert += DPAD_SPEED;
            if (gamepad1.dpad_down) vert -= DPAD_SPEED;
            robotDrivetrain.emulateController(vert,horizontal,0);
        } else {
            robotDrivetrain.emulateController(gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x  * 0.5);
        }
        */
        if (customGamepad2.aToggle){
            intakeMotor.setPower(0.5);
        } else {
            intakeMotor.setPower(0);
        }

        if (customGamepad2.bToggle){
            hangingMotor.setPower(0.1);
        } else {
            hangingMotor.setPower(0);
        }    }
}
