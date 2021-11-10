package cs2030.simulator;
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
    }

    public Event() {
        this.id = 0;
        this.time = 0;
        this.serverId = 0;
        this.eventState = EventState.FINISH;
        this.serviceTime = () -> 0.0;
    }

    public int getServerId () { return this.serverId; }

    public EventState getEventState() {return this.eventState;}

    public double getServiceCompletionTime() {
        return this.time + this.getServiceTime();
    }

    public double getTime() {
        return this.time;
    }

    public int getId() {
        return this.id;
    }

    public double getServiceTime() { return this.serviceTime.get(); }

    @Override
    public String toString() {
        String statement = "";
        switch (this.eventState){
            case ARRIVAL: {
                statement = String.format("%.3f", this.time) + " " + this.id + " arrives";
                break;
            }
            case SERVE: {
                statement = String.format("%.3f", this.time) + " " + this.id + " serves by server " + this.getServerId();
                break;
            }
            case SERVEBYSELFCHECKOUT: {
                statement = String.format("%.3f", this.time) + " " + this.id + " serves by self-check " + this.getServerId();
                break;
            }
            case WAIT: {
                statement = String.format("%.3f", this.time) + " " + this.id + " waits at server " + this.getServerId();
                break;
            }
            case WAITSELFCHECKOUT: {
                statement = String.format("%.3f", this.time) + " " + this.id + " waits at self-check " + this.getServerId();
                break;
            }
            case DONESELFCHECKOUT: {
                statement = String.format("%.3f", this.time) + " " + this.id + " done serving by self-check " + this.getServerId();
                break;
            }
            case DONE: {
                statement = String.format("%.3f", this.time) + " " + this.id + " done serving by server " + this.getServerId();
                break;
            }
            case LEAVE: {
                statement = String.format("%.3f", this.time) + " " + this.id + " leaves";
                break;
            }
        }
        return statement;
    }

}
