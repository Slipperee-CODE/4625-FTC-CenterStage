package org.firstinspires.ftc.teamcode.customclasses.preMeet3;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;
import org.openftc.apriltag.AprilTagDetection;

public class CustomAprilTagPose {
        public double x;
        public double z;
        public double angleY;
        public int id;

        public static CustomAprilTagPose fromNative(AprilTagDetection detection) {
            CustomAprilTagPose x = new CustomAprilTagPose();
            x.x = detection.pose.x;
            x.z = detection.pose.z;
            Orientation rot = Orientation.getOrientation(detection.pose.R, AxesReference.INTRINSIC, AxesOrder.YXZ, AngleUnit.RADIANS); // Maybe find a way to get the y rotation in radians without calculating all the rotations
            x.angleY = rot.firstAngle;
            x.id = detection.id;

            return x;
        }

}
