package cs2030.simulator;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

public class Server {
    private final int id;
    private final ServerState state;
    private final LinkedList<Event> waitingQueue;
    private final double nextAvailableTime;

    //Simulator to create server
    public Server(int id) {
        this.id = id;
        this.state = ServerState.IDLE;
        this.waitingQueue = new LinkedList<>();
        this.nextAvailableTime = 0;
    }

    //Server that start serving
    public Server(int id, ServerState state, LinkedList<Event> waitingQueue, double nextAvailableTime) {
        this.id = id;
        this.state = state;
        this.waitingQueue = waitingQueue;
        this.nextAvailableTime = nextAvailableTime;
    }

    public int getId() {
        return this.id;
    }

    public ServerState getServerState() {
        return state;
    }

    public double getNextAvailableTime() {
//        double newTime = 0;
//        for (int i = 0; i < getQueueSize(); i++) {
//            newTime = currentTime + this.getQueue().poll().getServiceTime();
//        }
//        return newTime;
        return nextAvailableTime;
    }

    public int getQueueSize() {
        return this.waitingQueue.size();
    }

    public LinkedList<Event> getQueue() {
        return this.waitingQueue;
    }

    public Server releaseServer() {
        return new Server(this.id, ServerState.IDLE, this.waitingQueue, this.nextAvailableTime);
    }
    public Server releaseServer(double restTime) {
        return new Server(this.id, ServerState.IDLE, this.waitingQueue, this.nextAvailableTime + restTime);
    }

    //Check if this server can serve the next customer
    //customer time + service time = time when done
    //if time when done is less than the next customer time
    boolean canServe(double time) {
        boolean result = false;
        if(this.getServerState()!= ServerState.SERVING && this.nextAvailableTime <= time) {
            result = true;
        }
        return result;
    }

}
