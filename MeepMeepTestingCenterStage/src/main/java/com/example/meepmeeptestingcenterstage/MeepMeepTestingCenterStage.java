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
                 drive.trajectorySequenceBuilder(new Pose2d(-12, -61.0, -Math.PI/2))
                .setReversed(true)
                         .back(26)
                         .turn(Math.PI/2)
                         .back(5)
                         .waitSeconds(3)
                         .splineTo(new Vector2d(-55,-60) ,Math.PI)
                         .setReversed(false)
                         .forward(12)
                         .strafeTo(new Vector2d(-44,-36))
                         //.back(10)
                         //.turn(Math.PI)
                         //.back(10)
                         //.waitSeconds(3)
                         //.forward(10)
                         //.turn(Math.PI)
                         //.setReversed(true)
                         //.lineTo(new Vector2d(-44,-36))
                        .build());





        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}