package org.firstinspires.ftc.teamcode.customclasses.preILT;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;

import org.firstinspires.ftc.teamcode.roadrunner.drive.SampleMecanumDrive;

public abstract class CustomOpMode extends OpMode {
    //pre-ILT -> CustomOpMode
    // - handleState() Removed
    // - enum RobotState {}, initLoop(), onMainLoop(), onNextLoop(), onIdleLoop(), onStopLoop() all removed for simplicity
    //   - I'm hoping that with our new roadrunner technique, there will be no need to go through all the shenanigans
    //     of handling auto states
    // - Renamed Robot class to RobotDrivetrain, robot to robotDriveTrain and drive to roadrunnerDrivetrain
    // - I noticed that the way the methods were set up, it allowed for telemetry.update() to not be present in any child OpModes
    //   however, I deemed it a good idea enough to try removing the extraneous methods especially considering we no
    //   longer have states here

    protected RobotDrivetrain robotDrivetrain;
    protected SampleMecanumDrive roadrunnerDrivetrain;
    protected Clock timer = new Clock();

    @Override //Overrides OpMode's init()
    public void init(){
        //Might want to bring back this later: PoseStorage.currentPose = drive.getPoseEstimate();
        //Just realized the line above is probably what was making it drive backwards sometimes, because it was saving its ending pose
        robotDrivetrain = new RobotDrivetrain(hardwareMap);
        roadrunnerDrivetrain = new SampleMecanumDrive(hardwareMap);
    }

    @Override //Overrides OpMode's init_loop()
    public void init_loop(){
        telemetry.update();
    }

    @Override //Overrides OpMode's start()
    public void start(){

    }

    @Override //Overrides OpMode's loop()
    public final void loop(){
        mainLoop();
        telemetry.update();
    }

    public abstract void mainLoop();
    @Override //Overrides OpMode's stop()
    public void stop(){
        robotDrivetrain.setAllMotorPowers(0.0);
    }

    //With the new way we will be using roadrunner, we may or may not need handleState() implemented here
    //Once I get to making the actual OpModes, I'll change this accordingly
    public static void sleep(long milliseconds) {
        try {
            Thread.sleep(milliseconds);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
