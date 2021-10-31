package cs2030.simulator;

import java.util.*;

public class EventHandler {
    private final Event event;
    private final List<Server> serverList;
    private final List<Double> statisticsHandler;
    private final int maxQueueLength;
    private final List<Double> restTimes;

    public EventHandler(Event event, List<Server> serverList, List<Double> statisticsHandler, int maxQueueLength, List<Double> restTimes) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = maxQueueLength;
        this.restTimes = restTimes;
    }

    Event handleEvent() {
        Event e = new Event(0, 0, 0, EventState.FINISH, 0);
        if (event.getEventState() == EventState.ARRIVAL) e = handleArrivalEvent();
        else if (event.getEventState() == EventState.SERVE) e = handleServeEvent();
        else if (event.getEventState() == EventState.DONE) e = handleDoneEvent();
        return e;
    }

    Event handleDoneEvent() {
        /*DONE EVENT
        0.5. Give server specified rest time
        1. Release Server
        2. Create new serve event for waiting customer at current time
         */
        int serverIndex = event.getServerId() - 1;
        double restTime = 0;
        Event e1 = this.serverList.get(serverIndex).getQueue().peek();
        Event e;
        if(!this.restTimes.isEmpty()){
            restTime = this.restTimes.get(event.getId() - 1);
        }

        //There is people waiting for this server
        if (this.serverList.get(serverIndex).getQueueSize() > 0) {
            e = new Event(e1.getId(), event.getTime(), e1.getServerId(), EventState.SERVE, e1.getServiceTime());
            if (this.serverList.get(serverIndex).getQueue().peek().getId() == event.getId()) {
                this.serverList.get(serverIndex).getQueue().poll();
            }
            //No people waiting for this server
        } else {
            e = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.FINISH, event.getServiceTime());
            this.serverList.set(serverIndex, this.serverList.get(serverIndex).releaseServer(restTime));
        }
        return e;
    }

    Event handleServeEvent() {
        //Increment number of customers served
        statisticsHandler.set(1, statisticsHandler.get(1) + 1);
        Queue<Event> q;
        Server s;
        //If there was someone in the queue
        if (this.serverList.get(event.getServerId() - 1).getQueueSize() != 0) {
            double waitingTime = event.getTime() - this.serverList.get(event.getServerId() - 1).getQueue().poll().getTime();
            statisticsHandler.set(0, statisticsHandler.get(0) + waitingTime);
            q = this.serverList.get(event.getServerId() - 1).getQueue();
            s = new Server(event.getServerId(), ServerState.SERVING, q, event.getServiceCompletionTime());
            serverList.set(event.getServerId() - 1, s);
        }
//        s = new Server(event.getServerId(), ServerState.IDLE, this.serverList.get(event.getServerId()).getQueueSize(), event.getServiceCompletionTime());
//        serverList.set(event.getServerId() - 1, s);
        Event nextEvent = new Event(event.getId(), event.getServiceCompletionTime(), event.getServerId(), EventState.DONE, event.getServiceTime());
        return nextEvent;
    }

    Event handleArrivalEvent() {
        Event nextEvent;
        Optional<Server> s = getFreeServer();

        //THERE IS AVAILABLE SERVER
        if (s.isPresent()) {
            Server s1 = new Server(s.get().getId(), ServerState.SERVING, s.get().getQueue(), event.getServiceCompletionTime());
            nextEvent = new Event(event.getId(), event.getTime(), s.get().getId(), EventState.SERVE, event.getServiceTime());
            serverList.set(s.get().getId() - 1, s1);
        } else {
            //NO AVAILABLE SERVER, NEED TO ADD TO SERVER QUEUE IF NOT AT <MAX QUEUE> LENGTH
            Server earliestAvailableServer = getEarliestAvailableServer();
            //LEAVE IF THERE IS ALREADY <MAX QUEUE> IN THE QUEUE
            if (earliestAvailableServer.getQueueSize() >= this.maxQueueLength) {
                this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                nextEvent = new Event(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event.getServiceTime());
            } else {
                //ADD TO EARLIEST SERVER'S QUEUE
                nextEvent = new Event(event.getId(), event.getTime(), earliestAvailableServer.getId(), EventState.WAIT, event.getServiceTime());
                Queue<Event> eventQueue = new PriorityQueue<>(earliestAvailableServer.getQueue());
                eventQueue.add(nextEvent);
                Server newServer = new Server(earliestAvailableServer.getId(), earliestAvailableServer.getServerState(), eventQueue, earliestAvailableServer.getNextAvailableTime() + event.getServiceTime());
                serverList.set(earliestAvailableServer.getId() - 1, newServer);
            }
        }
        return nextEvent;
    }

    Optional<Server> getFreeServer() {
        Optional<Server> result = Optional.empty();
        for (Server s : serverList) {
            if (s.canServe(event.getTime())) {
                result = Optional.of(s);
                break;
            }
        }
        return result;
    }

    Server getEarliestAvailableServer() {
        List<Server> newList = new ArrayList<>(this.serverList);
        Server s = new Server(0);
        newList.sort(Comparator.comparingDouble(Server::getNextAvailableTime));
        if(this.serverList.size() > 1){
            for (int i = 0; i < this.serverList.size(); i++) {
                if(this.serverList.get(i).getQueueSize() >= this.maxQueueLength) {
                    continue;
                } else{
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
