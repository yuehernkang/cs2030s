package cs2030.simulator;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Queue;

public class EventHandler {
    private final Event event;
    private final List<Server> serverList;
    private final List<Double> statisticsHandler;
    private final int maxQueueLength;

    public EventHandler(Event event, List<Server> serverList, List<Double> statisticsHandler) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = 1;
    }

    public EventHandler(Event event, List<Server> serverList, List<Double> statisticsHandler, int maxQueueLength) {
        this.event = event;
        this.serverList = serverList;
        this.statisticsHandler = statisticsHandler;
        this.maxQueueLength = maxQueueLength;
    }

    public List<Event> handleEvent() {
        List<Event> nextEvent = new ArrayList<>();
        switch (event.getEventState()) {
            case ARRIVAL: {
                nextEvent = handleArrivalEvent();
                break;
            }
            case SERVE: {
                nextEvent.add(handleServeEvent());
                break;
            }
            case WAIT: {
                nextEvent.add(handleWaitEvent());
                break;
            }
            case DONE: {
                nextEvent.add(handleDoneEvent());
                break;
            }
        }
        return nextEvent;
    }

    Event handleDoneEvent() {
        serverList.set(event.getServerId() - 1, this.serverList.get(event.getServerId() - 1).releaseServer());
        return null;
    }


    Event handleWaitEvent() {
        Event nextEvent;
        Server newServer = new Server(event.getServerId(), ServerState.SERVING, this.serverList.get(event.getServerId() - 1).getQueue(), this.serverList.get(event.getServerId() - 1).getNextAvailableTime());
        serverList.set(event.getServerId() - 1, newServer);
        nextEvent = new Event(event.getId(), this.serverList.get(event.getServerId() - 1).getNextAvailableTime(), event.getServerId(), EventState.SERVE, event.getServiceTime());
        return nextEvent;
    }

    Event handleServeEvent() {
        //Increment number of customers served
        statisticsHandler.set(1, statisticsHandler.get(1) + 1);
        Queue<Event> q;
        Server s;
        //If there was someone in the queue
        if(this.serverList.get(event.getServerId() - 1).getQueueSize() != 0) {
            double waitingTime = event.getTime() - this.serverList.get(event.getServerId() - 1).getQueue().poll().getTime();
            statisticsHandler.set(0, statisticsHandler.get(0) + waitingTime);
            q = this.serverList.get(event.getServerId() - 1).getQueue();
            q.remove();
            s = new Server(event.getServerId(), ServerState.SERVING, q, event.getServiceCompletionTime());
            serverList.set(event.getServerId() - 1, s);
        }
//        s = new Server(event.getServerId(), ServerState.IDLE, this.serverList.get(event.getServerId()).getQueueSize(), event.getServiceCompletionTime());
//        serverList.set(event.getServerId() - 1, s);
        Event nextEvent = new Event(event.getId(),event.getServiceCompletionTime(), event.getServerId(), EventState.DONE, event.getServiceTime());
        return nextEvent;
    }

    List<Event> handleArrivalEvent() {
        List<Event> nextEvent = new ArrayList<>();
        for (int i = 0; i < serverList.size(); i++) {
            Server s = getFreeServer();

            //THERE IS AVAILABLE SERVER
            if (s != null) {
                Server s1 = new Server(s.getId(), ServerState.SERVING, s.getQueue(), event.getServiceCompletionTime());
                Event e = new Event(event.getId(), event.getTime(), s.getId(), EventState.SERVE, event.getServiceTime());
                serverList.set(s.getId() - 1, s1);
                nextEvent.add(e);
                break;
            } else {
                //NO AVAILABLE SERVER, NEED TO ADD TO SERVER QUEUE IF NOT AT <MAX QUEUE> LENGTH
                Server earliestAvailableServer = getEarliestAvailableServer();
                //LEAVE IF THERE IS ALREADY <MAX QUEUE> IN THE QUEUE
                if (earliestAvailableServer.getQueueSize() >= this.maxQueueLength) {
                    this.statisticsHandler.set(2, this.statisticsHandler.get(2) + 1);
                    nextEvent.add(new Event(event.getId(), event.getTime(), event.getServerId(), EventState.LEAVE, event.getServiceTime()));
                } else {
                    //ADD TO EARLIEST SERVER'S QUEUE
                    Event waitEvent = new Event(event.getId(), event.getTime(), earliestAvailableServer.getId(), EventState.WAIT, event.getServiceTime());
//                    Event serveEvent = new Event(event.getId(), earliestAvailableServer.getNextAvailableTime(), earliestAvailableServer, EventState.SERVE, event.getServiceTime());
                    nextEvent.add(waitEvent);
//                    nextEvent.add(serveEvent);

                    Server newServer = new Server(earliestAvailableServer.getId(), earliestAvailableServer.getServerState(), earliestAvailableServer.getQueue(), earliestAvailableServer.getNextAvailableTime());
                    serverList.set(earliestAvailableServer.getId() - 1, newServer);
                    break;
                }
            }
        }
        return nextEvent;
    }

    Server getFreeServer() {
        Server result = null;
        for (Server s : serverList) {
            if (s.canServe()) {
                result = s;
                break;
            }
        }
        return result;
    }

    Server getEarliestAvailableServer() {
        serverList.sort(Comparator.comparingDouble(Server::getNextAvailableTime));
        return serverList.get(0);
    }
}
