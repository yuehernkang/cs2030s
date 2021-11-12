package cs2030.simulator;

import java.util.*;

public class Simulator {
    private final int numOfServers;
    private final PriorityQueue<Event> eventQueue;
    private final int maxQueueLength;
    private final LinkedList<Double> restTimes;
    private final int numOfSelfCheckoutCounters;
    private final RandomGenerator randomGenerator;

    public Simulator(int numOfServers, PriorityQueue<Event> eventQueue) {
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = 1;
        this.restTimes = new LinkedList<>();
        this.numOfSelfCheckoutCounters = 0;
        this.randomGenerator = new RandomGenerator(0,0.0,0.0,0.0);
    }

    public Simulator(int numOfServers, PriorityQueue<Event> eventQueue, int maxQueueLength) {
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = new LinkedList<>();
        this.numOfSelfCheckoutCounters = 0;
        this.randomGenerator = new RandomGenerator(0,0.0,0.0,0.0);
    }

    public Simulator(int numOfServers, PriorityQueue<Event> eventQueue, int maxQueueLength, LinkedList<Double> restTimes, int numOfSelfCheckoutCounters) {
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
        this.randomGenerator = new RandomGenerator(0,0.0,0.0,0.0);
    }

    public Simulator(int numOfServers, PriorityQueue<Event> eventQueue, int maxQueueLength, LinkedList<Double> restTimes, int numOfSelfCheckoutCounters, RandomGenerator randomGenerator) {
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
        this.randomGenerator = randomGenerator;
    }

    public void simulate() {
        List<Server> serverList;
        List<Double> statisticsHandler = initStatistics();
        serverList = initServer();
        while (!this.eventQueue.isEmpty()) {
            Event event = this.eventQueue.poll();
            System.out.println(event);
            EventHandler eventHandler = new EventHandler(event, serverList, statisticsHandler, this.maxQueueLength, this.restTimes, this.numOfSelfCheckoutCounters, this.randomGenerator);
            Event latestEvent = eventHandler.handleEvent();
            if(latestEvent.getEventState() != EventState.FINISH){
                eventQueue.add(latestEvent);
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
        for(int j = 0; j < this.numOfSelfCheckoutCounters; j++){
            serverList.add(new Server(serverList.size() + 1, ServerType.SELFCHECKOUT));
        }
        return serverList;
    }
}
