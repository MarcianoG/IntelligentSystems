package player;

import java.awt.Point;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

import exploration.ExplorationStrategy;
import main.Action;
import main.State;
import main.Tuple;
import policy.DeterministicPolicy;

public class QLearningPlayer implements Player {
	private boolean player;
	private State s;
	private Action a;
	private double discountFactor;
	private ExplorationStrategy es;

	private Map<Tuple<State, Action>, Double> qValues;

	public QLearningPlayer(boolean player, double discountFactor, ExplorationStrategy es) {
		this.player = player;
		this.s = null;
		this.a = null;
		this.discountFactor = discountFactor;
		this.es = es;
		initialize();
	}

	private void initialize() {
		this.qValues = new HashMap<Tuple<State, Action>, Double>();
		for (int x1 = State.MIN_X - 1; x1 <= State.MAX_X + 1; x1++) {
			for (int y1 = State.MIN_Y - 1; y1 <= State.MAX_Y + 1; y1++) {
				for (int x2 = State.MIN_X - 1; x2 <= State.MAX_X + 1; x2++) {
					for (int y2 = State.MIN_Y - 1; y2 <= State.MAX_Y + 1; y2++) {
						Point p1 = new Point(x1, y1);
						Point p2 = new Point(x2, y2);
						State s1 = new State(p1, p2, true);
						State s2 = new State(p1, p2, false);
						for (Action a : Action.values()) {
							this.qValues.put(new Tuple<State, Action>(s1, a), 0.0);
							this.qValues.put(new Tuple<State, Action>(s2, a), 0.0);
						}
					}
				}
			}
		}
	}

	@Override
	public Action chooseAction(State state) {
		this.s = state;
		this.a = this.es.selectAction(state, this.player);
		return this.a;
	}

	@Override
	public void receiveReward(double reward, State newState, Action opponentAction) {
		double q = calculateNewQValue(newState, reward, this.qValues, this.discountFactor);
		this.qValues.put(new Tuple<State, Action>(this.s, this.a), q);
	}

	private static double calculateNewQValue(State t, double reward, Map<Tuple<State, Action>, Double> qValues,
			double discountFactor) {
		double max = -Double.MAX_VALUE;
		for (Action a : Action.values()) {
			double q = qValues.get(new Tuple<State, Action>(t, a));
			if (q > max) {
				max = q;
			}
		}
		return reward + discountFactor * max;
	}

	public DeterministicPolicy getPolicy() {
		Map<State, Action> policy = new HashMap<State, Action>();
		HashSet<State> allStates = new HashSet<State>();
		for (Tuple<State, Action> sa : this.qValues.keySet()) {
			allStates.add(sa.getS());
		}
		for (State s : allStates) {
			policy.put(s, bestAction(s));
		}
		return new DeterministicPolicy(policy);
	}

	private Action bestAction(State state) {
		Action action = null;
		double max = -Double.MAX_VALUE;
		for (Action a : Action.values()) {
			Tuple<State, Action> sa = new Tuple<State, Action>(state, a);
			double q = this.qValues.get(sa);
			if (q > max) {
				max = q;
				action = sa.getT();
			}
		}
		return action;
	}

}
