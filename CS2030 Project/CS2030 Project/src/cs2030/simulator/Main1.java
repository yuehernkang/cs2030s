//package cs2030.simulator;

import cs2030.simulator.EventComparator;
import cs2030.simulator.Event;
import cs2030.simulator.EventState;
import cs2030.simulator.Simulator;
import cs2030.simulator.Server;


import java.util.PriorityQueue;
import java.util.Scanner;

public class Main1 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int numOfServers = sc.nextInt();
        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());

        int loopIndex = 0;

        while (sc.hasNextDouble()) {
            eventQueue.add(new Event(loopIndex + 1, sc.nextDouble(), 0, EventState.ARRIVAL, 1));
            loopIndex++;
        }

        Simulator s = new Simulator(numOfServers, eventQueue);
        s.simulate();
        sc.close();
    }
}
