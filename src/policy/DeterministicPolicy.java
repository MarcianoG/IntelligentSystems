package policy;

import java.awt.Point;
import java.util.HashMap;
import java.util.Map;

import main.Action;
import main.State;

public class DeterministicPolicy extends HashMap<State, Action> implements Policy {

    /**
     * generated id for serialization
     */
    private static final long serialVersionUID = 291698286088712191L;

    public DeterministicPolicy(Map<State, Action> policy) {
        super(policy);
    }

    @Override
    public Action getAction(State state) {
        Point P1 = state.getP1();
        Point P2 = state.getP2();
        if(state.getPossession() == State.FIRST_PLAYER){
            
        } else {
            if(P1.x == )
        }
        return super.get(state);
    }

}
