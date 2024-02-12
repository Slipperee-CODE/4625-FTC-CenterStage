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

                .followTrajectorySequence(drive ->
                        drive.trajectorySequenceBuilder(new Pose2d(36, 61.0, -Math.PI/2))
                                //DETECT RIGHT

                                .setReversed(true)
                                .lineToLinearHeading(new Pose2d(36, 34,Math.PI))
                                .back(5)

                                .waitSeconds(3)
                                .forward(5)

                                // ENDING
                                .strafeTo(new Vector2d(36,12))
                                .setReversed(false)
                                .splineTo(new Vector2d(-30,12),Math.PI)
                                .turn(Math.PI)
                                .setReversed(true)
                                .splineTo(new Vector2d(-44,34),Math.PI)

                                .build());






        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}