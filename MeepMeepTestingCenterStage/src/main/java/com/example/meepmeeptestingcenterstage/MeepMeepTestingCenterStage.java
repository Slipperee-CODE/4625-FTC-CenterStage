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
                                // BLUE CLOSE SIDE
                                .back(26)
                                .waitSeconds(1) //DETECTY

                                //IF WE DETECT:

                                // Center
                                .back(6)
                                .waitSeconds(3)
                                .forward(8)
                                .turn(Math.PI/2)
                                .waitSeconds(1)

                                //Left
                                //.splineTo(new Vector2d(-32,-34),Math.PI)
                                //.turn(Math.PI)
                                //.waitSeconds(1)
                                //.forward(4)
                                //.turn(Math.PI)


                                //Right
                                //.turn(-Math.PI/2)
                                //.back(4)
                                //.waitSeconds(1)// Dumpy
                                //.forward(10)
                                //.turn(Math.PI)
                                //.forward(4)
                                //.splineTo(new Vector2d(-35,-34),Math.PI)

                                // ENDING
                                .splineTo(new Vector2d(-44,-34),Math.PI)
                                .build());






        meepMeep.setBackground(MeepMeep.Background.FIELD_POWERPLAY_OFFICIAL)
                .setDarkMode(true)
                .setBackgroundAlpha(0.95f)
                .addEntity(myBot)
                .start();
    }
}