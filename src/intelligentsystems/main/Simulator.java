package main;

import java.awt.Point;

import player.Player;

public class Simulator {
	private Player p1, p2;
	private int goals1, goals2;

	public Simulator(Player p1, Player p2) {
		this.p1 = p1;
		this.p2 = p2;
		resetCounters();
	}

	public int getGoalsP1() {
		return this.goals1;
	}

	public int getGoalsP2() {
		return this.goals2;
	}

	public void setP1(Player p1) {
		this.p1 = p1;
	}

	public void setP2(Player p2) {
		this.p2 = p2;
	}

	private void resetCounters() {
		this.goals1 = 0;
		this.goals2 = 0;
	}

	public void simulate(int steps, double drawProbability) {
		resetCounters();

		State state = initializeRandomly();

		for (int i = 0; i < steps; i++) {
			Action a1 = this.p1.chooseAction(state);
			Action a2 = this.p2.chooseAction(state);
			if (Math.random() < 0.5) {
				state = performAction(state, a1, State.FIRST_PLAYER);
				state = performAction(state, a2, State.SECOND_PLAYER);
			} else {
				state = performAction(state, a2, State.SECOND_PLAYER);
				state = performAction(state, a1, State.FIRST_PLAYER);
			}

			Point ballPosition = state.getPossession() == State.FIRST_PLAYER ? state.getP1() : state.getP2();
			boolean resetBoard = false;
			double rewardP1 = 0.0, rewardP2 = 0.0;
			if (ballPosition.x == State.MIN_X - 1) { // goal for first player
				this.goals1++;
				resetBoard = true;
				rewardP1 = 1.0;
				rewardP2 = -1.0;
			} else if (ballPosition.x == State.MAX_X + 1) { // goal for second
															// player
				this.goals2++;
				resetBoard = true;
				rewardP1 = -1.0;
				rewardP2 = 1.0;
			} else if (Math.random() < drawProbability) { // draw
				resetBoard = true;
			}
			if (resetBoard) {
				state = initializeRandomly();
			}
			this.p1.receiveReward(rewardP1, state, a2);
			this.p2.receiveReward(rewardP2, state, a1);
		}

	}

	private State initializeRandomly() {
		return Math.random() < 0.5 ? State.getInitialState(State.FIRST_PLAYER)
				: State.getInitialState(State.SECOND_PLAYER);
	}

	private State performAction(State state, Action a, boolean player) {
		if (!state.isActionPossible(player, a)) {
			return state;
		}
		Point p1 = state.getP1();
		Point p2 = state.getP2();
		boolean possession = state.getPossession();
		if (player == State.FIRST_PLAYER) {
			Point newPos = targetPosition(p1, a);
			if (newPos.equals(p2)) {
				// possession goes to the stationary player
				// and move does not take place
				possession = State.SECOND_PLAYER;
			} else {
				p1 = newPos;
			}
		} else {
			Point newPos = targetPosition(p2, a);
			if (newPos.equals(p1)) {
				// possession goes to the stationary player
				// and move does not take place
				possession = State.FIRST_PLAYER;
			} else {
				p2 = newPos;
			}
		}
		return new State(p1, p2, possession);
	}

	private Point targetPosition(Point pos, Action a) {
		Point newPos = new Point(pos);
		switch (a) {
		case EAST:
			newPos.x += 1;
			break;
		case NORTH:
			newPos.y -= 1;
			break;
		case SOUTH:
			newPos.y += 1;
			break;
		case WEST:
			newPos.x -= 1;
			break;
		case STAND:
			break;
		default:
			break;
		}
		return newPos;
	}
}
