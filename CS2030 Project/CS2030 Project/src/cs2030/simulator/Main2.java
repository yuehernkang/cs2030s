package cs2030.simulator;

//import cs2030.simulator.Simulator;

import java.util.*;

public class Main2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());

        int loopIndex = 0;

        while (sc.hasNextDouble()) {
            eventQueue.add(new Event(loopIndex + 1, sc.nextDouble(), 0, EventState.ARRIVAL, sc.nextDouble()));
            loopIndex++;
        }

        Simulator s = new Simulator(numOfServers, eventQueue, maxQueueLength);
        s.simulate();
        sc.close();

    }
}
