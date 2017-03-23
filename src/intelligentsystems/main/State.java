package main;

import java.awt.Point;

public class State {
	public static final boolean FIRST_PLAYER = true;
	public static final boolean SECOND_PLAYER = false;

	public static final int MIN_X = 0, MAX_X = 4;
	public static final int MIN_Y = 0, MAX_Y = 3;

	/** p1: position of the first player */
	private Point p1;
	/** p2: position of the second player */
	private Point p2;
	/** indicates which player is in possession of the ball */
	private boolean possession;

	public State(Point p1, Point p2, boolean possession) {
		this.p1 = new Point(p1);
		this.p2 = new Point(p2);
		this.possession = possession;
	}

	public State(State s) {
		this(s.p1, s.p2, s.possession);
	}

	public static State getInitialState(boolean possession) {
		return new State(new Point(3, 1), new Point(1, 2), possession);
	}

	public boolean getPossession() {
		return this.possession;
	}

	public Point getP1() {
		return new Point(this.p1);
	}

	public Point getP2() {
		return new Point(this.p2);
	}

	@Override
	public boolean equals(Object obj) {
		try {
			return this.equals((State) obj);
		} catch (ClassCastException e) {
			return false;
		}
	}

	public boolean equals(State s) {
		return this.p1.equals(s.p1) && this.p2.equals(s.p2) && this.possession == s.possession;
	}

	/**
	 * This way of implementing hash function is inspired by Josh Bloch's book
	 * 'Effective Java'
	 */
	@Override
	public int hashCode() {
		int hash = 17;
		hash = hash * 31 + this.p1.hashCode();
		hash = hash * 31 + this.p2.hashCode();
		hash += this.possession ? 1 : 0;
		return hash;
	}

	public boolean isActionPossible(boolean player, Action a) {
		Point pos = player == State.FIRST_PLAYER ? this.p1 : this.p2;
		if (!isInsideField(pos) && !isInsideGoal(pos)) {
			return false;
		} else {
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
			return isInsideField(newPos) || isInsideGoal(newPos);
		}
	}

	private boolean isInsideField(Point pos) {
		return pos.x >= State.MIN_X && pos.x <= State.MAX_X && pos.y >= State.MIN_Y && pos.y <= State.MAX_Y;
	}

	public boolean isInsideGoal(Point pos) {
		return (pos.x == State.MIN_X - 1 || pos.x == State.MAX_X + 1) && (pos.y == 1 || pos.y == 2);
	}

	public double observeReward(boolean player) {
		if (player == State.FIRST_PLAYER) {
			return (p1.x == State.MAX_X + 1 && (p1.y == 1 || p1.y == 2)) ? 1.0 : 0.0;
		} else { // second player
			return (p2.x == State.MIN_X - 1 && (p2.y == 1 || p2.y == 2)) ? 1.0 : 0.0;
		}
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int y = State.MIN_Y - 1; y <= State.MAX_Y + 1; y++) {
			for (int x = State.MIN_X - 1; x <= State.MAX_X + 1; x++) {
				Point p = new Point(x, y);
				if (this.p1.equals(p)) {
					if (possession == State.FIRST_PLAYER) {
						sb.append('A');
					} else {
						sb.append('a');
					}
				} else if (this.p2.equals(p)) {
					if (possession == State.FIRST_PLAYER) {
						sb.append('b');
					} else {
						sb.append('B');
					}
				} else if ((p.x == State.MIN_X - 1 || p.x == State.MAX_X + 1) && (p.y == 1 || p.y == 2)) {
					sb.append('X');
				} else {
					sb.append(' ');
				}
			}
			sb.append(System.getProperty("line.separator"));
		}
		return sb.toString();
	}
}
