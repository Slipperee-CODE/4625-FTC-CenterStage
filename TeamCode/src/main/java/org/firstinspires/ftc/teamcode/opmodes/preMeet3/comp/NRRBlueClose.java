package org.firstinspires.ftc.teamcode.opmodes.preMeet3.comp;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.firstinspires.ftc.teamcode.customclasses.preILT.Clock;
import org.firstinspires.ftc.teamcode.customclasses.preILT.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.ActiveIntake;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Outtake;
import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.TeamPropDetection;
import org.firstinspires.ftc.teamcode.opmodes.preMeet3.WaitingAutoLinear;
@Disabled
@Autonomous(name = "!RoadRunner Blue Close")
public class NRRBlueClose extends WaitingAutoLinear {
    //private AprilTagAlign tagAlign;
    private BNO055IMU imu;
    private TeamPropDetection teamPropDetection;
    private Outtake outtake;
    private ActiveIntake activeIntake;
    private final Clock detectionTime = new Clock();

    @Override
    public void initialize() {
        teamPropDetection = new TeamPropDetection(hardwareMap);
        outtake = new Outtake(hardwareMap,new CustomGamepad(gamepad2));
        imu = hardwareMap.get(BNO055IMU.class,"imu");
        activeIntake = new ActiveIntake(hardwareMap, new CustomGamepad(gamepad1));

        //tagAlign = new AprilTagAlign(hardwareMap,telemetry,new CustomGamepad(gamepad2),robot);

        //tagAlign.setState(MechanismState.OFF);
    }

    @Override
    public void run() {
        robot.emulateController(.5,0,0);
        int initial_encoder = robot.rightBack.getCurrentPosition();
        detectionTime.reset();
        while (detectionTime.getTimeSeconds() < 1.133) {
            int error = robot.rightBack.getCurrentPosition() - initial_encoder;
            robot.emulateController(.46,error * 0.00005,0);
        }
        //sleep(1700 * 2 / 3);
        robot.stop();
        detectionTime.reset();
        while (detectionTime.getTimeSeconds() < 2.0) {
            teamPropDetection.update();
            telemetry.addData("DETECTED POSITION: ",teamPropDetection.state.toString());
            telemetry.update();
        }

        switch (teamPropDetection.state) {
            case LEFT:
                turnRight90();
                robot.emulateController(-.2,0,0);
                sleep(2_200);
                robot.stop();
                outtake.setDropLowerPositionWithLidClosed();
                sleep(500);
                outtake.drop();
                sleep(500);
                robot.emulateController(-.2,0,0);
                sleep(2_000);
                turnLeft90();
                turnLeft90();

                break;
            case RIGHT:
                turnRight90();
                robot.emulateController(.2,0,0);
                sleep(800);
                robot.emulateController(-.2,0,0);
                sleep(1_000);
                robot.stop();
                outtake.setDropLowerPositionWithLidClosed();
                sleep(500);
                outtake.drop();
                sleep(500);
                robot.emulateController(-.2,0,0);
                sleep(1_000);
                robot.stop();
                robot.emulateController(-.5,0,0);
                sleep(2_000);
                robot.stop();
                break;
            case CENTER:
                robot.emulateController(.2,0,0);
                sleep(800);
                robot.emulateController(-.2,0,0);
                sleep(800);
                robot.stop();
                outtake.setDropLowerPositionWithLidClosed();
                sleep(500);
                outtake.drop();
                sleep(500);
                robot.emulateController(-.2,0,0);
                sleep(1_000);
                robot.stop();

                // drive to thingy
                outtake.setReceivePosition();
                robot.emulateController(0,0,-.5);
                sleep(1_200);
                robot.stop();

                robot.emulateController(.5,0,0);
                sleep(1_500);
                robot.stop();

                //tagAlign.setState(MechanismState.ON);

                sleep(1_000);
                break;
        }


    }
    private void turnLeft90(){
        robot.emulateController(0,0,-.5);
        sleep(1_120);
        robot.stop();
    }
    private void turnRight90() {
        robot.emulateController(0,0,.5);
        sleep(1_120);
        robot.stop();
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