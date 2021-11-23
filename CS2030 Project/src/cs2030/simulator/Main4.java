//package cs2030.simulator;

import cs2030.simulator.ArrivalEvent;
import cs2030.simulator.Event;
import cs2030.simulator.EventComparator;
import cs2030.simulator.EventState;
import cs2030.simulator.CustomerType;
import cs2030.simulator.Simulator;

import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;
import java.util.ArrayList;

public class Main4 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int numOfSelfCheckoutCounters = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        int numOfCustomers = sc.nextInt();

        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());
        LinkedList<Double> restTime = new LinkedList<>();
        List<Double> arrivalTimes = new ArrayList<>();
        List<Double> serviceTimes = new ArrayList<>();

        int loopIndex = 0;

        for (int i = 0; i < numOfCustomers; i++) {
            arrivalTimes.add(sc.nextDouble());
            serviceTimes.add(sc.nextDouble());
        }

        for (int i = 0; i < numOfCustomers; i++) {
            int finalLoopIndex = loopIndex;
            eventQueue.add(new ArrivalEvent(i + 1, arrivalTimes.get(i), 0,
                    EventState.ARRIVAL, () -> serviceTimes.get(finalLoopIndex),
                    CustomerType.NORMAL));
            loopIndex++;
        }

        for (int i = 0; i < numOfCustomers; i++) {
            restTime.add(sc.nextDouble());
        }

        Simulator s = new Simulator(numOfCustomers, numOfServers, eventQueue, maxQueueLength,
                restTime, numOfSelfCheckoutCounters);
        s.simulate();
        sc.close();
    }
}
