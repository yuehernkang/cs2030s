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

    public EventHandler(Event event,
                        List<Server> serverList,
                        List<Double> statisticsHandler,
                        int maxQueueLength, LinkedList<Double> restTimes,
                        int numOfSelfCheckoutCounters) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
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
                        s1 = new Server(s1.getId(), ServerState.SERVING, s1.getServerType(), s1.getQueue(), s1.getNextAvailableTime() + s.getQueue().peek().getServiceTime());
                        e = new Event(s.getQueue().peek().getId(), event.getTime(), s1.getId(), EventState.SERVEBYSELFCHECKOUT, () -> s.getQueue().peek().getServiceTime());
                        this.serverList.set(serverIndex, s1);
                        break;
                    } else {
                        e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime);
                        this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(0));
                    }
                }
                break;
            case HUMAN:
                //There is people waiting for this server
                if (!this.restTimes.isEmpty()) restTime = this.restTimes.peek();
                if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                    this.restTimes.poll();
                    e = new Event(e1.getId(), event.getTime() + restTime, e1.getServerId(), EventState.SERVE, e1::getServiceTime);
                } else {
                    //No people waiting for this server
                    this.restTimes.poll();
                    this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(restTime));
                    e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime);
                }
                break;
            default:
                e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event::getServiceTime);
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
        Event e = new Event(event.getId(), event.getServiceCompletionTime(), event.getServerId(), es, event::getServiceTime);
        double waitingTime = 0;

        //If there was someone in the queue
        switch (st) {
            case HUMAN: {
                if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                    for (int i = 0; i < this.serverList.get(serverIndex).getQueue().size(); i++) {
                        if (event.getId() == this.serverList.get(serverIndex).getQueue().get(i).getId()) {
                            waitingTime = event.getTime() - this.serverList.get(serverIndex).getQueue().peek().getTime();
                            this.serverList.get(serverIndex).getQueue().poll();
                        }
                    }
                    q = this.serverList.get(serverIndex).getQueue();
                    s = new Server(event.getServerId(), ServerState.SERVING, this.serverList.get(serverIndex).getServerType(), q, event.getServiceCompletionTime());
                    serverList.set(serverIndex, s);
                }
            }
            case SELFCHECKOUT: {
                for (Server s1 : this.serverList) {
                    if (s1.getServerType() == ServerType.SELFCHECKOUT) {
                        for (Event e1 :
                                s1.getQueue()) {
                            if (e1.getId() == event.getId()) {
                                waitingTime = event.getTime() - e1.getTime();
                                s1.getQueue().poll();
                                break;
                            }
                        }
                    }
                }
            }
        }
        statisticsHandler.set(0, statisticsHandler.get(0) + waitingTime);
        return e;
    }

    /**
     * Handles the arrival event and returns the next event following this arrival event
     * Checks if any servers are free, then assign server to the customer
     *
     * @return the next event depending on the available servers
     */
    Event handleArrivalEvent() {
        Optional<Integer> serverIndex = getFreeServer();
        //THERE IS AVAILABLE SERVER
        //THIS FUNCTION CHECKS IF THERE ARE AVAILABLE SERVERS
        return serverIndex.map(id -> {
            Server s = this.serverList.get(id);
            Event e = new Event();
            Server s1 = new Server(s.getId(), ServerState.SERVING, s.getServerType(), s.getQueue(), event.getServiceCompletionTime());
            if (s.getServerType() == ServerType.HUMAN) {
                e = new Event(event.getId(), event.getTime(), s.getId(), EventState.SERVE, event::getServiceTime);
            } else if (s.getServerType() == ServerType.SELFCHECKOUT) {
                e = new Event(event.getId(), event.getTime(), s.getId(), EventState.SERVEBYSELFCHECKOUT, event::getServiceTime);
            }
            serverList.set(s.getId() - 1, s1);
            return e;
        }).orElseGet(() -> {
            Event e = new Event();
            //NO AVAILABLE SERVER, NEED TO CHECK IF ANY SELFCHECKOUT
            //IF AT MAX SELFCHECKOUT, NEED TO ADD TO SERVER QUEUE IF NOT AT <MAX QUEUE> LENGTH
            int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;
            Server earliestAvailableServer = getEarliestAvailableServer();
            //LEAVE IF THERE IS ALREADY <MAX QUEUE> IN THE QUEUE
            if (earliestAvailableServer.getId() != 0) {
                if (earliestAvailableServer.canQueue(this.maxQueueLength) || selfCheckoutAvailable(this.maxQueueLength)) {
                    this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                    e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event::getServiceTime);
                } else {
                    //ADD TO EARLIEST SERVER'S QUEUE
                    if (earliestAvailableServer.getServerType() == ServerType.HUMAN) {
                        e = new Event(event.getId(), event.getTime(), earliestAvailableServer.getId(), EventState.WAIT, event::getServiceTime);
                    } else if (earliestAvailableServer.getServerType() == ServerType.SELFCHECKOUT) {
                        e = new Event(event.getId(), event.getTime(), idOfSelfCheckout, EventState.WAITSELFCHECKOUT, event::getServiceTime);
                    }
                    LinkedList<Event> eventQueue = new LinkedList<>(earliestAvailableServer.getQueue());
                    eventQueue.add(e);
                    Server newServer = new Server(earliestAvailableServer.getId(), earliestAvailableServer.getServerState(), earliestAvailableServer.getServerType(), eventQueue, earliestAvailableServer.getNextAvailableTime() + event.getServiceTime());
                    serverList.set(earliestAvailableServer.getId() - 1, newServer);
                }
            } else {
                this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event::getServiceTime);
            }
            return e;
        });
    }

    /**
     * Checks if there are any self-checkout counters available
     *
     * @return if there are any self-checkout counters available
     */
    boolean selfCheckoutAvailable(int maxQueueLength) {
        int totalQueueSize = 0;
        for (Server s : this.serverList) {
            if (s.getServerType() == ServerType.SELFCHECKOUT) totalQueueSize += s.getQueueSize();
        }
        return totalQueueSize >= maxQueueLength;
    }

    /**
     * Check if there are any free servers available to serve
     *
     * @return an optional containing the result
     */
    Optional<Integer> getFreeServer() {
        Optional<Integer> serverId = Optional.empty();
        for (Server s : serverList) {
            if (s.canServe(event.getTime())) {
                serverId = Optional.of(s.getId() - 1);
                break;
            }
        }
        return serverId;
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
     * Find the earliest available server depending on the @link{nextAvailableTime}
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