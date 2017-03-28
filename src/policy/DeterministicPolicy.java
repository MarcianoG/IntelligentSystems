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

    public DeterministicPolicy() {
        super();
    }

    @Override
    public Action getAction(State state) {
        Point p1 = state.getP1();
        Point p2 = state.getP2();

        //If player is one has the ball
        //We are always player two
        if (state.getPossession() == State.SECOND_PLAYER) {
            /*Has the ball*/
            //Next to the end
            if (p1.x == State.MAX_X) {
                //Next to the goal
                if (p1.y == 1 || p1.y == 2) {
                    return Action.EAST;
                } else if (isBelow(p1, p2) && p1.y == 0) {
                    return Action.STAND;
                } else if (isAbove(p1, p2) && p1.y == State.MAX_Y) {
                   
                        return Action.EAST;
                } else if (p1.y == State.MAX_Y) {
                    return Action.NORTH;
                } else {
                    return Action.SOUTH;
                }
            } else if (isLeft(p1, p2)) {
                    return Action.STAND;
            } else {
                return Action.EAST;
            }

        } else if (p1.x == State.MIN_X) {
            if (p1.y == 1) {
              
                    return Action.SOUTH;
              
            } else if (p1.y == 2) {
              
                    return Action.NORTH;
               
            } else if (p1.y == 0) {
                return Action.SOUTH;
            } else {
                return Action.NORTH;
            }
        } else if (isLeft(p1, p2)) {
            return Action.STAND;
        } else {
            return Action.EAST;
        }
    }

    private boolean isAbove(Point p1, Point p2) {
        return p2.y == p1.y + 1;
    }

    private boolean isLeft(Point p1, Point p2) {
        return p2.x == p1.x - 1;
    }

    private boolean isRight(Point p1, Point p2) {
        return p2.x == p1.x + 1;
    }

    private boolean isBelow(Point p1, Point p2) {
        return p2.y == p1.y - 1;
    }

}
