package com.example.meepmeeptestingcenterstage;

import com.acmerobotics.roadrunner.geometry.Pose2d;
import com.acmerobotics.roadrunner.geometry.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;
import com.noahbres.meepmeep.roadrunner.trajectorysequence.TrajectorySequenceBuilder;

public class MeepMeepTestingCenterStage {
    public enum TEAMPROP {
        LEFT,CENTER,RIGHT;
    }

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(800);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
                // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
                .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 15)
                .followTrajectorySequence(drive ->
                        addStuff(
                drive.trajectorySequenceBuilder(new Pose2d(36, 61.0, -Math.PI/2))
                        ).build());






        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
    public static TrajectorySequenceBuilder addStuff(TrajectorySequenceBuilder bob) {

            //DETECT CENTER
            switch (TEAMPROP.RIGHT) {
                    case LEFT:
                        bob.setReversed(true)
                                .lineToLinearHeading(new Pose2d(36, -31, 0))
                                .waitSeconds(1)
                                .forward(5)
                                .strafeTo(new Vector2d(36,-13))
                                .setReversed(false)
                                .lineTo(new Vector2d(-38,-13))
                                .turn(Math.PI)
                                .setReversed(true)
                                .strafeLeft(28);

                    break;


                    //.forward(12)
                    case CENTER:
                        bob.back(0.1)
                                .waitSeconds(0.5)
                                .forward(8)
                                .turn(Math.PI/2);
                        //.splineTo(new Vector2d(-24,-30),Math.PI)

                        break;
                    case RIGHT:
bob.setReversed(true)
                        .lineToLinearHeading(new Pose2d(36, 31, 0))
                        .waitSeconds(1)
                        .forward(5)
                        .strafeTo(new Vector2d(36,13))
                        .setReversed(false)
                        .lineTo(new Vector2d(-38,13))
                        .turn(Math.PI)
                        .setReversed(true)
                        .strafeRight(28);
                        break;
                }
                // ENDING
                //bob.strafeTo(new Vector2d(-38,34));
            return bob;
}}