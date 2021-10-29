package cs2030.simulator;

import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;

public class Server {
    private final int id;
    private final ServerState state;
    private final Queue<Event> waitingQueue;
    private final double nextAvailableTime;

    //Simulator to create server
    public Server(int id) {
        this.id = id;
        this.state = ServerState.IDLE;
        this.waitingQueue = new PriorityQueue<>();
        this.nextAvailableTime = 0;
    }

    //Server that start serving
    public Server(int id, ServerState state, Queue<Event> waitingQueue, double nextAvailableTime) {
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
        double time = nextAvailableTime;
        if(!this.waitingQueue.isEmpty()){
            for(Event e: this.waitingQueue){
                time += e.getTime();
            }
        }
        return time;
    }

    public int getQueueSize() {
        return this.waitingQueue.size();
    }

    public Queue<Event> getQueue() {
        return waitingQueue;
    }

    public Server releaseServer() {
        return new Server(this.id, ServerState.IDLE, this.waitingQueue, this.nextAvailableTime);
    }

    //Check if this server can serve the next customer
    //customer time + service time = time when done
    //if time when done is less than the next customer time
    boolean canServe() {
        return this.state != ServerState.SERVING;
    }

}
