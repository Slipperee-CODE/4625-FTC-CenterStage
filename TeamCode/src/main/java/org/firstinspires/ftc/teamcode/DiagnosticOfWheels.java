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

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.customclasses.CustomDcMotor;
import org.firstinspires.ftc.teamcode.customclasses.Robot;


@TeleOp(name="DiagnosticOfWheels", group="Iterative Opmode")


//@Disabled


public class DiagnosticOfWheels extends OpMode
{
    private Robot robot = null;

    private CustomDcMotor currentMotor = null;
    private int currentMotorIndex = 0;
    private int maxScreens = 2;
    private int screenIndex = 0;

    String[] motorNames = null;


    Telemetry.Item motorItem;
    Telemetry.Item directionItem;
    Telemetry.Item powerItem; //Include Max Power and Current Power
    Telemetry.Item posItem;

    Telemetry.Item zeroPowerBehaviorItem;
    Telemetry.Item modeItem;

    Telemetry.Item typeItem;
    Telemetry.Item motorControllerItem;
    Telemetry.Item portNumItem;


    Telemetry.Item rightFrontMotorItem;
    Telemetry.Item rightBackMotorItem;
    Telemetry.Item leftFrontMotorItem;
    Telemetry.Item leftBackMotorItem;



    @Override
    public void init()
    {
        motorNames = new String[]{"Right Front", "Right Back", "Left Front", "Left Back"};

        robot = new Robot(hardwareMap);
        robot.SetSpeedConstant(0.5);

        telemetry.setAutoClear(false);
        CreateIndividualDiagnosticData();
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
        if (gamepad1.a)
        {
            screenIndex++;
            if (screenIndex > maxScreens -1)
            {
                screenIndex = 0;
            }

            if (screenIndex == 0)
            {
                CreateIndividualDiagnosticData();
            }
            if (screenIndex == 1)
            {
                CreateGroupDiagnosticData();
            }

            sleep(500);
        }

        if (screenIndex == 0)
        {
            IndividualMotorDiagnostic();
            IndividualPowerLogic();
        }
        if (screenIndex == 1)
        {
           GroupMotorDiagnostic();
           GroupPowerLogic();
        }
    }


    @Override
    public void stop()
    {

    }


    private void IndividualPowerLogic()
    {
        robot.customMotors[currentMotorIndex].SetPower(-gamepad1.left_stick_y);
    }


    private void CreateIndividualDiagnosticData()
    {
        telemetry.clearAll();
        motorItem = telemetry.addData("Motor", "");
        directionItem = telemetry.addData("Direction", "");
        powerItem = telemetry.addData("Power", 0);
        posItem = telemetry.addData("Encoder Pos", 0);

        zeroPowerBehaviorItem = telemetry.addData("Zero Power Behavior", "");
        modeItem = telemetry.addData("Mode", "");

        typeItem = telemetry.addData("Registered Type", "");
        motorControllerItem = telemetry.addData("Connected to", "");
        portNumItem = telemetry.addData("Connected to port", "");
    }


    private void CreateGroupDiagnosticData()
    {
        telemetry.clearAll();
        rightFrontMotorItem = telemetry.addData("rightFront - Encoder Value", 0);
        rightBackMotorItem = telemetry.addData("rightBack - Encoder Value", 0);
        leftFrontMotorItem = telemetry.addData("leftFront - Encoder Value", 0);
        leftBackMotorItem = telemetry.addData("leftBack - Encoder Value", 0);
    }


    private void GroupMotorDiagnostic()
    {
        RefreshGroupMotorInfo();
    }


    private void GroupPowerLogic()
    {
        double rightSidePower = -gamepad1.right_stick_y;
        robot.customMotors[0].SetPower(rightSidePower);
        robot.customMotors[1].SetPower(rightSidePower);

        double leftSidePower = -gamepad1.left_stick_y;
        robot.customMotors[2].SetPower(leftSidePower);
        robot.customMotors[3].SetPower(leftSidePower);
    }


    private void RefreshGroupMotorInfo()
    {
        rightFrontMotorItem.setValue(robot.customMotors[0].customMotor.getCurrentPosition());
        rightBackMotorItem.setValue(robot.customMotors[1].customMotor.getCurrentPosition());
        leftFrontMotorItem.setValue(robot.customMotors[2].customMotor.getCurrentPosition());
        leftBackMotorItem.setValue(robot.customMotors[3].customMotor.getCurrentPosition());
    }


    private void IndividualMotorDiagnostic()
    {
        if (gamepad1.dpad_up)
        {
            currentMotorIndex++;
            if (currentMotorIndex == robot.customMotors.length)
            {
                currentMotorIndex = 0;
            }
            sleep(500);
        }

        if (gamepad1.dpad_down)
        {
            currentMotorIndex--;
            if (currentMotorIndex == -1)
            {
                currentMotorIndex = robot.customMotors.length-1;
            }
            sleep(500);
        }

        RefreshIndividualMotorInfo();
    }


    private void RefreshIndividualMotorInfo()
    {
        currentMotor = robot.customMotors[currentMotorIndex];

        motorItem.setValue(motorNames[currentMotorIndex]);
        directionItem.setValue(currentMotor.customMotor.getDirection());
        powerItem.setValue(currentMotor.customMotor.getPower() + " (Max: " + robot.customMotors[currentMotorIndex].speedConstant + ")");
        posItem.setValue(currentMotor.customMotor.getCurrentPosition());

        zeroPowerBehaviorItem.setValue(currentMotor.customMotor.getZeroPowerBehavior());
        modeItem.setValue(currentMotor.customMotor.getMode());

        typeItem.setValue(currentMotor.customMotor.getMotorType());
        motorControllerItem.setValue(currentMotor.customMotor.getController());
        portNumItem.setValue(currentMotor.customMotor.getPortNumber());

        telemetry.update();
    }


    public void sleep(int millis){
        try {
            Thread.sleep(millis);
        } catch (Exception e){}
    }
}
