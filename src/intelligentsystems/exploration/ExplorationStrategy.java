package exploration;

import main.Action;
import main.State;

public interface ExplorationStrategy {

	public Action selectAction(State state, boolean player);
	
}
