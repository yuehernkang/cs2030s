package cs2030.simulator;

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
        RandomGenerator randomGenerator = new RandomGenerator(seed, lambda, mu, rho);

        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());
        LinkedList<Double> restTime = new LinkedList<>();
        int loopIndex = 0;

        for (int i = 0; i < numOfCustomers; i++) {
            if (i == 0) {
                eventQueue.add(new Event(loopIndex + 1, 0, 0, EventState.ARRIVAL, () -> randomGenerator.genServiceTime()));
                loopIndex++;
            } else {
                eventQueue.add(new Event(loopIndex + 1, randomGenerator.genInterArrivalTime(), 0, EventState.ARRIVAL, () -> randomGenerator.genServiceTime()));
                loopIndex++;
            }
        }

        for (int i = 0; i < numOfCustomers; i++) {
            restTime.add(sc.nextDouble());
        }

//        Simulator s = new Simulator(numOfServers, eventQueue, maxQueueLength, restTime, numOfSelfCheckoutCounters);
//        s.simulate();
//        sc.close();
    }
}
