/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package policy;

import java.awt.Point;
import java.util.HashMap;
import java.util.Random;
import main.Action;
import main.State;

/**
 *
 * @author Kareem Horstink
 */
public class ProbablityDeterministicPolicy extends HashMap<State, Action> implements Policy {

    Random r = new Random();

   
    public ProbablityDeterministicPolicy() {
        super();
    }

    @Override
    public Action getAction(State state) {
        Point p1 = state.getP1();
        Point p2 = state.getP2();

        //If player is one has the ball
        //We are always player one
        if (state.getPossession() == State.FIRST_PLAYER) {
            /*Has the ball*/
            //Next to the end
            if (p1.x == State.MIN_X) {
                //Next to the goal
                if (p1.y == 1 || p1.y == 2) {
                    return Action.WEST;
                } else if (isBelow(p1, p2) && p1.y == 0) {
                    if (rand(0.5)) {
                        return Action.EAST;
                    } else {
                        return Action.STAND;
                    }
                } else if (isAbove(p1, p2) && p1.y == State.MAX_Y) {
                    if (rand(0.5)) {
                        return Action.EAST;
                    } else {
                        return Action.STAND;
                    }
                } else if (p1.y == State.MAX_Y) {
                    return Action.NORTH;
                } else {
                    return Action.SOUTH;
                }
            } else if (isLeft(p1, p2)) {
                if (rand(0.5)) {
                    return Action.STAND;
                } else if (rand(0.5)) {
                    return Action.NORTH;
                } else {
                    return Action.SOUTH;
                }
            } else if (rand(0.5)) {
                return Action.STAND;
            } else {
                return Action.WEST;
            }

        } else if (p1.x == State.MAX_X) {
            if (p1.y == 1) {
                if (rand(0.5)) {
                    return Action.SOUTH;
                } else {
                    return Action.STAND;
                }
            } else if (p1.y == 2) {
                if (rand(0.5)) {
                    return Action.NORTH;
                } else {
                    return Action.STAND;
                }
            } else if (p1.y == 0) {
                return Action.SOUTH;
            } else {
                return Action.NORTH;
            }
        } else if (isLeft(p1, p2) && rand(0.33333)) {
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

    private boolean rand(double limit) {
        return r.nextDouble() < limit ? Boolean.getBoolean("True") : Boolean.getBoolean("False");
    }

}
