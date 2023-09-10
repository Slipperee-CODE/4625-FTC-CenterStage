/* Copyright (c) 2017 FIRST. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without modification,
 * are permitted (subject to the limitations in the disclaimer below) provided that
 * the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice, this list
 * of conditions and the following disclaimer.
 *
 * Redistributions in binary form must reproduce the above copyright notice, this
 * list of conditions and the following disclaimer in the documentation and/or
 * other materials provided with the distribution.
 *
 * Neither the name of FIRST nor the names of its contributors may be used to endorse or
 * promote products derived from this software without specific prior written permission.
 *
 * NO EXPRESS OR IMPLIED LICENSES TO ANY PARTY'S PATENT RIGHTS ARE GRANTED BY THIS
 * LICENSE. THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
 * "AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR CONTRIBUTORS BE LIABLE
 * FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL
 * DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 * SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER
 * CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY,
 * OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
 * OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */

package org.firstinspires.ftc.teamcode;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.customclasses.CustomGamepad;
import org.firstinspires.ftc.teamcode.customclasses.CustomOpenCVWebcam;
import org.firstinspires.ftc.teamcode.customclasses.PoseStorage;
import org.firstinspires.ftc.teamcode.customclasses.Robot;
import org.firstinspires.ftc.teamcode.customclasses.TestRRMechanism;
import org.firstinspires.ftc.teamcode.customclasses.Webcam;
import org.firstinspires.ftc.teamcode.drive.SampleMecanumDrive;


@TeleOp(name="NEW_RoadRunnerTeleop_RC", group="Iterative Opmode")


//@Disabled


public class NEW_RoadRunnerTeleop_RC extends OpMode
{
    private SampleMecanumDrive drive = null;
    private Robot robot = null;

    private Webcam webcam = null;

    CustomOpenCVWebcam customOpenCVWebcam = null;


    private TestRRMechanism testRRMechanism = null;

    private PoseStorage poseStorage = new PoseStorage();
    private Pose2d robotPose;


    CustomGamepad customGamepad1 = null;
    CustomGamepad customGamepad2 = null;


    @Override
    public void init()
    {
        robot = new Robot(hardwareMap);
        drive = new SampleMecanumDrive(hardwareMap);
        robot.SetSpeedConstant(0.5);
        drive.setPoseEstimate(poseStorage.currentPose);
        testRRMechanism = new TestRRMechanism(hardwareMap);
        webcam = new Webcam(hardwareMap);
        customOpenCVWebcam = new CustomOpenCVWebcam(webcam.camera, telemetry);
        customGamepad1 = new CustomGamepad(this, 1);
        customGamepad2 = new CustomGamepad(this, 2);
    }


    @Override
    public void init_loop()
    {

    }


    @Override
    public void start() { robot.runtime.reset(); }


    @Override
    public void loop()
    {
        UpdatePose();

        GamepadControls();

        UpdateMechanisms();

        DrivetrainMovement();

        telemetry.update();
    }


    @Override
    public void stop()
    {

    }


    private void UpdatePose()
    {
        drive.update();
        robotPose = drive.getPoseEstimate();

        telemetry.addData("x", robotPose.getX());
        telemetry.addData("y", robotPose.getY());
        telemetry.addData("heading", robotPose.getHeading());
    }


    private void DrivetrainMovement()
    {
        double y = -gamepad1.left_stick_y;
        double x = gamepad1.left_stick_x;
        double rx = -gamepad1.right_stick_x;

        double denominator = Math.max(Math.abs(y) + Math.abs(x) + Math.abs(rx), 1);
        double frontLeftPower = (y + x - rx) / denominator;
        double backLeftPower = (y - x - rx) / denominator;
        double frontRightPower = (y - x + rx) / denominator;
        double backRightPower = (y + x + rx) / denominator;

        robot.rF.SetPower(frontRightPower);
        robot.rB.SetPower(backRightPower);
        robot.lF.SetPower(frontLeftPower);
        robot.lB.SetPower(backLeftPower);
    }


    private void GamepadControls()
    {
        //Insert most gamepad controls here (minus certain drivetrain/path following ones because of motor power overriding);
        //Use finite state machines in all robot mechanisms so that this section is as simple as possible
        customGamepad1.Update();
        customGamepad2.Update();


        if (customGamepad1.aToggle || customGamepad1.bToggle)
        {
            telemetry.addLine("a1 or b1 is toggled");
            testRRMechanism.motorState = TestRRMechanism.MotorState.ON;
        }
        else
        {
            testRRMechanism.motorState = TestRRMechanism.MotorState.OFF;
        }


        if (customGamepad1.yDown)
        {
            telemetry.addLine("y1 is pressed");
        }


        if (customGamepad1.x || customGamepad2.x)
        {
            customOpenCVWebcam.PrintData();
        }
    }


    private void UpdateMechanisms()
    {
        testRRMechanism.Update();
        //Add other mechanisms here
    }
}
