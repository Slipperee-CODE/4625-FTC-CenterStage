package org.firstinspires.ftc.teamcode.customclasses;


public class Clock {
    private static final double NANOSECONDS_PER_SECOND = 1_000_000_000;
    private long startTime;
    public Clock() {
        startTime = getNs();
    }
    public long getNs() {
        return System.nanoTime();
    }

    public double getTimeSeconds() {
        return getTime() /  NANOSECONDS_PER_SECOND;
    }
    public long getTime() {
        return getNs() - startTime;
    }

    // This may or may not be a side effect, which affects the purity of this method.
    // This resets <startTime> to the current time which may be undesired in some situations
    public long tick() {
        final long currTime = getNs();
        final long time = currTime - startTime;
        startTime = currTime;
        return time;
    }
    public void reset() {
        startTime = getNs();
    }
    public double getDeltaSeconds() {
        return tick() / NANOSECONDS_PER_SECOND;
    }
}
