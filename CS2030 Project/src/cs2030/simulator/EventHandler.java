package cs2030.simulator;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;
import java.util.Comparator;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

/**
 * EventHandler
 * Process next event depending on current event.
 *
 * @return the next event
 */
public class EventHandler {
    private final Event event;
    private final List<Server> serverList;
    private final List<Double> statisticsHandler;
    private final int maxQueueLength;
    private final LinkedList<Double> restTimes;
    private final int numOfSelfCheckoutCounters;
    private final RandomGenerator randomGenerator;
    private final double probOfResting;
    private final double probOfGreedyCustomer;

    public EventHandler(Event event,
                        List<Server> serverList,
                        List<Double> statisticsHandler,
                        int maxQueueLength, LinkedList<Double> restTimes,
                        int numOfSelfCheckoutCounters,
                        RandomGenerator randomGenerator,
                        double probOfResting, double probOfGreedyCustomer) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
        this.randomGenerator = randomGenerator;
        this.probOfResting = probOfResting;
        this.probOfGreedyCustomer = probOfGreedyCustomer;
    }

    Event handleEvent() {
        Event e = new Event();
        if (event.getEventState() == EventState.ARRIVAL) {
            e = handleArrivalEvent();
        } else if (event.getEventState() == EventState.SERVE ||
                event.getEventState() == EventState.SERVEBYSELFCHECKOUT) {
            e = handleServeEvent();
        } else if (event.getEventState() == EventState.DONE ||
                event.getEventState() == EventState.DONESELFCHECKOUT) {
            e = handleDoneEvent();
        }
        return e;
    }

    /**
     * Give server specified rest time.
     * Release Server
     * Create new serve event for waiting customer at current time
     *
     * @return the next event
     */
    Event handleDoneEvent() {
        int serverIndex = event.getServerId() - 1;
        double restTime = 0;
        Event e1 = this.serverList.get(serverIndex).getQueue().peek();
        ServerType serverType = this.serverList.get(serverIndex).getServerType();
        Event e = new Event();

        switch (serverType) {
            case SELFCHECKOUT:
                for (Server s : this.serverList) {
                    if (s.getServerType() == ServerType.SELFCHECKOUT && !s.queueIsEmpty()) {
                        Server s1 = this.serverList.get(serverIndex);
                        Supplier<Double> serviceTime = () -> s.getQueue().peek().getServiceTime();

                        s1 = new Server(s1.getId(), ServerState.SERVING, s1.getServerType(),
                                s1.getQueue(), s1.getNextAvailableTime());
                        e = new ServeBySelfCheckEvent(s.getQueue().peek().getId(), event.getTime(),
                                s1.getId(), EventState.SERVEBYSELFCHECKOUT, serviceTime,
                                event.getCache(), s.getQueue().peek().getCustomerType());
                        this.serverList.set(serverIndex, s1);
                        break;
                    } else {
                        e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(),
                                EventState.FINISH, event::getServiceTime, event.getCustomerType());
                        this.serverList.set(serverIndex,
                                this.serverList.get(serverIndex).releaseServer(0));
                    }
                }
                break;
            case HUMAN:
                boolean serverRest = this.randomGenerator.genRandomRest() < probOfResting;
                //There is people waiting for this server
                if (!this.restTimes.isEmpty()) {
                    restTime = this.restTimes.peek();
                }
                if (serverRest) {
                    if (this.probOfGreedyCustomer != -1) {
                        restTime
                                = this.randomGenerator.genRestPeriod();
                    }
                    //SERVER CAN REST
                    this.restTimes.poll();
                    this.serverList.set(serverIndex,
                            this.serverList.get(serverIndex).releaseServer(restTime));
                    if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                        e = new ServeEvent(e1.getId(), event.getTime()
                                + restTime, e1.getServerId(),
                                EventState.SERVE, e1::getServiceTime, event.getCache(),
                                e1.getCustomerType());
                    } else {
                        //No people waiting for this server
                        e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(),
                                EventState.FINISH, event::getServiceTime, event.getCustomerType());
                    }
                } else {
                    //SERVER CANNOT REST
                    if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                        Event tempEvent = this.serverList.get(serverIndex).getQueue().peek();
                        e = new ServeEvent(tempEvent.getId(), event.getTime(),
                                tempEvent.getServerId(),
                                EventState.SERVE, tempEvent::getServiceTime, tempEvent.getCache(),
                                tempEvent.getCustomerType());
                    } else {
                        //No people waiting for this server
                        this.serverList.set(serverIndex,
                                this.serverList.get(serverIndex).releaseServer(0));
                        e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(),
                                EventState.FINISH, event::getServiceTime, event.getCustomerType());
                    }
                }
                break;
            default:
                e = new FinishEvent(event.getId(), event.getTime(), event.getServerId(),
                        EventState.FINISH, event::getServiceTime, event.getCustomerType());
        }
        return e;
    }

    /**
     * Handles the serve event and returns the next event following this serve event.
     * Checks if the queue of the server is empty and returns a serve event if there is a customer
     * in the queue
     *
     * @return the next event, finish event if there are no events in the queue,
     *          serve event if there are events in the queue
     */
    Event handleServeEvent() {
        statisticsHandler.set(1, statisticsHandler.get(1) + 1);
        LinkedList<Event> q;
        Server s;
        int serverIndex = event.getServerId() - 1;
        ServerType st = this.serverList.get(serverIndex).getServerType();
        EventState es = st == ServerType.HUMAN ? EventState.DONE : EventState.DONESELFCHECKOUT;
        Event e = new Event();
        if (st == ServerType.HUMAN) {
            e = new DoneEvent(event.getId(), event.getServiceCompletionTime(), event.getServerId(),
                    es, event::getServiceTime, e.getCache(), event.getCustomerType());
        }
        if (st == ServerType.SELFCHECKOUT) {
            e = new DoneSelfCheckEvent(event.getId(), event.getServiceCompletionTime(),
                    event.getServerId(), es, event::getServiceTime,
                    e.getCache(), event.getCustomerType());
        }
        double waitingTime = 0.0;

        //If there was someone in the queue
        switch (st) {
            case HUMAN: {
                if (!this.serverList.get(serverIndex).queueIsEmpty()) {
                    for (int i = 0; i < this.serverList.get(serverIndex).getQueue().size(); i++) {
                        if (event.getId() ==
                                this.serverList.get(serverIndex).getQueue().get(i).getId()) {
                            waitingTime = event.getTime() -
                                    this.serverList.get(serverIndex).getQueue().get(i).getTime();
                            this.serverList.get(serverIndex).getQueue().poll();
                            break;
                        }
                    }
                    q = this.serverList.get(serverIndex).getQueue();
                    s = new Server(event.getServerId(), ServerState.SERVING,
                            this.serverList.get(serverIndex).getServerType(), q,
                            event.getServiceCompletionTime());
                    serverList.set(serverIndex, s);
                }
                break;
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
                break;
            }
            default:
        }
        statisticsHandler.set(0, statisticsHandler.get(0) + waitingTime);
        return e;
    }

    /**
     * Handles the arrival event and returns the next event following this arrival event.
     * Checks if any servers are free, then assign server to the customer
     * If no human server is available, check if there is any selfcheckout available
     *
     * @return the next event depending on the available servers
     */
    Event handleArrivalEvent() {
        Optional<Integer> serverIndex = event.getCustomerType() == CustomerType.NORMAL ?
                getNormalFreeServer() : getGreedyFreeServer();
        int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;

        //THERE IS AVAILABLE SERVER
        //THIS FUNCTION CHECKS IF THERE ARE AVAILABLE SERVERS
        return serverIndex.map(id -> {
            Server s = this.serverList.get(id);
            Event e = new Event();
            Server s1 = new Server(s.getId(), ServerState.SERVING, s.getServerType(),
                    s.getQueue(), event.getTime() + event.getServiceTime());
            if (s.getServerType() == ServerType.HUMAN) {
                e = new ServeEvent(event.getId(), event.getTime(), s.getId(), EventState.SERVE,
                        event::getServiceTime, event.getCache(), event.getCustomerType());
            } else if (s.getServerType() == ServerType.SELFCHECKOUT) {
                e = new ServeBySelfCheckEvent(event.getId(), event.getTime(), s.getId(),
                        EventState.SERVEBYSELFCHECKOUT, event::getServiceTime, event.getCache(),
                        event.getCustomerType());
            }
            serverList.set(s.getId() - 1, s1);
            return e;
        }).orElseGet(() -> {
            Event e = new Event();

            if (event.getCustomerType() == CustomerType.GREEDY) {
                Server s2 = getServerForGreedy();
                if (s2.getId() != 0) {
                    if (s2.getServerType() == ServerType.HUMAN) {
                        e = new WaitEvent(event.getId(), event.getTime(), s2.getId(),
                                EventState.WAIT, event::getServiceTime,
                                event.getCache(), event.getCustomerType());
                    } else if (s2.getServerType() == ServerType.SELFCHECKOUT) {
                        e = new WaitAtSelfCheckEvent(event.getId(), event.getTime(),
                                idOfSelfCheckout, EventState.WAIT, event::getServiceTime,
                                event.getCache(), event.getCustomerType());
                    }
                    LinkedList<Event> eventQueue = new LinkedList<>(s2.getQueue());
                    eventQueue.add(e);
                    Server newServer = new Server(s2.getId(), s2.getServerState(),
                            s2.getServerType(), eventQueue, s2.getNextAvailableTime());
                    serverList.set(s2.getId() - 1, newServer);
                } else {
                    this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                    e = new LeaveEvent(event.getId(), event.getTime(), event.getServerId(),
                            EventState.LEAVE, event::getServiceTime, event.getCache(),
                            event.getCustomerType());
                }


            } else {

                Server earliestAvailableServer = getEarliestAvailableServer();
                //LEAVE IF THERE IS ALREADY <MAX QUEUE> IN THE QUEUE
                if (earliestAvailableServer.getId() != 0) {
                    if (!earliestAvailableServer.canQueue(this.maxQueueLength)) {
                        this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                        e = new LeaveEvent(event.getId(), event.getTime(), event.getServerId(),
                                EventState.LEAVE,
                                event::getServiceTime, event.getCache(), event.getCustomerType());
                    } else {
                        //ADD TO EARLIEST SERVER'S QUEUE
                        if (earliestAvailableServer.getServerType() == ServerType.HUMAN) {
                            e = new WaitEvent(event.getId(), event.getTime(),
                                    earliestAvailableServer.getId(),
                                    EventState.WAIT, event::getServiceTime, event.getCache(),
                                    event.getCustomerType());
                        } else if (earliestAvailableServer.getServerType()
                                == ServerType.SELFCHECKOUT) {
                            e = new WaitAtSelfCheckEvent(event.getId(), event.getTime(),
                                    idOfSelfCheckout,
                                    EventState.WAITSELFCHECKOUT, event::getServiceTime,
                                    event.getCache(), event.getCustomerType());
                        }
                        LinkedList<Event> eventQueue
                                = new LinkedList<>(earliestAvailableServer.getQueue());
                        eventQueue.add(e);
                        Server newServer = new Server(earliestAvailableServer.getId(),
                                earliestAvailableServer.getServerState(),
                                earliestAvailableServer.getServerType(), eventQueue,
                                earliestAvailableServer.getNextAvailableTime());
                        serverList.set(earliestAvailableServer.getId() - 1, newServer);
                    }
                } else {
                    this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                    e = new LeaveEvent(event.getId(), event.getTime(), event.getServerId(),
                            EventState.LEAVE,
                            event::getServiceTime, event.getCache(), event.getCustomerType());
                }
            }

            return e;
        });
    }

    /**
     * Checks if there are any self-checkout counters available.
     *
     * @return true if there are any self-checkout counters available
     */
    boolean selfCheckoutAvailable(int maxQueueLength) {
        int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;
        int queueSize = this.serverList.get(idOfSelfCheckout - 1).getQueueSize();
        boolean result = false;
        if (queueSize < maxQueueLength) {
            result = true;
        }
        return result;
    }

    /**
     * Check if there are any free servers available to serve.
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
        Optional<Integer> result;
        List<Server> list = new ArrayList<>();
        for (Server s : this.serverList) {
            if (s.canServe(event.getTime())) {
                list.add(s);
            }
        }
        list.sort((o1, o2) -> {
            if (o1.getQueueSize() == o2.getQueueSize()) {
                //IF QUEUE SAME SIZE, CHOOSE THE FIRST ONE
                return Integer.compare(o1.getId(), o2.getId());
            } else {
                return Integer.compare(o1.getQueueSize(), o2.getQueueSize());
            }
        });
        if (!list.isEmpty()) {
            result = Optional.of(list.get(0).getId() - 1);
        } else {
            result = Optional.empty();
        }

        return result;

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
     * A greedy customer always joins the queue with the fewest customers;
     * in the case of a tie, he/she breaks the tie by choosing the
     * first one while scanning from servers 1 to k.
     * @return Server for the event
     */

    Server getServerForGreedy() {
        Server resultServer = new Server(0);
        List<Server> newList2 = new ArrayList<>();

        int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;
        for (Server s :
                this.serverList) {
            if (s.getServerType() == ServerType.SELFCHECKOUT) {
                if (s.getId() == idOfSelfCheckout) {
                    newList2.add(s);
                }
            } else {
                newList2.add(s);
            }
        }
        newList2.sort(new Comparator<Server>() {
            @Override
            public int compare(Server o1, Server o2) {
                if (o2.getQueueSize() == o1.getQueueSize()) {
                    return Integer.compare(o1.getId(), o2.getId());
                }
                return Integer.compare(o1.getQueueSize(), o2.getQueueSize());

            }
        });

        for (int i = 0; i < newList2.size(); i++) {
            if (newList2.get(i).getQueueSize() < this.maxQueueLength) {
                resultServer = newList2.get(i);
                break;
            }
        }


        return resultServer;
    }

    boolean humanServerAvailable() {
        boolean isFull = false;
        for (Server s : this.serverList) {
            if (s.getServerType() == ServerType.HUMAN && s.getQueueSize() < this.maxQueueLength) {
                isFull = true;
                break;
            }
        }
        return isFull;
    }

    /**
     * Find the earliest available server depending on the nextAvailableTime.
     *
     * @return an optional containing the result
     */
    Server getEarliestAvailableServer() {
        Server s = new Server(0);

        List<Server> newList2 = new ArrayList<>();

        int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;
        for (Server s1 :
                this.serverList) {
            if (s1.getServerType() == ServerType.SELFCHECKOUT) {
                if (s1.getId() == idOfSelfCheckout) {
                    newList2.add(s1);
                }
            } else {
                newList2.add(s1);
            }
        }
        if (this.serverList.size() > 1) {
            for (int i = 0; i < newList2.size(); i++) {
                if (newList2.get(i).canQueue(this.maxQueueLength)) {
                    s = newList2.get(i);
                    break;
                }
            }
        } else {
            s = this.serverList.get(0);
        }
        return s;
    }
}