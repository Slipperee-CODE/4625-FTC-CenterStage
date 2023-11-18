package org.firstinspires.ftc.teamcode.opmodes;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.customclasses.CustomOpMode;
import org.firstinspires.ftc.teamcode.customclasses.PIDMotor;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismBase;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MissingHardware;

@TeleOp(name = "Outtake Tuner")
public class OuttakeTuner extends CustomOpMode {
    private static final float OVERRIDE_SPEED = 10.0f;

    public static int DROP_PIXEL_MIN_POSITION = 500; // position for linear slides
    public static int DROP_PIXEL_MAX_POSITION = 3000; // position for linear slides
    public final int readyToReceivePixelsTarget = 0; // position for linear slides
    public final int readyToDropPixelsTarget = 1000; // position for linear slides
    public static double OUTTAKE_RECIEVE_ANGLER_POSITION = 0.0; // position for dropAngler
    public static double OUTTAKE_DROP_ANGLER_POSITION_LOWER = 0.3; // position for dropAngler
    public static double OUTTAKE_DROP_ANGLER_POSITION_NORMAL = 0.6; // position for dropAngler
    public static double LID_RECIVE_POSITION  = 0.3; // position for LidAngler
    public static double LID_DROP_POSITION  = 0.3; // position for LidAngler
    public static double OUTTAKE_CLOSED_POSITION = 0.0; // position for Dropper
    public static double OUTTAKE_OPEN_POSITION = 1.0; // position for Dropper
    public static float STARTING_JOYSTICK_THRESHOLD = 0.2f;
    private PIDMotor SlidesMotor;
    private Servo DropAngler;
    private Servo LidAngler;
    private Servo Dropper;
    @Override
    public void initLoop() {}
    public void init() {
        super.init();
        Dropper = hardwareMap.get(Servo.class,"OuttakeDropper");
        LidAngler = hardwareMap.get(Servo.class,"OuttakeLidAngler");
        try {
            DropAngler = hardwareMap.get(Servo.class, "OuttakeAngler");
        }  catch (Exception ignored){
            MissingHardware.addMissingHardware("OuttakeAngler");
        }
    }
    public void onMainLoop() {
        double angler_position = Range.clip(DropAngler.getPosition() + gamepad1.left_stick_y * 0.0001,0.0,1.0);
        double lid_position = Range.clip(LidAngler.getPosition() + gamepad1.right_stick_y * 0.0001,0.0,1.0);
        double dropper_position = Range.clip(Dropper.getPosition() + gamepad1.left_trigger * 0.0001,0.0,1.0);
        DropAngler.setPosition(angler_position);
        LidAngler.setPosition(lid_position);
        Dropper.setPosition(dropper_position);

        telemetry.addData("Angler: ",angler_position);
        telemetry.addData("Lid: ",lid_position);
        telemetry.addData("Dropper: ",dropper_position);
    }
    @Override
    public void onNextLoop() {}

    @Override
    protected void onIdleLoop() {}

    @Override
    protected boolean handleState(RobotState state) {
        return false;
    }

    @Override
    public void start() {

    }
}
