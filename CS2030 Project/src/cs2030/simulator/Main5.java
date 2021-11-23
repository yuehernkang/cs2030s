//package cs2030.simulator;


import cs2030.simulator.CustomerType;
import cs2030.simulator.EventComparator;
import cs2030.simulator.Simulator;
import cs2030.simulator.ArrivalEvent;
import cs2030.simulator.Event;
import cs2030.simulator.EventState;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main5 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int seed = sc.nextInt();
        int numOfServers = sc.nextInt();
        int numOfSelfCheckoutCounters = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();
        double lambda = sc.nextDouble();
        double mu = sc.nextDouble();
        double rho = sc.nextDouble();
        double probOfResting = sc.nextDouble();
        double probOfGreedyCustomer = sc.nextDouble();

        sc.nextLine();

        Simulator s = new Simulator(numOfCustomers, numOfServers, maxQueueLength, probOfResting,
                probOfGreedyCustomer, numOfSelfCheckoutCounters, seed, lambda, mu, rho);
        s.simulate();
        sc.close();
    }
}
