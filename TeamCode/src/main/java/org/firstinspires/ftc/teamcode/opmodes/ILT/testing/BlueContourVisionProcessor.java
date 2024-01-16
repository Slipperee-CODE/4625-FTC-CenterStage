package org.firstinspires.ftc.teamcode.opmodes.ILT.testing;

//C:\Users\Micaiah\Desktop\4625-FTC-CenterStage2023-2024\TeamCode\src\main\java\org\firstinspires\ftc\teamcode\opmodes\ILT\testing

import android.graphics.Canvas;

import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;

import java.util.ArrayList;
import java.util.List;

public class BlueContourVisionProcessor implements VisionProcessor {
    public enum TeamPropState{
        LEFT,
        RIGHT,
        CENTER
    }

    public TeamPropState teamPropState;
    public Scalar lowerBlue = new Scalar(70, 40, 40);
    public Scalar upperBlue = new Scalar(128, 255, 255);

    private int WIDTH;
    private int HEIGHT;

    private Rect rectLeft;
    private Rect rectCenter;
    private Rect rectRight;

    Mat processedFrame;
    double cX = 0;
    double cY = 0;

    private double leftDifference;
    private double rightDifference;
    private double centerDifference;


    @Override
    public void init(int width, int height, CameraCalibration calibration) {
        WIDTH = width;
        HEIGHT = height;
        rectLeft = new Rect(0, 0, (int) (WIDTH/3.0 * 1) , HEIGHT);
        rectCenter = new Rect((int) (WIDTH/3.0 * 1), 0, (int) (WIDTH/3.0 * 1), HEIGHT);
        rectRight = new Rect((int) (WIDTH/3.0 * 2), 0, (int) (WIDTH/3.0 * 1), HEIGHT);
    }

    /*
    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        Mat redMask = new Mat();
        Core.inRange(hsvMat, lowerRed, upperRed, redMask);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(redMask, redMask, Imgproc.MORPH_OPEN, kernel);
        Imgproc.morphologyEx(redMask, frame, Imgproc.MORPH_CLOSE, kernel);

        return null;
    }
    */


    @Override
    public Object processFrame(Mat frame, long captureTimeNanos) {
        processedFrame = preprocessFrame(frame);

        Mat hierarchy = new Mat();
        List<MatOfPoint> contours = new ArrayList<>();
        Imgproc.findContours(processedFrame,contours,hierarchy,Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);

        MatOfPoint largestContour = findLargestContour(contours);

        if (largestContour != null){
            Imgproc.drawContours(frame,contours,contours.indexOf(largestContour), new Scalar(255,0,0),2);

            // Calculate the centroid of the largest contour
            Moments moments = Imgproc.moments(largestContour);
            cX = moments.get_m10() / moments.get_m00();
            cY = moments.get_m01() / moments.get_m00();

            // Draw a dot at the centroid
            String label = "(" + (int) cX + ", " + (int) cY + ")";
            Imgproc.putText(frame, label, new Point(cX + 10, cY), Imgproc.FONT_HERSHEY_COMPLEX, 0.5, new Scalar(0, 255, 0), 2);
            Imgproc.circle(frame, new Point(cX, cY), 5, new Scalar(0, 255, 0), -1);

            //Find Centers of Active Rectangles

            //Measure Lateral distance between centers (find the smallest)
            //Make boxes with appropriate colors


            leftDifference = Math.abs((rectLeft.x + rectLeft.width/2.0) - cX);
            centerDifference = Math.abs((rectCenter.x + rectCenter.width/2.0) - cX);
            rightDifference = Math.abs((rectRight.x + rectRight.width/2.0) - cX);

            double minDifference = Math.min(leftDifference,Math.min(centerDifference,rightDifference));

            if (leftDifference == minDifference){
                Imgproc.rectangle(frame,rectCenter,new Scalar(255, 0, 0),10);
                Imgproc.rectangle(frame,rectRight,new Scalar(255, 0, 0),10);
                Imgproc.rectangle(frame,rectLeft,new Scalar(0, 255, 0),10);
                teamPropState = TeamPropState.LEFT;
            } else if (rightDifference == minDifference){
                Imgproc.rectangle(frame,rectLeft,new Scalar(255, 0, 0),10);
                Imgproc.rectangle(frame,rectCenter,new Scalar(255, 0, 0),10);
                Imgproc.rectangle(frame,rectRight,new Scalar(0, 255, 0),10);
                teamPropState = TeamPropState.RIGHT;
            } else {
                Imgproc.rectangle(frame,rectLeft,new Scalar(255, 0, 0),10);
                Imgproc.rectangle(frame,rectRight,new Scalar(255, 0, 0),10);
                Imgproc.rectangle(frame,rectCenter,new Scalar(0, 255, 0),10);
                teamPropState = TeamPropState.CENTER;
            }
        }
        return null;
    }


    @Override
    public void onDrawFrame(Canvas canvas, int onscreenWidth, int onscreenHeight, float scaleBmpPxToCanvasPx, float scaleCanvasDensity, Object userContext) {

    }


    private Mat preprocessFrame(Mat frame){
        Mat hsvMat = new Mat();
        Imgproc.cvtColor(frame, hsvMat, Imgproc.COLOR_RGB2HSV);
        Mat redMask = new Mat();
        Core.inRange(hsvMat, lowerBlue, upperBlue, redMask);

        Mat kernel = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(5, 5));
        Imgproc.morphologyEx(redMask, redMask, Imgproc.MORPH_OPEN, kernel);
        Imgproc.morphologyEx(redMask, redMask, Imgproc.MORPH_CLOSE, kernel);

        return redMask;
    }

    private MatOfPoint findLargestContour(List<MatOfPoint> contours) {
        double maxArea = 0;
        MatOfPoint largestContour = null;

        for (MatOfPoint contour : contours) {
            double area = Imgproc.contourArea(contour);
            if (area > maxArea) {
                maxArea = area;
                largestContour = contour;
            }
        }
        return largestContour;
    }



}