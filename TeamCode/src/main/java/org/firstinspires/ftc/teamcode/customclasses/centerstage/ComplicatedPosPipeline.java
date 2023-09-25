package org.firstinspires.ftc.teamcode.customclasses.centerstage;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.centerstage.OpenCVPipeline;
import org.jetbrains.annotations.Contract;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgproc.Imgproc;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class ComplicatedPosPipeline extends OpenCvPipeline implements OpenCVPipeline
{
    private boolean DEBUG = true;
    private boolean tunedForFrame = false;
    Mat leftCrop, centerCrop, rightCrop = new Mat();
    double leftRedPercent,centerRedPercent,rightRedPercent;
    double leftBiasOffset,centerBiasOffset,rightBiasOffset;
    double leftChange,centerChange,rightChange;
    Mat output = new Mat();
    final Scalar rectNormalColor = new Scalar(255.0, 0.0, 0.0);
    final Scalar rectFoundColor = new Scalar(0.0, 255.0, 0.0);

    int autoVersion = 0;
    public int WEBCAM_HEIGHT;
    public int WEBCAM_WIDTH;
    final int heightOffset = WEBCAM_HEIGHT - WEBCAM_HEIGHT/2;
    final int widthOffset = 50;
    private boolean alliance = false;
    private final Rect[] rects = new Rect[3];
    private final double[] Percents = new double[3];
    private static final ArrayList<Object> NotAValidSide = null;

    public void setDebug(boolean debug) {
        this.DEBUG = debug;
        this.MEMLEAK_DETECTION_ENABLED = debug;
    }

    public void setOptimization(int level) {
        //da tingy
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

    public ComplicatedPosPipeline(String side) {
        super();

        switch (side) {
            case "Blue":
            case "blue":
            case "b":
                alliance = false;
                break;
            case "Red":
            case "red":
            case "r":
                alliance = true;
                break;
            default:
                Object thingy = NotAValidSide.get(1);//Will raise a NullPointerException (hopefully) to catch early on any times that we misspell Blue.. -> Bleu

        }

        if (alliance) {
            //Red Side
            rects[0] =  new Rect(1+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
            rects[1] =  new Rect(WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
            rects[2] =  new Rect(2 * WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
        } else {
            //Bleu Side
            rects[0] =  new Rect(1+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
            rects[1] =  new Rect(WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
            rects[2] =  new Rect(2 * WEBCAM_WIDTH/3+widthOffset, heightOffset,WEBCAM_WIDTH/5 - 1, WEBCAM_HEIGHT/4);
        }

        //right now the rectangles are the same for both red and blue, we can change this later.


    }

    @Contract(pure = true)
    private double scalarSum(Scalar scalar) {
        return scalar.val[0] + scalar.val[1] + scalar.val[2] + scalar.val[3];
    }
    @Contract(pure = true)
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
        // We can simply return the original input reference as is and this should be alot faster than creating a copy.
        if (DEBUG) {
            input.copyTo(output);
        } else {
            output = input;
        }
        // Get the rectangles within the screen that we actually care about
        leftCrop = input.submat(rects[0]);
        centerCrop = input.submat(rects[1]);
        rightCrop = input.submat(rects[2]);

        // Add up all the channels for RGB(A?) individually
        Scalar leftsum = Core.sumElems(leftCrop);
        Scalar centersum = Core.sumElems(centerCrop);
        Scalar  rightsum = Core.sumElems(rightCrop);

        // Get the average percent that each rectangle is red
        Percents[0] = leftsum.val[0] / scalarSum(leftsum) - leftBiasOffset;
        Percents[1] = centersum.val[0] / scalarSum(centersum) - centerBiasOffset;
        Percents[2] = rightsum.val[0] / scalarSum(rightsum) - rightBiasOffset;

        int screen_side = indexOfLargest(Percents);
        autoVersion = screen_side + 1; // Replacing the entire switch statement from before


        if (DEBUG) { // Drawing the squares that tell you where the pipeline detects the item
            for (int i = 0; i < rects.length; i++){
                Scalar rectColor = i==screen_side ? rectFoundColor : rectNormalColor;
                Imgproc.rectangle(output, rects[i], rectColor , 5);
            }
        }
        return output; // TODO: Find out where does this go to? Driver HUB?
    }
    private double fullRangeSquare(double x) {
        if (x>=0) {
            return x * x;
        } else {
            return -x * x;
        }
    }

    public int ReturnCurrentTeamPropPos() {
        return autoVersion;
    }
    public void manualTuneBias(double left, double center, double right) {
        leftBiasOffset += left;
        centerBiasOffset += center;
        rightBiasOffset += right;
    }
    public boolean tuneBias() {
        final double K = 5;
        final double K2 = 0.04;
        final double min = 0.006; // The bias offset should not be lowered after this threshold
        // We set the minimum for two reasons
        // 1) Because we want some sense of consistency and its easier for the program to hit repeatedly hit a threshold than to just go to zero
        // 2) Because it makes it almost guaranteed that all values will hit the minimum.
        if (tunedForFrame) { return false; }
        tunedForFrame = true;
        
        if (Math.abs(Percents[0])   > min) { leftBiasOffset   += fullRangeSquare(K*leftRedPercent) * K2; }
        if (Math.abs(Percents[1]) > min) { centerBiasOffset += fullRangeSquare(K*centerRedPercent) * K2; }
        if (Math.abs(Percents[2])  > min) { rightBiasOffset  += fullRangeSquare(K*rightRedPercent) * K2; }
        leftChange = fullRangeSquare(K*leftRedPercent) * K2;
        centerChange = fullRangeSquare(K*centerRedPercent) * K2;
        rightChange = fullRangeSquare(K*centerRedPercent) * K2;

        return true;
    }


    public void PrintTelemetry(Telemetry telemetry) {
        telemetry.addData("Left Red", Percents[0]);
        telemetry.addData("Center Red", Percents[1]);
        telemetry.addData("Right Red", Percents[2]);
        //telemetry.addData("Left Change : ", leftChange);
        //telemetry.addData("Center Change : ", centerChange);
        telemetry.addData("Right Change : ", rightChange);
        telemetry.addData("Left Bias:", leftBiasOffset);
        telemetry.addData("Center Bias:", centerBiasOffset);
        telemetry.addData("Right Bias:", rightBiasOffset);
    }

    @Override
    public void setCameraResolution(int width, int height) {
        WEBCAM_WIDTH = width;
        WEBCAM_HEIGHT = height;
    }
}

/*
I followed this tutorial https://www.youtube.com/watch?v=547ZUZiYfQE - Cai
Help me Leo is holding me hostage - Zane
 */