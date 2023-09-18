package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.centerstage.OpenCVPipeline;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

public class ComplicatedPosPipeline extends OpenCvPipeline implements OpenCVPipeline
{
    private boolean DEBUG = true;
    private boolean tunedForFrame = false;
    Mat leftCrop, centerCrop, rightCrop = new Mat();
    double leftRedPercent,centerRedPercent,rightRedPercent;
    double leftBiasOffset,centerBiasOffset,rightBiasOffset;
    Mat output = new Mat();
    final Scalar rectNormalColor = new Scalar(255.0, 0.0, 0.0);
    final Scalar rectFoundColor = new Scalar(0.0, 255.0, 0.0);

    int autoVersion = 0;
    final int WEBCAM_HEIGHT = 544;
    final int WEBCAM_WIDTH = 960;
    final int heightOffset = WEBCAM_HEIGHT - WEBCAM_HEIGHT/2;
    final int widthOffset = 50;

    final Rect leftRect = new Rect(1+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
    final Rect centerRect = new Rect(WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
    final Rect rightRect = new Rect(2 * WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);

    public void setDebug(boolean debug) {
        this.DEBUG = debug;
        this.MEMLEAK_DETECTION_ENABLED = debug;
    }
    public void setOptimization(int level) {
        switch (level) { // This technically could be optimized but I dont think it should because the performance gain would be close to nothing and this function would rarely run anyways
            case 0:
                this.DEBUG = true;
                this.MEMLEAK_DETECTION_ENABLED = true;
                break;
            case 1:
                this.DEBUG = false;
                this.MEMLEAK_DETECTION_ENABLED = true;
                break;
            case 2:
                this.DEBUG = false;
                this.MEMLEAK_DETECTION_ENABLED = false;
                break;
            default:
                setOptimization(0);


        }
    }


    private double scalarSum(Scalar scalar) {
        return scalar.val[0] + scalar.val[1] + scalar.val[2] + scalar.val[3];
    }
    private int indexOfLargest(double[] arr) {
        double largest = Double.NEGATIVE_INFINITY;
        int index = -1;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > largest) {
                largest = arr[i];
                index = i;
            }
        }
        return index;
    }



    @Override
    public Mat processFrame(Mat input)
    {
        // IMPORTANT! If there are weird bugs in this processFrame function then simply keep DEBUG true and the problem must stem from having to return a copy not the original reference.
        if (DEBUG) {
            input.copyTo(output);
        } else {
            output = input;
        }
        // Get the rectangles within the screen that we actually care about
        leftCrop = input.submat(leftRect);
        centerCrop = input.submat(centerRect);
        rightCrop = input.submat(rightRect);

        // Add up all the channels for RGB(A?) individually
        Scalar leftsum = Core.sumElems(leftCrop);
        Scalar centersum = Core.sumElems(centerCrop);
        Scalar  rightsum = Core.sumElems(rightCrop);

        // Get the average percent that each rectangle is red
        leftRedPercent = leftsum.val[0] / scalarSum(leftsum) - leftBiasOffset;
        centerRedPercent = centersum.val[0] / scalarSum(centersum) - centerBiasOffset;
        rightRedPercent = rightsum.val[0] / scalarSum(rightsum) - rightBiasOffset;

        double[] arr = new double[] {leftRedPercent,centerRedPercent,rightRedPercent};

        int screen_side = indexOfLargest(arr);
        autoVersion = screen_side + 1; // Replacing the entire switch statement from before

        if (DEBUG) { // Drawing the squares that tell you where the pipeline detects the item
            tunedForFrame = false; // This is for tuneBias method;
            Rect[] debug_arr = new Rect[] {leftRect, centerRect, rightRect};
            for (int i = 0; i < debug_arr.length; i++){
                Scalar rectColor = i==screen_side ? rectFoundColor : rectNormalColor;
                Imgproc.rectangle(output, debug_arr[i], rectColor , 5);
            }
        }

        return output; // TODO: Find out where does this go to? Driver HUB?
    }

    public int ReturnCurrentTeamPropPos() {
        return autoVersion;
    }

    public void tuneBias() {
        final double K = 0.00001;
        final double min = 0.01; // The bias offset should not be lowered after this threshold
        // We set the minimum for two reasons
        // 1) Because we want some sense of consistency and its easier for the program to hit repeatedly hit a threshold than to just go to zero
        // 2) Because it makes it almost guaranteed that all values will hit the minimum.
        if (tunedForFrame) { return; }
        tunedForFrame = true;
        if (leftBiasOffset   > min) { leftBiasOffset   += leftRedPercent * K; }
        if (centerBiasOffset > min) { centerBiasOffset += centerRedPercent * K; }
        if (rightBiasOffset  > min) { rightBiasOffset  += rightRedPercent * K; }
    }


    public void PrintTelemetry(Telemetry telemetry)
    {
        telemetry.addData("Left Red",leftRedPercent);
        telemetry.addData("Center Red",centerRedPercent);
        telemetry.addData("Right Red",rightRedPercent);
    }
}

/*
I followed this tutorial https://www.youtube.com/watch?v=547ZUZiYfQE - Cai
Help me Leo is holding me hostage - Zane
 */