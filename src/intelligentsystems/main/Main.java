package main;

import exploration.RandomExploration;
import player.Player;
import player.PolicyPlayer;
import player.QLearningPlayer;
import player.RandomPlayer;
import policy.Policy;

public class Main {

	public static void main(String[] args) {
		QLearningPlayer qPlayer = new QLearningPlayer(State.FIRST_PLAYER, 0.9, new RandomExploration());
		Player p2 = new RandomPlayer(State.SECOND_PLAYER);

		Simulator sim = new Simulator(qPlayer, p2);
		sim.simulate(1_000_000, 0.1);
		System.out.println("Training QR finished");

		Policy QR = qPlayer.getPolicy();
		sim.setP1(new PolicyPlayer(QR));
		sim.simulate(1_000_000, 0.1);
		System.out.println("Evaluation QR finished");
		System.out.println(sim.getGoalsP1());
		System.out.println(sim.getGoalsP2());
		System.out.println("");
	}
}
