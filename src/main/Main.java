package main;

import exploration.RandomExploration;
import player.Player;
import player.PolicyPlayer;
import player.QLearningPlayer;
import policy.Policy;
import policy.ProbablityDeterministicPolicy;

public class Main {

    public static void main(String[] args) {
        reruns(50);
    }

    public static void given() {
        QLearningPlayer qPlayer = new QLearningPlayer(State.SECOND_PLAYER, 0.9, new RandomExploration());
        Player p2 = new PolicyPlayer(new ProbablityDeterministicPolicy());

        Simulator sim = new Simulator(p2, qPlayer);
        sim.simulate(1_000_000, 0.1);
        System.out.println("Training QR finished");

        Policy QR = qPlayer.getPolicy();
        sim.setP1(p2);
        sim.setP2(new PolicyPlayer(QR));
        sim.simulate(1_000_000, 0.1);
        System.out.println("Evaluation QR finished");
        System.out.println("Ours: " + sim.getGoalsP1());
        System.out.println("QR: " + sim.getGoalsP2());
        System.out.println("");
    }

    public static void reruns(int i) {
        QLearningPlayer qPlayer = new QLearningPlayer(State.SECOND_PLAYER, 0.9, new RandomExploration());
        Player p2 = new PolicyPlayer(new ProbablityDeterministicPolicy());//REPLACE THIS BIT

        Simulator sim = new Simulator(p2, qPlayer);
        sim.simulate(1_000_000, 0.1);
        System.out.println("Training QR finished");
        long s1 = 0;
        long s2 = 0;
        for (int j = 0; j < i; j++) {
            Policy QR = qPlayer.getPolicy();
            sim.setP1(p2);
            sim.setP2(new PolicyPlayer(QR));
            sim.simulate(1_000_000, 0.1);
            s1 += sim.getGoalsP1();
            s2 += +sim.getGoalsP2();
        }
        System.out.println("Ours : " + s1/(double)i);
        System.out.println("QR   : " + s2/(double)i);
    }
}
