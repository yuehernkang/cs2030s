//package cs2030.simulator;

import cs2030.simulator.Event;
import cs2030.simulator.EventComparator;
import cs2030.simulator.EventState;
import cs2030.simulator.Simulator;

import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

public class Main3 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();

        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());
        ArrayList<Double> restTime = new ArrayList<>();

        int loopIndex = 0;

        for (int i = 0; i < numOfCustomers; i++) {
            eventQueue.add(new Event(loopIndex + 1, sc.nextDouble(), 0, EventState.ARRIVAL, sc.nextDouble()));
            loopIndex++;
        }

        for (int i = 0; i < numOfCustomers; i++) {
            restTime.add(sc.nextDouble());
        }

        Simulator s = new Simulator(numOfServers, eventQueue, maxQueueLength, restTime);
        s.simulate();
        sc.close();
    }

}
