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
                        drive.trajectorySequenceBuilder(new Pose2d(-12, -61.0, Math.PI/2))
                                .forward(26)
                                .waitSeconds(1) //DETECTY

                                //IF WE DETECT:

                                // Center
                                //.splineTo(new Vector2d(-24,-24),Math.PI)

                                //Left
                                //.splineTo(new Vector2d(-35,-34),Math.PI)

                                //Right
                                //.turn(Math.PI/2)
                                //.splineTo(new Vector2d(-35,-34),Math.PI)

                                .waitSeconds(1)// Dumpy
                                // ENDING
                                .splineTo(new Vector2d(-44,-34),Math.PI)


                                .build()
                );






        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}