
/*
 * Copyright (c) 2021 OpenFTC Team
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.firstinspires.ftc.teamcode.customclasses.webcam;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import org.opencv.core.Mat;

import org.opencv.imgproc.Imgproc;
import org.openftc.apriltag.AprilTagDetection;
import org.openftc.apriltag.AprilTagDetectorJNI;
import org.openftc.apriltag.ApriltagDetectionJNI;
import org.openftc.easyopencv.OpenCvPipeline;

import java.util.ArrayList;

public class SpeedyAprilTagPipeline extends OpenCvPipeline implements OpenCVPipeline
{
    private long nativeApriltagPtr;
    private final Mat grey = new Mat();

    private final long greyAddr = grey.dataAddr();

    private ArrayList<AprilTagDetection> detections = new ArrayList<>();
    private ArrayList<AprilTagDetection>  detectionsUpdate = new ArrayList<>();
    private long internalCounter = 0;
    private long externalCounter = 0;

    double fx;
    double fy;
    double cx;
    double cy;

    // UNITS ARE METERS
    double tagsize;


    public SpeedyAprilTagPipeline(double tagsize) {
        this(tagsize,835.64,835.64,459.22,261.933);
    }
    public SpeedyAprilTagPipeline(double tagsize, double fx, double fy, double cx, double cy)
    {
        this.MEMLEAK_DETECTION_ENABLED = false;

        this.tagsize = tagsize;

        this.fx = fx;
        this.fy = fy;
        this.cx = cx;
        this.cy = cy;

        // Allocate a native context object. See the corresponding deletion in the finalizer
        nativeApriltagPtr = AprilTagDetectorJNI.createApriltagDetector(AprilTagDetectorJNI.TagFamily.TAG_36h11.string, 3, 3);
        if (nativeApriltagPtr == 0) {
            throw new IllegalArgumentException("THINGY WAS WRONG NEVER WAS CREATED");
        }
    }

    @Override
    protected void finalize()
    {
        // Might be 0 if createApriltagDetector() threw an exception
        if(nativeApriltagPtr != 0)
        {
            // Delete the native context we created in the constructor
            AprilTagDetectorJNI.releaseApriltagDetector(nativeApriltagPtr);
            nativeApriltagPtr = 0;
        }
        else
        {
            System.out.println("AprilTagDetectionPipeline.finalize(): AprilTagDetector was never created!");
        }
    }

    @Override
    public Mat processFrame(Mat input)
    {
        // Convert to greyscale
        Imgproc.cvtColor(input, grey, Imgproc.COLOR_RGBA2GRAY);
        // Run AprilTag

        // Get a pointer to a native array of detections
        long ptrDetectionArray = AprilTagDetectorJNI.runApriltagDetector(nativeApriltagPtr, greyAddr, grey.width(), grey.height());
        if(ptrDetectionArray != 0) // Check if the array was not a null pointer
        {
            detections = ApriltagDetectionJNI.getDetections(ptrDetectionArray, tagsize, fx, fy, cx, cy); // convert the native array to an arraylist
            ApriltagDetectionJNI.freeDetectionList(ptrDetectionArray); // free the list back to memory
            internalCounter++;
            detectionsUpdate = detections;
        }

        return input;
    }

    public void setDecimation(float decimation,Telemetry telemetry)
    {
        telemetry.addLine("Settings decimation");
        telemetry.update();
        // If this throws an error we have to put it back into the processFrame method
        setDecimation(decimation);
        telemetry.addLine("decimation set!");
        telemetry.update();

    }
    public void setDecimation(float decimation) {
        AprilTagDetectorJNI.setApriltagDetectorDecimation(nativeApriltagPtr, decimation);
    }

    public ArrayList<AprilTagDetection> getLatestDetections()
    {
        return detections;
    }

    public ArrayList<AprilTagDetection> getDetectionsUpdate()
    {
        if (internalCounter==externalCounter) return null;
        externalCounter = internalCounter;
        return detectionsUpdate;
    }

    /// MANDATORY METHODS FOR INTERFACE ///






    @Override
    public int ReturnCurrentTeamPropPos() {
        return 0;
    }

    @Override
    public void PrintTelemetry(Telemetry telemetry) {

    }

    @Override
    public boolean tuneBias() {
        return false;
    }

    @Override
    public void manualTuneBias(double left, double center, double right) {

    }

    @Override
    public void setCameraResolution(int width, int height) {

    }

    @Override
    public void setOptimization(int level) {

    }

    @Override
    public void setDebug(boolean debug) {

    }



}