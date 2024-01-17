package org.firstinspires.ftc.teamcode.customclasses.preILT.mechanisms;

import java.util.Objects;

public class Pair<X,Y> {
    public final X x;
    public final Y y;
    public Pair(X x, Y y) {
        this.x = x;
        this.y = y;
    }
    public boolean equals(Object other) {
        if (other == this) return true;
        if (!(other instanceof Pair)) return false;
        Pair<X,Y> other_ = (Pair<X, Y>) other;
        return Objects.equals(other_.x,x) && Objects.equals(other_.y,y);
    }

}
