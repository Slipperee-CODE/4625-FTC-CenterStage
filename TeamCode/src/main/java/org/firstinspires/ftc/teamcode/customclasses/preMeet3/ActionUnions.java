package org.firstinspires.ftc.teamcode.customclasses.preMeet3;

import org.firstinspires.ftc.teamcode.customclasses.preMeet3.mechanisms.Mechanism;
import org.firstinspires.ftc.teamcode.roadrunner.trajectorysequence.TrajectorySequence;

import java.util.ArrayList;

public class ActionUnions {
    private ArrayList<Action> actions = null;

    public void runAll() {

    }
    public class ActionUnionBuilder {
        private ArrayList<Mechanism> actions = new ArrayList<>();
        public ActionUnionBuilder() {

        }
    }
    public interface Action extends Mechanism {
        boolean getRunning();
        boolean getDone();
        boolean getAwaiting();
    }
    public abstract class RoadRunnerWrapper implements Action{
        ArrayList<TrajectorySequence> trajectories = null;


    }
}
