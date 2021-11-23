package cs2030.simulator;


import cs2030.simulator.RandomGenerator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;

public class Simulator {
    private final int numOfCustomers;
    private final int numOfServers;
    private final PriorityQueue<Event> eventQueue;
    private final int maxQueueLength;
    private final double probOfResting;
    private final double probOfGreedyCustomer;
    private final LinkedList<Double> restTimes;
    private final int numOfSelfCheckoutCounters;
    private final RandomGenerator randomGenerator;

    public Simulator(int numOfCustomers, int numOfServers, PriorityQueue<Event> eventQueue) {
        this.numOfCustomers = numOfCustomers;
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = 1;
        this.restTimes = new LinkedList<>();
        this.numOfSelfCheckoutCounters = 0;
        this.randomGenerator = new RandomGenerator(0, 0.0, 0.0, 0.0);
        this.probOfResting = -1;
        this.probOfGreedyCustomer = -1;
    }

    public Simulator(int numOfCustomers, int numOfServers,
                     PriorityQueue<Event> eventQueue, int maxQueueLength) {
        this.numOfCustomers = numOfCustomers;
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = new LinkedList<>();
        this.numOfSelfCheckoutCounters = 0;
        this.randomGenerator = new RandomGenerator(0, 0.0, 0.0, 0.0);
        this.probOfResting = -1.0;
        this.probOfGreedyCustomer = -1;

    }

    public Simulator(int numOfCustomers, int numOfServers, PriorityQueue<Event> eventQueue,
                     int maxQueueLength, LinkedList<Double> restTimes,
                     int numOfSelfCheckoutCounters) {
        this.numOfCustomers = numOfCustomers;
        this.numOfServers = numOfServers;
        this.eventQueue = eventQueue;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
        this.randomGenerator = new RandomGenerator(0, 0.0, 0.0, 0.0);
        this.probOfResting = 1;
        this.probOfGreedyCustomer = -1;

    }

    public Simulator(int numOfCustomers, int numOfServers, int maxQueueLength, double probOfResting,
                     double probOfGreedyCustomer, int numOfSelfCheckoutCounters, int seed,
                     double lambda, double mu, double rho) {
        this.numOfCustomers = numOfCustomers;
        this.numOfServers = numOfServers;
        this.eventQueue = new PriorityQueue<>();
        this.maxQueueLength = maxQueueLength;
        this.restTimes = new LinkedList<>();
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
        this.randomGenerator = new RandomGenerator(seed, lambda, mu, rho);
        this.probOfResting = probOfResting;
        this.probOfGreedyCustomer = probOfGreedyCustomer;
    }

    public void simulate() {
        List<Server> serverList;
        List<Double> statisticsHandler = initStatistics();
        serverList = initServer();
        PriorityQueue<Event> eventQueue1 = new PriorityQueue<>(new EventComparator());

        if (this.probOfGreedyCustomer != -1) {
            int loopIndex = 1;
            double trackTime = 0;
            double genFirstCustomerType = randomGenerator.genCustomerType();
            CustomerType firstCustomerType = genFirstCustomerType < probOfGreedyCustomer ?
                    CustomerType.GREEDY : CustomerType.NORMAL;
            eventQueue1.add(new ArrivalEvent(1, 0, 0, EventState.ARRIVAL,
                    randomGenerator::genServiceTime, firstCustomerType));
            for (int i = 1; i < numOfCustomers; i++) {
                double time = randomGenerator.genInterArrivalTime();
                double determineCustomerType = randomGenerator.genCustomerType();
                CustomerType customerType = CustomerType.NORMAL;
                if (determineCustomerType < probOfGreedyCustomer) {
                    customerType
                            = CustomerType.GREEDY;
                }
                trackTime += time;
                eventQueue1.add(new ArrivalEvent(loopIndex + 1, trackTime, 0,
                        EventState.ARRIVAL, randomGenerator::genServiceTime, customerType));
                loopIndex++;
            }
        } else {
            eventQueue1 = this.eventQueue;
        }
        while (!eventQueue1.isEmpty()) {
            Event event = eventQueue1.poll();
            System.out.println(event);
            EventHandler eventHandler = new EventHandler(event, serverList,
                    statisticsHandler, this.maxQueueLength,
                    this.restTimes, this.numOfSelfCheckoutCounters, this.randomGenerator,
                    this.probOfResting, this.probOfGreedyCustomer);
            Event latestEvent = eventHandler.handleEvent();
            if (latestEvent.getEventState() != EventState.FINISH) {
                eventQueue1.add(latestEvent);
            }
        }
        System.out.println("[" + String.format("%.3f", statisticsHandler.get(0) /
                statisticsHandler.get(1)) + " " + statisticsHandler.get(1).intValue()
                + " " + statisticsHandler.get(2).intValue() + "]");
    }

    private List<Double> initStatistics() {
        List<Double> statisticsHandler = new ArrayList<>();
        double initValue = 0.0;
        /*
        0 - waiting time
        1 - number of customers served
        2 - number of customers leave without serving
         */
        statisticsHandler.add(initValue);
        statisticsHandler.add(initValue);
        statisticsHandler.add(initValue);
        return statisticsHandler;
    }


    private List<Server> initServer() {
        List<Server> serverList = new ArrayList<>();
        //INITIALIZE SERVER LIST
        for (int i = 0; i < this.numOfServers; i++) {
            serverList.add(new Server(i + 1));
        }
        for (int j = 0; j < this.numOfSelfCheckoutCounters; j++) {
            serverList.add(new Server(serverList.size() + 1, ServerType.SELFCHECKOUT));
        }
        return serverList;
    }
}
