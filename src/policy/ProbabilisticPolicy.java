package policy;

import java.util.HashMap;
import java.util.Map;

import main.Action;
import main.State;

public class ProbabilisticPolicy extends HashMap<State, double[]> implements Policy {
	/** generated id */
	private static final long serialVersionUID = -7901905496277036579L;

	public ProbabilisticPolicy(Map<State, double[]> policy) {
		super(policy);
	}

	@Override
	public Action getAction(State state) {
		Action[] allActions = Action.values();
		double[] prob = super.get(state);

		double sum = 0.0;
		double r = Math.random();

		for (int i = 0; i < prob.length; i++) {
			sum = sum + prob[i];
			if (r <= sum) {
				return allActions[i];
			}
		}
		return allActions[allActions.length - 1];
	}

}
