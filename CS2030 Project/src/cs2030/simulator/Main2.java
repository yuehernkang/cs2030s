//package cs2030.simulator;

import cs2030.simulator.ArrivalEvent;
import cs2030.simulator.Event;
import cs2030.simulator.EventComparator;
import cs2030.simulator.EventState;
import cs2030.simulator.CustomerType;
import cs2030.simulator.Simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Scanner;


public class Main2 {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        int numOfServers = sc.nextInt();
        int maxQueueLength = sc.nextInt();
        sc.nextLine();
        PriorityQueue<Event> eventQueue = new PriorityQueue<>(new EventComparator());
        List<Double> arrivalTimes = new ArrayList<>();
        List<Double> serviceTimes = new ArrayList<>();

        int loopIndex = 0;

        while (sc.hasNextDouble()) {
            arrivalTimes.add(sc.nextDouble());
            serviceTimes.add(sc.nextDouble());
        }

        for (int i = 0; i < arrivalTimes.size(); i++) {
            int finalLoopIndex = loopIndex;
            eventQueue.add(new ArrivalEvent(loopIndex + 1, arrivalTimes.get(i),
                    0, EventState.ARRIVAL, () -> serviceTimes.get(finalLoopIndex),
                    CustomerType.NORMAL));
            loopIndex++;
        }

        Simulator s = new Simulator(arrivalTimes.size(), numOfServers, eventQueue, maxQueueLength);
        s.simulate();
        sc.close();

    }
}
