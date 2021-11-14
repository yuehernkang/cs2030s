package cs2030.simulator;
import java.util.ArrayList;
import java.util.Optional;
import java.util.function.Supplier;

/**
 * Event class
 * EVENTS:
 * ARRIVE, SERVE, WAIT, LEAVE and DONE
 *
 * ARRIVE → SERVE → DONE
 * ARRIVE → WAIT → SERVE → DONE
 * ARRIVE → LEAVE
 *
 * ARRIVE:
 * ARRIVAL EVENT GENERATES A SERVE EVENT
 * ~SERVE~ IF THERE ARE AVAILABLE SERVERS,
 * ~WAIT~ IF THERE ARE NO AVAILABLE SERVERS
 * ~LEAVE~ ???
 *
 * SERVE EVENT:
 * GENERATE A DONE EVENT
 *
 * DONE EVENT:
 * GENERATE STATISTICS
 *
 */

public class Event {
    private final int id;
    private final double time;
    private final int serverId;
    private final EventState eventState;
    private final Supplier<Double> serviceTime;
    private ArrayList<Double> cache;
    private final CustomerType customerType;

    /**
     * Constructor for Event class
     * @param  id           id of the particular event
     * @param  time         time the event started
     * @param  serverId     server that is assigned to this event
     * @param  eventState   state of the event
     * @param  serviceTime  service time refers to how long it takes to complete the event
     * @return              returns a new Event object with the parameters
     */
    public Event(int id, double time, int serverId, EventState eventState, Supplier<Double> serviceTime) {
        this.id = id;
        this.time = time;
        this.serverId = serverId;
        this.eventState = eventState;
        this.serviceTime = serviceTime;
        this.cache = new ArrayList<>();
        this.customerType = CustomerType.NORMAL;
    }

    public Event(int id, double time, int serverId, EventState eventState, Supplier<Double> serviceTime, CustomerType customerType) {
        this.id = id;
        this.time = time;
        this.serverId = serverId;
        this.eventState = eventState;
        this.serviceTime = serviceTime;
        this.cache = new ArrayList<>();
        this.customerType = customerType;
    }

    public Event(int id, double time, int serverId, EventState eventState, Supplier<Double> serviceTime, ArrayList<Double> cache, CustomerType customerType) {
        this.id = id;
        this.time = time;
        this.serverId = serverId;
        this.eventState = eventState;
        this.serviceTime = serviceTime;
        this.cache = cache;
        this.customerType = customerType;
    }

    public Event() {
        this.id = 0;
        this.time = 0;
        this.serverId = 0;
        this.eventState = EventState.FINISH;
        this.serviceTime = () -> 0.0;
        this.cache = new ArrayList<>();
        this.customerType = CustomerType.NORMAL;
    }

    public int getServerId () { return this.serverId; }

    public EventState getEventState() { return this.eventState; }

    public double getServiceCompletionTime() {
        return this.time + this.getServiceTime();
    }

    public ArrayList<Double> getCache() {
        return this.cache;
    }

    public double getTime() {
        return this.time;
    }

    public int getId() {
        return this.id;
    }
    public CustomerType getCustomerType() {
        return this.customerType;
    }

    public double getServiceTime() {
        if (this.cache.isEmpty()) {
            this.cache.add(this.serviceTime.get());
        }
        return this.cache.get(0);
    }

//    @Override
//    public String toString() {
//        String statement = "";
//        String greedyState = this.customerType == CustomerType.GREEDY ? "(greedy)":"";
//        switch (this.eventState){
//            case ARRIVAL: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " arrives";
//                break;
//            }
//            case SERVE: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " serves by server " + this.getServerId();
//                break;
//            }
//            case SERVEBYSELFCHECKOUT: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " serves by self-check " + this.getServerId();
//                break;
//            }
//            case WAIT: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " waits at server " + this.getServerId();
//                break;
//            }
//            case WAITSELFCHECKOUT: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " waits at self-check " + this.getServerId();
//                break;
//            }
//            case DONESELFCHECKOUT: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " done serving by self-check " + this.getServerId();
//                break;
//            }
//            case DONE: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " done serving by server " + this.getServerId();
//                break;
//            }
//            case LEAVE: {
//                statement = String.format("%.3f", this.time) + " " + this.id + greedyState + " leaves";
//                break;
//            }
//        }
//        return statement;
//    }


}
