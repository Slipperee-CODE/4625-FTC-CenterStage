package com.example.meepmeeptestingcenterstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTestingCenterStage {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                //  .followTrajectorySequence(drive ->
                //          drive.trajectorySequenceBuilder(new Pose2d(0, 0, 0))
                //                  .forward(30)
                //                  .turn(Math.toRadians(90))
                //                  .forward(30)
                //                  .turn(Math.toRadians(90))
                //                  .forward(30)
                //                  .turn(Math.toRadians(90))
                //                  .forward(30)
                //                  .turn(Math.toRadians(90))
                //                  .build()
                // // );


                //CENTER
                //    .followTrajectorySequence(drive ->
                //    drive.trajectorySequenceBuilder(new Pose2d(-36.40, -62.70, Math.toRadians(90)))
                //            .splineToLinearHeading(new Pose2d(-35.96, -38.02, Math.toRadians(-90)), Math.toRadians(0))
                //            .splineTo(new Vector2d(24.63, -60.29), Math.toRadians(0))
                //         .build()
                //    );

                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(38.0, -61.0, Math.toRadians(-90)))
                                .back(20)
                                .splineTo(new Vector2d(38, -15),3 * Math.PI/4)
                                .splineTo(new Vector2d(0,-10),Math.PI)
                                .splineTo(new Vector2d(-32,-15),Math.toRadians(20+180))
                                .back(14)


                                .build()
                );






        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}