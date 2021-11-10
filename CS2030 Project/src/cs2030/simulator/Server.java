package cs2030.simulator;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
/**
 * Event class
 *

 */
public class Server {
    /**
     * Constructor for Server class
     * @param  id                   id of the server
     * @param  state                state of the server
     * @param  serverType           type of server
     * @param  waitingQueue         queue of events waiting for this server
     * @param  nextAvailableTime    the time when the server is available again
     * @return                      returns a new Server object with the parameters
     *
     */
    private final int id;
    private final ServerState state;
    private final ServerType serverType;
    private final LinkedList<Event> waitingQueue;
    private final double nextAvailableTime;

    /**
     * Constructor to used when creating initial server to assign id
     *
     * @param id assigned id from Simulator class
     *
     */
    public Server(int id) {
        this.id = id;
        this.state = ServerState.IDLE;
        this.serverType = ServerType.HUMAN;
        this.waitingQueue = new LinkedList<>();
        this.nextAvailableTime = 0;
    }

    /**
     * Constructor to used when creating self-checkout server
     *
     * @param id assigned id from Simulator class
     * @param serverType assigned id from Simulator class
     *
     */
    public Server(int id, ServerType serverType) {
        this.id = id;
        this.state = ServerState.IDLE;
        this.serverType = serverType;
        this.waitingQueue = new LinkedList<>();
        this.nextAvailableTime = 0;
    }

    //Server that start serving
    public Server(int id, ServerState state, ServerType serverType, LinkedList<Event> waitingQueue, double nextAvailableTime) {
        this.id = id;
        this.state = state;
        this.serverType = serverType;
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
        return nextAvailableTime;
    }

    public ServerType getServerType() {
        return serverType;
    }

    public int getQueueSize() {
        return this.waitingQueue.size();
    }

    /**
     * Returns true if queue is empty;
     *
     */
    public boolean queueIsEmpty() {
        return this.waitingQueue.size() == 0;
    }

    /**
     * Checks if the customer is able to queue at this server
     *
     * @param maxQueueLength max queue length
     *
     */
    public boolean canQueue(int maxQueueLength) {
        return this.waitingQueue.size() >= maxQueueLength;
    }

    public LinkedList<Event> getQueue() {
        return this.waitingQueue;
    }

    /**
     * Release server after serving is done
     *
     * @param restTime rest time of server
     *
     */
    public Server releaseServer(double restTime) {
        return new Server(this.id, ServerState.IDLE, this.serverType, this.waitingQueue, this.nextAvailableTime + restTime);
    }

    /**
     * Checks if the server is able to serve a customer at the specified time
     *
     * @param time  time when the server is required to serve
     *
     */

    boolean canServe(double time) {
        boolean result = false;
        if(this.getServerState()!= ServerState.SERVING && this.nextAvailableTime <= time) {
            result = true;
        }
        return result;
    }

}
