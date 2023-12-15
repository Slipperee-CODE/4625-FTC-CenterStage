package org.firstinspires.ftc.teamcode.opmodes.comp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.Clock;
import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.AprilTagAlign;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.MechanismState;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.WaitingAutoLinear;

@Autonomous(name = "!RoadRunner Blue Close")
public class NRRBlueClose extends WaitingAutoLinear {
    private AprilTagAlign tagAlign;
    private BNO055IMU imu;
    private TeamPropDetection teamPropDetection;
    private Outtake outtake;
    private final Clock detectionTime = new Clock();

    @Override
    public void initialize() {
        teamPropDetection = new TeamPropDetection(hardwareMap);
        outtake = new Outtake(hardwareMap,new CustomGamepad(gamepad2));
        imu = hardwareMap.get(BNO055IMU.class,"imu");
        tagAlign = new AprilTagAlign(hardwareMap,telemetry,new CustomGamepad(gamepad2),robot);
        tagAlign.setState(MechanismState.OFF);
    }

    @Override
    public void run() {
        robot.emulateController(.5,0,0);
        sleep(2_000);
        robot.stop();
        detectionTime.reset();
        while (detectionTime.getTimeSeconds() < 2.0) {
            teamPropDetection.update();
            telemetry.addData("DETECTED POSITION: ",teamPropDetection.state.toString());
        }
        switch (teamPropDetection.state) {
            case LEFT:
            case RIGHT:
            case CENTER:
                break;
        }
        outtake.setDropLowerPositionWithLidClosed();
        sleep(500);
        outtake.drop();
        sleep(1_000);
        outtake.setReceivePosition();
        robot.emulateController(0,0,-.5);
        sleep(1_500);
        robot.stop();

        robot.emulateController(.5,0,0);
        sleep(1_000);
        robot.stop();
        tagAlign.setState(MechanismState.ON);
        sleep(1_000);
    }
    private void turnRobotUsingExternalHeading(double requestedAngle) {
        final double ERROR_THRESHOLD = 0.1;
        final double K_TURN = 0.5;
        Orientation angles   = imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.RADIANS);

        double error = 1000;
        while (error > ERROR_THRESHOLD) {
            error = requestedAngle - angles.firstAngle;
            robot.emulateController(0, 0, error * K_TURN);
        }

    }
}