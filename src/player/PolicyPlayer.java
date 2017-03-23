package player;

import main.Action;
import main.State;
import policy.Policy;

public class PolicyPlayer implements Player {
	private Policy policy;

	public PolicyPlayer(Policy policy) {
		this.policy = policy;
	}

	@Override
	public Action chooseAction(State state) {
		return this.policy.getAction(state);
	}

	@Override
	public void receiveReward(double reward, State newState, Action opponentAction) {
		// ignore any rewards
	}

}
