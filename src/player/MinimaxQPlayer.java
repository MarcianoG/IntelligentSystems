package player;

import java.util.Map;

import exploration.ExplorationStrategy;
import java.awt.Point;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;
import main.Action;
import main.State;
import main.Triple;
import policy.Policy;
import policy.ProbabilisticPolicy;
import scpsolver.constraints.LinearBiggerThanEqualsConstraint;
import scpsolver.constraints.LinearEqualsConstraint;
import scpsolver.constraints.LinearSmallerThanEqualsConstraint;
import scpsolver.lpsolver.LinearProgramSolver;
import scpsolver.lpsolver.SolverFactory;
import scpsolver.problems.LinearProgram;

public class MinimaxQPlayer implements Player {

    private boolean player;
    private State s;
    private Point[] currentState;
    private Action currentAction;
    private double alpha;
    private double discountFactor;
    private double decay;
    private Map<Triple<Integer, Action, Action>, Double> qValues = new HashMap();
    private Map<Integer, Double> values = new HashMap();
    //private Map<Point[], double[]> pi;
    //private List<Point[]> allStates = new ArrayList();                      
    HashMap<Integer, HashMap<Action, Double>> pi = new HashMap(); //PI

    public MinimaxQPlayer(boolean player, double discountFactor, double decay, ExplorationStrategy es) {
        this.player = player;
        this.discountFactor = discountFactor;
        this.decay = decay;//All the possible states
        for (int x1 = 0; x1 < State.MAX_X; x1++) {
            for (int y1 = 0; y1 < State.MAX_Y; y1++) {
                for (int x2 = 0; x2 < State.MAX_X; x2++) {
                    for (int y2 = 0; y2 < State.MAX_Y; y2++) {
                        if (x1 == x2 && y2 == y1) {
                            continue;
                        }
                        Point[] state = new Point[]{new Point(x1, y1), new Point(x2, y2)};

                        values.put(stateHash(state), 1d);
                        HashMap<Action, Double> actions = new HashMap();
                        for (Action a : Action.values()) {
                            actions.put(a, 1d / 5.0);
                            for (Action aa : Action.values()) {
                                qValues.put(new Triple(stateHash(state), a, aa), 1d);
                            }
                        }
                        pi.put(stateHash(state), actions);

                    }
                }
            }

            alpha = 1d;
        }
    }

    private static int stateHash(Point[] state) {
        int hash = 0;
        int i = 0;
        for (Point p : state) {
            hash += (i += 77) * Math.pow(p.getX(), 3) * Math.pow(p.getY(), 3);
        }
        return hash;
    }

    private static class Pair<A, B> {

        public A a;
        public B b;

        public Pair(A a, B b) {
            this.a = a;
            this.b = b;
        }
    }

    Random r = new Random();

    @Override
    public Action chooseAction(State state) {
        s = state;
        currentState = new Point[]{state.getP1(), state.getP2()};
        if (r.nextDouble() > 0.99) {
            currentAction = new RandomPlayer(this.player).chooseAction(state);
            return currentAction;
        }

        HashMap<Action, Double> prob = pi.get(stateHash(currentState));
        double rand = r.nextDouble();
        double t = 0d;
        for (Entry<Action, Double> entry : prob.entrySet()) {
            if (rand < t + entry.getValue()) {
                currentAction = entry.getKey();
                return currentAction;
            }
            t += entry.getValue();
        }
        //if we get here, not good
        currentAction = new RandomPlayer(this.player).chooseAction(state);
        return currentAction;
    }

    @Override
    public void receiveReward(double reward, State newState, Action opponentAction) {
        //Q[s,a,o] := (1-alpha) * Q[s,a,o] + alpha * (rew + gamma * V[s’])
        Point[] s2 = new Point[]{newState.getP1(), newState.getP2()};

        double currentQ = qValues.get(new Triple(stateHash(currentState), currentAction, opponentAction));
        double newQ = 1d - alpha * currentQ + alpha * (reward + discountFactor + values.get(stateHash(s2)));
        qValues.put(new Triple(stateHash(currentState), currentAction, opponentAction), newQ);
        learn(newState, opponentAction);
    }

    public void learn(State newState, Action opponentAction) {
        Point[] s2 = new Point[]{newState.getP1(), newState.getP2()};
        linearProgramming();
        double min = Double.MIN_VALUE;//Fix me pplease
        for (Action o : Action.values()) {
            double sum = 0;
            //a'
            for (Action a : Action.values()) {
                sum += pi.get(stateHash(s2)).get(a) * qValues.get(new Triple(s2, a, o));
            }
            if (min > sum) {
                min = sum;
            }
        }

        values.put(stateHash(s2), min);
        alpha *= decay;
    }

    public void linearProgramming() {
        Action[] allActions = Action.values();

        // maximize slack variable (index 0), which represents the value of the
        // inner minimization
        double[] maxObjective = new double[allActions.length + 1];
        maxObjective[0] = 1.0;
        LinearProgram lp = new LinearProgram(maxObjective);

        // all probabilities must add up to 1
        double[] sum1 = new double[allActions.length + 1];
        for (int i = 1; i < sum1.length; i++) {
            sum1[i] = 1.0;
        }
        lp.addConstraint(new LinearEqualsConstraint(sum1, 1.0, "a"));

        for (int i = 0; i < allActions.length; i++) {
            // all probabilities must be positive
            double[] arr1 = new double[maxObjective.length];
            arr1[i + 1] = 1.0;
            lp.addConstraint(new LinearBiggerThanEqualsConstraint(arr1, 0, "b" + (i + 1)));

            // v <= sum( p(s, action_i) * Q(s,action_i, action_opponent) )
            // v - sum( p(s, action_i) * Q(s,action_i, action_opponent) ) <= 0
            double[] arr2 = new double[maxObjective.length];
            arr2[0] = 1.0;
            for (int j = 0; j < allActions.length; j++) {
                arr2[i + 1] = -this.qValues.get(new Triple<>(stateHash(currentState), allActions[i], allActions[j]));
            }
            lp.addConstraint(new LinearSmallerThanEqualsConstraint(arr2, 0, "c" + (i + 1)));
        }
        lp.setMinProblem(false);

        LinearProgramSolver solver = SolverFactory.newDefault();
        double[] sol = solver.solve(lp);
        double[] newPi = new double[allActions.length];

        HashMap<Action, Double> actions = pi.get(stateHash(currentState));
        int i = 0;
        for (Action key : actions.keySet()) {
            actions.put(key, newPi[i++]);
        }
        pi.put(stateHash(currentState), actions);
//         System.arraycopy(sol, 1, newPi, 0, newPi.length);
//         this.pi.put(currentState,(allActions ,newPi));
    }

    public Policy getPolicy() {
        return new ProbabilisticPolicy(this.pi);
    }

}
