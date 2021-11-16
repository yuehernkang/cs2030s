package cs2030.simulator;

import cs2030.simulator.ArrivalEvent;
import cs2030.simulator.Event;
import cs2030.simulator.EventComparator;
import cs2030.simulator.EventState;
import cs2030.simulator.CustomerType;
import cs2030.simulator.Simulator;
import cs2030.simulator.RandomGenerator;

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
        int loopIndex = 1;
        double trackTime = 0;
        double genFirstCustomerType = randomGenerator.genCustomerType();
        CustomerType firstCustomerType = genFirstCustomerType < probOfGreedyCustomer ? CustomerType.GREEDY : CustomerType.NORMAL;
        eventQueue.add(new ArrivalEvent(1, 0, 0, EventState.ARRIVAL, randomGenerator::genServiceTime, firstCustomerType));
        for (int i = 1; i < numOfCustomers; i++) {
            double time = randomGenerator.genInterArrivalTime();
            double determineCustomerType = randomGenerator.genCustomerType();
            CustomerType customerType = CustomerType.NORMAL;
            if (determineCustomerType < probOfGreedyCustomer) customerType = CustomerType.GREEDY;
            trackTime += time;
            eventQueue.add(new ArrivalEvent(loopIndex + 1, trackTime, 0, EventState.ARRIVAL, randomGenerator::genServiceTime, customerType));
            loopIndex++;
        }
        System.out.println(eventQueue);
        Simulator s = new Simulator(numOfServers, eventQueue, maxQueueLength, probOfResting, restTime, numOfSelfCheckoutCounters, randomGenerator);
        s.simulate();
        sc.close();
    }
}
