package player;

import java.util.Map;

import exploration.ExplorationStrategy;
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

	private Map<Triple<State, Action, Action>, Double> qValues;
	private Map<State, double[]> pi;

	public MinimaxQPlayer(boolean player, double discountFactor, double decay, ExplorationStrategy es) {
		this.player = player;
	}

	@Override
	public Action chooseAction(State state) {
		return new RandomPlayer(this.player).chooseAction(state);
	}

	@Override
	public void receiveReward(double reward, State newState, Action opponentAction) {

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
				arr2[i + 1] = -this.qValues
						.get(new Triple<State, Action, Action>(this.s, allActions[i], allActions[j]));
			}
			lp.addConstraint(new LinearSmallerThanEqualsConstraint(arr2, 0, "c" + (i + 1)));
		}
		lp.setMinProblem(false);

		LinearProgramSolver solver = SolverFactory.newDefault();
		double[] sol = solver.solve(lp);
		double[] newPi = new double[allActions.length];
		System.arraycopy(sol, 1, newPi, 0, newPi.length);
		this.pi.put(this.s, newPi);
	}

	public Policy getPolicy() {
		return new ProbabilisticPolicy(this.pi);
	}

}
