package player;

import main.Action;
import main.State;

public interface Player {
	
	public Action chooseAction(State state);
	
	public void receiveReward(double reward, State newState, Action opponentAction);

}
