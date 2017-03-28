package main;

import exploration.RandomExploration;
import player.MinimaxQPlayer;
import player.Player;
import player.PolicyPlayer;
import player.QLearningPlayer;
import player.RandomPlayer;
import policy.DeterministicPolicy;
import policy.Policy;
import policy.ProbablityDeterministicPolicy;

public class Main {

    public static void main(String[] args) {
//        reruns(50);
        given();
    }

    public static void given() {
        QLearningPlayer qPlayer2 = new QLearningPlayer(State.SECOND_PLAYER, 0.9, new RandomExploration());
                MinimaxQPlayer qPlayer = new MinimaxQPlayer(State.FIRST_PLAYER, 0.9,1, null);

        Player policyPlayer = new PolicyPlayer(new ProbablityDeterministicPolicy());//REPLACE THIS BIT

        Simulator sim = new Simulator(qPlayer, qPlayer2);
        sim.simulate(100_000, 0.1);
        System.out.println("Training QR finished");

        Policy QR = qPlayer.getPolicy();
        sim.setP2(new PolicyPlayer(qPlayer2.getPolicy()));
        sim.setP1(new PolicyPlayer(QR));
        sim.simulate(1_000_000, 0.1);
        System.out.println("Evaluation QR finished");
        System.out.println("p1: " + sim.getGoalsP1());
        System.out.println("p2: " + sim.getGoalsP2());
        System.out.println("");
    }

    public static void reruns(int i) {
//        QLearningPlayer qPlayer = new QLearningPlayer(State.FIRST_PLAYER, 0.9, new RandomExploration());
        MinimaxQPlayer qPlayer = new MinimaxQPlayer(State.FIRST_PLAYER, 0.9,1, null);
        Player policyPlayer = new RandomPlayer(State.SECOND_PLAYER);//REPLACE THIS BIT

        System.out.println("Training QR finished");
        long s1 = 0;
        long s2 = 0;
        for (int j = 0; j < i; j++) {
            Simulator sim = new Simulator(qPlayer, policyPlayer);
            sim.simulate(1_000_000, 0.1);

            Policy QR = qPlayer.getPolicy();
            sim.setP1(new PolicyPlayer(QR));
            sim.setP2(policyPlayer);
            sim.simulate(1_000_000, 0.1);
            s1 += sim.getGoalsP1();
            s2 += +sim.getGoalsP2();
        }
        System.out.println("P1 : " + s1 / (double) i);
        System.out.println("P2   : " + s2 / (double) i);
    }
}
