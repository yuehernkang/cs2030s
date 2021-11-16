package cs2030.simulator;

import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

public class EventHandler {
    private final Event event;
    private final List<Server> serverList;
    private final List<Double> statisticsHandler;
    private final int maxQueueLength;
    private final LinkedList<Double> restTimes;
    private final int numOfSelfCheckoutCounters;
    private final RandomGenerator randomGenerator;
    private final double probOfResting;

    public EventHandler(Event event,
                        List<Server> serverList,
                        List<Double> statisticsHandler,
                        int maxQueueLength, LinkedList<Double> restTimes,
                        int numOfSelfCheckoutCounters, RandomGenerator randomGenerator, double probOfResting) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
        this.randomGenerator = randomGenerator;
        this.probOfResting = probOfResting;
    }

    Event handleEvent() {
        Event e = new Event();
        if (event.getEventState() == EventState.ARRIVAL) e = handleArrivalEvent();
        else if (event.getEventState() == EventState.SERVE || event.getEventState() == EventState.SERVEBYSELFCHECKOUT)
            e = handleServeEvent();
        else if (event.getEventState() == EventState.DONE || event.getEventState() == EventState.DONESELFCHECKOUT)
            e = handleDoneEvent();
        return e;
    }

    Event handleDoneEvent() {
        /**
         * Give server specified rest time
         * Release Server
         * Create new serve event for waiting customer at current time
         * @return the next event
         */
        int serverIndex = event.getServerId() - 1;
        double restTime = 0;
        Event e1 = this.serverList.get(serverIndex).getQueue().peek();
        ServerType serverType = this.serverList.get(serverIndex).getServerType();
        Event e = new Event();

        switch (serverType) {
            case SELFCHECKOUT:
                for (Server s : this.serverList) {
                    if (s.getServerType() == ServerType.SELFCHECKOUT && !s.queueIsEmpty()) {
                        if (findEventInQueue(event.getId(), s)) {
                            s.getQueue().poll();
                        }
                        Server s1 = this.serverList.get(serverIndex);
                        double serviceTime = s.getQueue().peek().getServiceTime();
                        s1 = new Server(s1.getId(), ServerState.SERVING, s1.getServerType(), s1.getQueue(), s1.getNextAvailableTime() + serviceTime);
                        e = new ServeBySelfCheckEvent(s.getQueue().peek().getId(), event.getTime(), s1.getId(), EventState.SERVEBYSELFCHECKOUT, () -> serviceTime, event.getCache(), event.getCustomerType());
                        this.serverList.set(serverIndex, s1);
                        break;
                    } else {
                        e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime, event.getCustomerType());
                        this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(0));
                    }
                }
                break;
            case HUMAN:
                boolean serverRest = this.randomGenerator.genRandomRest() < probOfResting;
                //There is people waiting for this server
                if (!this.restTimes.isEmpty()) restTime = this.restTimes.peek();
                if (this.probOfResting != 1) restTime = this.randomGenerator.genRestPeriod();
                if (serverRest) {
                    //SERVER CAN REST
                    this.restTimes.poll();
                    this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(restTime));
                    if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                        e = new ServeEvent(e1.getId(), event.getTime() + restTime, e1.getServerId(), EventState.SERVE, e1::getServiceTime, event.getCache(), event.getCustomerType());
                    } else {
                        //No people waiting for this server
                        e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime, event.getCustomerType());
                    }
                } else {
                    //SERVER CANNOT REST
                    if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                        Event tempEvent = this.serverList.get(serverIndex).getQueue().poll();
                        e = new ServeEvent(tempEvent.getId(), event.getTime(), tempEvent.getServerId(), EventState.SERVE, tempEvent::getServiceTime, tempEvent.getCache(), tempEvent.getCustomerType());
                    } else {
                        //No people waiting for this server
                        this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(0));
                        e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime, event.getCustomerType());
                    }
                }
                break;
            default:
                e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime, event.getCustomerType());
        }
        return e;
    }

    /**
     * Handles the serve event and returns the next event following this serve event
     * Checks if the queue of the server is empty and returns a serve event if there is a customer
     * in the queue
     *
     * @return the next event, finish event if there are no events in the queue, serve event if there are events in the queue
     */
    Event handleServeEvent() {
        //Increment number of customers served
        statisticsHandler.set(1, statisticsHandler.get(1) + 1);
        LinkedList<Event> q;
        Server s;
        int serverIndex = event.getServerId() - 1;
        ServerType st = this.serverList.get(serverIndex).getServerType();
        EventState es = st == ServerType.HUMAN ? EventState.DONE : EventState.DONESELFCHECKOUT;
        Event e = new Event();
        if (st == ServerType.HUMAN) {
            e = new DoneEvent(event.getId(), event.getServiceCompletionTime(), event.getServerId(), es, event::getServiceTime, e.getCache(), event.getCustomerType());
        }
        if (st == ServerType.SELFCHECKOUT) {
            e = new DoneSelfCheckEvent(event.getId(), event.getServiceCompletionTime(), event.getServerId(), es, event::getServiceTime, e.getCache(), event.getCustomerType());
        }
        AtomicReference<Double> waitingTime = new AtomicReference<>((double) 0);

        //If there was someone in the queue
        switch (st) {
            case HUMAN: {
                if (!this.serverList.get(serverIndex).queueIsEmpty()) {
//                    this.serverList.get(serverIndex).getQueue().stream().filter(x -> x.getId() == event.getId()).forEach((y) -> {
//                        waitingTime.set(event.getTime() - y.getTime());
//                        this.serverList.get(serverIndex).getQueue().poll();
//                    });
                    for (int i = 0; i < this.serverList.get(serverIndex).getQueue().size(); i++) {
                        if (event.getId() == this.serverList.get(serverIndex).getQueue().get(i).getId()) {
                            waitingTime.set(event.getTime() - this.serverList.get(serverIndex).getQueue().peek().getTime());
                            this.serverList.get(serverIndex).getQueue().poll();
                        }
                    }
                    q = this.serverList.get(serverIndex).getQueue();
                    s = new Server(event.getServerId(), ServerState.SERVING, this.serverList.get(serverIndex).getServerType(), q, event.getTime() + event.getServiceTime());
                    serverList.set(serverIndex, s);
                }
            }
            case SELFCHECKOUT: {
                for (Server s1 : this.serverList) {
                    if (s1.getServerType() == ServerType.SELFCHECKOUT) {
                        for (Event e1 :
                                s1.getQueue()) {
                            if (e1.getId() == event.getId()) {
                                waitingTime.set(event.getTime() - e1.getTime());
                                s1.getQueue().poll();
                                break;
                            }
                        }
                    }
                }
            }
        }
        statisticsHandler.set(0, statisticsHandler.get(0) + waitingTime.get());
        return e;
    }

    /**
     * Handles the arrival event and returns the next event following this arrival event
     * Checks if any servers are free, then assign server to the customer
     *
     * @return the next event depending on the available servers
     */
    Event handleArrivalEvent() {
        Optional<Integer> serverIndex = event.getCustomerType() == CustomerType.NORMAL ? getNormalFreeServer() : getGreedyFreeServer();
        //THERE IS AVAILABLE SERVER
        //THIS FUNCTION CHECKS IF THERE ARE AVAILABLE SERVERS
        return serverIndex.map(id -> {
            Server s = this.serverList.get(id);
            Event e = new Event();
            Server s1 = new Server(s.getId(), ServerState.SERVING, s.getServerType(), s.getQueue(), event.getTime() + event.getServiceTime());
            if (s.getServerType() == ServerType.HUMAN) {
                e = new ServeEvent(event.getId(), event.getTime(), s.getId(), EventState.SERVE, event::getServiceTime, event.getCache(), event.getCustomerType());
            } else if (s.getServerType() == ServerType.SELFCHECKOUT) {
                e = new ServeBySelfCheckEvent(event.getId(), event.getTime(), s.getId(), EventState.SERVEBYSELFCHECKOUT, event::getServiceTime, event.getCache(), event.getCustomerType());
            }
            serverList.set(s.getId() - 1, s1);
            return e;
        }).orElseGet(() -> {
            Event e = new Event();

            if (event.getCustomerType() == CustomerType.GREEDY) {
                Server earliestServer = getEarliestAvailableServer();
                e = new WaitEvent(event.getId(), event.getTime(), earliestServer.getId(), EventState.WAIT, event::getServiceTime, event.getCache(), event.getCustomerType());
                LinkedList<Event> eventQueue = new LinkedList<>(earliestServer.getQueue());
                eventQueue.add(e);
                Server newServer = new Server(earliestServer.getId(), earliestServer.getServerState(), earliestServer.getServerType(), eventQueue, earliestServer.getNextAvailableTime() + event.getServiceTime());
                serverList.set(earliestServer.getId() - 1, newServer);
            } else {
                //NO AVAILABLE SERVER, NEED TO CHECK IF ANY SELFCHECKOUT
                //IF AT MAX SELFCHECKOUT, NEED TO ADD TO SERVER QUEUE IF NOT AT <MAX QUEUE> LENGTH
                int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;
                Server earliestAvailableServer = getEarliestAvailableServer();
                //LEAVE IF THERE IS ALREADY <MAX QUEUE> IN THE QUEUE
                if (earliestAvailableServer.getId() != 0) {
                    if (earliestAvailableServer.canQueue(this.maxQueueLength) || selfCheckoutAvailable(this.maxQueueLength)) {
                        this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                        e = new LeaveEvent(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event::getServiceTime, event.getCache(), event.getCustomerType());
                    } else {
                        //ADD TO EARLIEST SERVER'S QUEUE
                        if (earliestAvailableServer.getServerType() == ServerType.HUMAN) {
                            e = new WaitEvent(event.getId(), event.getTime(), earliestAvailableServer.getId(), EventState.WAIT, event::getServiceTime, event.getCache(), event.getCustomerType());
                        } else if (earliestAvailableServer.getServerType() == ServerType.SELFCHECKOUT) {
                            e = new WaitAtSelfCheckEvent(event.getId(), event.getTime(), idOfSelfCheckout, EventState.WAITSELFCHECKOUT, event::getServiceTime, event.getCache(), event.getCustomerType());
                        }
                        LinkedList<Event> eventQueue = new LinkedList<>(earliestAvailableServer.getQueue());
                        eventQueue.add(e);
                        Server newServer = new Server(earliestAvailableServer.getId(), earliestAvailableServer.getServerState(), earliestAvailableServer.getServerType(), eventQueue, earliestAvailableServer.getNextAvailableTime() + event.getServiceTime());
                        serverList.set(earliestAvailableServer.getId() - 1, newServer);
                    }
                } else {
                    this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                    e = new LeaveEvent(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event::getServiceTime, event.getCache(), event.getCustomerType());
                }
            }

            return e;
        });
    }

    /**
     * Checks if there are any self-checkout counters available
     *
     * @return true if there are any self-checkout counters available
     */
    boolean selfCheckoutAvailable(int maxQueueLength) {
        int totalQueueSize = this.serverList
                .stream()
                .filter(s -> s.getServerType() == ServerType.SELFCHECKOUT).mapToInt(Server::getQueueSize).sum();
        return totalQueueSize >= maxQueueLength;
    }

    /**
     * Check if there are any free servers available to serve
     *
     * @return an optional containing the result
     */
    Optional<Integer> getNormalFreeServer() {
        Optional<Integer> serverId = Optional.empty();

        for (Server s : serverList) {
            if (s.canServe(event.getTime())) {
                serverId = Optional.ofNullable(s.getId() - 1);
                break;
            }
        }
        return serverId;
    }

    Optional<Integer> getGreedyFreeServer() {
        //Check for
        Optional<Integer> result = Optional.empty();
        List<Server> list = new ArrayList<>();
        for (Server s : this.serverList) {
            if (s.canServe(event.getTime())) {
                list.add(s);
            }
        }
        list.sort((o1, o2) -> {
                if(o1.getQueueSize() == o2.getQueueSize()){
                    //IF QUEUE SAME SIZE, CHOOSE THE FIRST ONE
                    return Integer.compare(o1.getId(), o2.getId());
                } else{
                    return Integer.compare(o1.getQueueSize(), o2.getQueueSize());
                }
        });
        if(!list.isEmpty()){
            result = Optional.of(list.get(0).getId() - 1);
        }

        return result;


//        this.serverList.stream()
//                .filter(s -> s.canServe(event.getTime()))
//                .min((o1, o2) -> {
//                    if(o1.getQueueSize() == o2.getQueueSize()){
//                        return Integer.compare(o1.getId(), o2.getId());
//                    }else {
//                        return Double.compare(o1.getQueueSize(), o2.getQueueSize());
//                    }
//                })
//                .ifPresentOrElse((s) -> {
//                            serverId.set(Optional.of(s.getId() - 1));
//                        },
//                        () -> {
//                            serverId.set(Optional.empty());
//                        });
//
//        return serverId.get();

    }

    boolean findEventInQueue(int eventId, Server s) {
        boolean result = false;
        for (Event e : s.getQueue()) {
            if (e.getId() == eventId) {
                result = true;
                break;
            }
        }
        return result;
    }

    /**
     * Find the earliest available server depending on the nextAvailableTime
     *
     * @return an optional containing the result
     */
    Server getEarliestAvailableServer() {
        Server s = new Server(0);
        if (this.serverList.size() > 1) {
            for (int i = 0; i < this.serverList.size(); i++) {
                if (this.serverList.get(i).canQueue(this.maxQueueLength)) {
                    continue;
                } else {
                    s = this.serverList.get(i);
                    break;
                }
            }
        } else {
            s = this.serverList.get(0);
        }
        return s;
    }
}