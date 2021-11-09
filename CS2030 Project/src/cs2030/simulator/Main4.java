package cs2030.simulator;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        //number of servers, the number of self-checkout counters, the maximum queue length and the number of customers N
        int numOfServers = sc.nextInt();
        int numOfSelfCheckoutCounters = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();

        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());
        LinkedList<Double> restTime = new LinkedList<>();

        int loopIndex = 0;

        for (int i = 0; i < numOfCustomers; i++) {
            eventQueue.add(new Event(loopIndex + 1, sc.nextDouble(), 0, EventState.ARRIVAL, sc.nextDouble()));
            loopIndex++;
        }

        for (int i = 0; i < numOfCustomers; i++) {
            restTime.add(sc.nextDouble());
        }

        Simulator s = new Simulator(numOfServers, eventQueue, maxQueueLength, restTime, numOfSelfCheckoutCounters);
        s.simulate();
        sc.close();
    }
}
