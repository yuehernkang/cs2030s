package cs2030.simulator;

import java.util.*;

public class EventHandler {
    private final Event event;
    private final List<Server> serverList;
    private final List<Double> statisticsHandler;
    private final int maxQueueLength;
    private final LinkedList<Double> restTimes;
    private final int numOfSelfCheckoutCounters;

    public EventHandler(Event event, List<Server> serverList, List<Double> statisticsHandler, int maxQueueLength, LinkedList<Double> restTimes, int numOfSelfCheckoutCounters) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
        this.numOfSelfCheckoutCounters = numOfSelfCheckoutCounters;
    }

    Event handleEvent() {
        Event e = new Event(0, 0, 0, EventState.FINISH, 0);
        if (event.getEventState() == EventState.ARRIVAL) e = handleArrivalEvent();
        else if (event.getEventState() == EventState.SERVE || event.getEventState() == EventState.SERVEBYSELFCHECKOUT)
            e = handleServeEvent();
        else if (event.getEventState() == EventState.DONE || event.getEventState() == EventState.DONESELFCHECKOUT)
            e = handleDoneEvent();
        return e;
    }

    Event handleDoneEvent() {
        /*DONE EVENT
        1. Give server specified rest time
        2. Release Server
        3. Create new serve event for waiting customer at current time
         */
        int serverIndex = event.getServerId() - 1;
        double restTime = 0;
        Event e1 = this.serverList.get(serverIndex).getQueue().peek();
        Event e = null;

        if (this.serverList.get(serverIndex).getServerType() == ServerType.SELFCHECKOUT) {
            for (Server s : this.serverList) {
                if(s.getServerType() == ServerType.SELFCHECKOUT && s.getQueueSize() != 0){
                    if(s.getQueue().peek().getId() == event.getId()){
                        s.getQueue().poll();
                    }
                }
            }

            //CHECK IF ANY SELF CHECKOUT
            for (Server s : this.serverList) {
                if (s.getServerType() == ServerType.SELFCHECKOUT && s.getQueueSize() != 0) {

                    Server s1 = this.serverList.get(serverIndex);
                    s1 = new Server(s1.getId(), ServerState.SERVING, s1.getServerType(), s1.getQueue(), s1.getNextAvailableTime() + s.getQueue().peek().getServiceTime());
                    e = new Event(s.getQueue().peek().getId(), event.getTime(), s1.getId(), EventState.SERVEBYSELFCHECKOUT, s.getQueue().peek().getServiceTime());
                    s.getQueue().poll();
                    this.serverList.set(serverIndex, s1);

                    break;
                } else {
                    e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event.getServiceTime());
                    this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(0));
                }
            }
        } else if (this.serverList.get(serverIndex).getServerType() == ServerType.HUMAN) {
            //There is people waiting for this server
            if (this.serverList.get(serverIndex).getQueueSize() > 0) {
                if (!this.restTimes.isEmpty()) {
                    restTime = this.restTimes.peek();
                }
                e = new Event(e1.getId(), event.getTime() + restTime, e1.getServerId(), EventState.SERVE, e1.getServiceTime());
                this.serverList.get(serverIndex).getQueue().poll();

                this.restTimes.poll();
                //No people waiting for this server
            } else {
                if (!this.restTimes.isEmpty()) {
                    restTime = this.restTimes.peek();
                }
                this.restTimes.poll();

                e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event.getServiceTime());
                if (this.serverList.get(event.getServerId() - 1).getServerType() == ServerType.HUMAN) {
                    this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(restTime));
                } else if (this.serverList.get(event.getServerId() - 1).getServerType() == ServerType.SELFCHECKOUT) {
                    this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(0));
                }
            }

        } else if (e == null) {
            e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event.getServiceTime());
        }

        return e;
    }


    Event handleServeEvent() {
        //Increment number of customers served
        statisticsHandler.set(1, statisticsHandler.get(1) + 1);
        LinkedList<Event> q;
        Server s;
        Event nextEvent = null;
        //If there was someone in the queue
        if (this.serverList.get(event.getServerId() - 1).getQueueSize() != 0) {
            double waitingTime = event.getTime() - this.serverList.get(event.getServerId() - 1).getQueue().peek().getTime();
            statisticsHandler.set(0, statisticsHandler.get(0) + waitingTime);
            q = this.serverList.get(event.getServerId() - 1).getQueue();
            s = new Server(event.getServerId(), ServerState.SERVING, this.serverList.get(event.getServerId() - 1).getServerType(), q, event.getServiceCompletionTime());
            serverList.set(event.getServerId() - 1, s);
        }
        if (this.serverList.get(event.getServerId() - 1).getServerType() == ServerType.HUMAN) {
            nextEvent = new Event(event.getId(), event.getServiceCompletionTime(), event.getServerId(), EventState.DONE, event.getServiceTime());
        } else if (this.serverList.get(event.getServerId() - 1).getServerType() == ServerType.SELFCHECKOUT) {
            nextEvent = new Event(event.getId(), event.getServiceCompletionTime(), event.getServerId(), EventState.DONESELFCHECKOUT, event.getServiceTime());
        }
        return nextEvent;
    }

    Event handleArrivalEvent() {
        int serverIndex = getFreeServer();
        Event nextEvent = null;
        int maxSelfCheckoutCounters = this.serverList.size() + this.numOfSelfCheckoutCounters;

        //THERE IS AVAILABLE SERVER
        //THIS FUNCTION CHECKS IF THERE ARE AVAILABLE SERVERS
        if (serverIndex != -1) {
            Server s = this.serverList.get(serverIndex);
            Server s1 = new Server(s.getId(), ServerState.SERVING, s.getServerType(), s.getQueue(), event.getServiceCompletionTime());
            if (s.getServerType() == ServerType.HUMAN) {
                nextEvent = new Event(event.getId(), event.getTime(), s.getId(), EventState.SERVE, event.getServiceTime());
            } else if (s.getServerType() == ServerType.SELFCHECKOUT) {
                nextEvent = new Event(event.getId(), event.getTime(), s.getId(), EventState.SERVEBYSELFCHECKOUT, event.getServiceTime());
            }
            serverList.set(s.getId() - 1, s1);
        } else {
            //NO AVAILABLE SERVER, NEED TO CHECK IF ANY SELFCHECKOUT
            // IF AT MAX SELFCHECKOUT, NEED TO ADD TO SERVER QUEUE IF NOT AT <MAX QUEUE> LENGTH
//            if(){

//            }else {
            int idOfSelfCheckout = this.serverList.size() - this.numOfSelfCheckoutCounters + 1;
            Server earliestAvailableServer = getEarliestAvailableServer();
            //LEAVE IF THERE IS ALREADY <MAX QUEUE> IN THE QUEUE
            if (earliestAvailableServer.getId() != 0) {
                if (earliestAvailableServer.getQueueSize() >= this.maxQueueLength || getSelfCheckoutTotalQueueSize() >= this.maxQueueLength) {
                    this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                    nextEvent = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event.getServiceTime());
                } else {
                    //ADD TO EARLIEST SERVER'S QUEUE
                    if (earliestAvailableServer.getServerType() == ServerType.HUMAN) {
                        nextEvent = new Event(event.getId(), event.getTime(), earliestAvailableServer.getId(), EventState.WAIT, event.getServiceTime());
                    } else if (earliestAvailableServer.getServerType() == ServerType.SELFCHECKOUT) {
                        nextEvent = new Event(event.getId(), event.getTime(), idOfSelfCheckout, EventState.WAITSELFCHECKOUT, event.getServiceTime());
                    }
                    LinkedList<Event> eventQueue = new LinkedList<>(earliestAvailableServer.getQueue());
                    eventQueue.add(nextEvent);
                    Server newServer = new Server(earliestAvailableServer.getId(), earliestAvailableServer.getServerState(), earliestAvailableServer.getServerType(), eventQueue, earliestAvailableServer.getNextAvailableTime() + event.getServiceTime());
                    serverList.set(earliestAvailableServer.getId() - 1, newServer);
                }
            } else {
                this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                nextEvent = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event.getServiceTime());
            }
//            }

        }
        return nextEvent;
    }

    int getSelfCheckoutTotalQueueSize() {
        int totalQueueSize = 0;
        for (Server s : this.serverList) {
            if(s.getServerType() == ServerType.SELFCHECKOUT) {
                totalQueueSize += s.getQueueSize();
            }
        }
        return totalQueueSize;
    }

    int getFreeServer() {
        int serverId = -1;
        for (Server s : serverList) {
            if (s.canServe(event.getTime())) {
                serverId = s.getId() - 1;
                break;
            }
        }
        return serverId;
    }

    Server getEarliestAvailableServer() {
        Server s = new Server(0);
        if (this.serverList.size() > 1) {
            for (int i = 0; i < this.serverList.size(); i++) {
                if (this.serverList.get(i).getQueueSize() >= this.maxQueueLength) {
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

    Server checkVacantSelfCheckout() {
        return this.serverList.stream()
                .filter(x -> x.getServerType()  == ServerType.SELFCHECKOUT)
                .filter(y -> y.canServe(event.getTime()))
                .findFirst().get();
    }
}
