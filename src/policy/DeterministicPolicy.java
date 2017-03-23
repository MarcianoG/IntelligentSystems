package policy;

import java.util.HashMap;
import java.util.Map;

import main.Action;
import main.State;

public class DeterministicPolicy extends HashMap<State, Action> implements Policy {
	/** generated id for serialization */
	private static final long serialVersionUID = 291698286088712191L;

	public DeterministicPolicy(Map<State, Action> policy) {
		super(policy);
	}

	@Override
	public Action getAction(State state) {
		return super.get(state);
	}

}
