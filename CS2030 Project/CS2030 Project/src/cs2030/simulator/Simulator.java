package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Arrays;

public class Simulator {
    private final int numOfServers;
    private final PriorityQueue<Event> eventQueue;
    private final int maxQueueLength;

    public Simulator(int numOfServers, PriorityQueue<Event> eventQueue) {
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = 1;
    }

    public Simulator(int numOfServers, PriorityQueue<Event> eventQueue, int maxQueueLength) {
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = maxQueueLength;
    }

    public void simulate() {
        List<Server> serverList;
        List<Double> statisticsHandler = initStatistics();
        serverList = initServer();
        while (!this.eventQueue.isEmpty()) {
            Event event = this.eventQueue.poll();
            System.out.println(event);
            EventHandler eventHandler = new EventHandler(event, serverList, statisticsHandler, this.maxQueueLength);
            List<Event> latestEvent = eventHandler.handleEvent();
            for (Event e : latestEvent) {
                if (e != null) {
                    eventQueue.add(e);
                }
            }
        }
        System.out.println("[" + String.format("%.3f", statisticsHandler.get(0) / statisticsHandler.get(1)) + " " + statisticsHandler.get(1).intValue()
                + " " + statisticsHandler.get(2).intValue() + "]");
    }

     private List<Double> initStatistics() {
        List<Double> statisticsHandler = new ArrayList<>();
        /*
        0 - waiting time
        1 - number of customers served
        2 - number of customers leave without serving
         */
        statisticsHandler.add(0.0);
        statisticsHandler.add(0.0);
        statisticsHandler.add(0.0);
        return statisticsHandler;
    }

    private List<Server> initServer() {
        List<Server> serverList = new ArrayList<>();
        //INITIALIZE SERVER LIST
        for (int i = 0; i < this.numOfServers; i++) {
            serverList.add(new Server(i + 1));
        }
        return serverList;
    }
}
