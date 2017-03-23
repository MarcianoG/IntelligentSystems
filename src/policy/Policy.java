package policy;

import main.Action;
import main.State;

public interface Policy {
	Action getAction(State state);
}
