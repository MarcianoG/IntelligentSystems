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
        int hash = 0;
        int i = 0;
        for (Point p : state) {
            hash += (i += 77) * Math.pow(p.getX(), 3) * Math.pow(p.getY(), 3);
        }
        return hash;
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
