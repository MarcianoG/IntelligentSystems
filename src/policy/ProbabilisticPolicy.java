package policy;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import main.Action;
import main.State;
import player.RandomPlayer;

public class ProbabilisticPolicy extends HashMap<State, double[]> implements Policy {

    /**
     * generated id
     */
    private static final long serialVersionUID = -7901905496277036579L;
    private HashMap<Integer, HashMap<Action, Double>> policy;

    public ProbabilisticPolicy(HashMap<Integer, HashMap<Action, Double>> policy) {
        this.policy = policy;
    }

    private static int stateHash(Point[] state) {
        Point p1 = state[0];
        Point p2 = state[1];

        return  p1.x + p1.y * 10 + p2.x * 100 + p2.y * 1000;
    }

    @Override
    public Action getAction(State state) {
        Random r = new Random();
        Point[] currentState = new Point[]{state.getP1(), state.getP2()};
        HashMap<Action, Double> prob = policy.get(stateHash(currentState));
        double rand = r.nextDouble();
        double t = 0d;
        for (Entry<Action, Double> entry : prob.entrySet()) {
            if (rand < t + entry.getValue()) {
                return entry.getKey();
            }
            t += entry.getValue();
        }
        //if we get here, not good
        return new RandomPlayer(true).chooseAction(state);
    }

}
