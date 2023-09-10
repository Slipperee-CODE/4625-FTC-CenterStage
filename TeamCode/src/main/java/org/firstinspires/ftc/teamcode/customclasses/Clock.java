package org.firstinspires.ftc.teamcode.customclasses;

import com.qualcomm.robotcore.util.ElapsedTime;

public class Clock {
    private long startTime;
    public Clock() {
        startTime = getNs();
    }
    public long getNs() {
        return System.nanoTime();
    }
    public long getTime() {
        return getNs() - startTime;
    }
    public long tick() {
        final long currTime = getNs();
        final long time = currTime - startTime;
        startTime = currTime;
        return time;
    }
    public long getDeltaTime() {
        return tick();
    }
}
