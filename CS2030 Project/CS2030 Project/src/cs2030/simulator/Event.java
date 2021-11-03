package cs2030.simulator;

/*
EVENTS:
ARRIVE, SERVE, WAIT, LEAVE and DONE

ARRIVE → SERVE → DONE
ARRIVE → WAIT → SERVE → DONE
ARRIVE → LEAVE

ARRIVE:
ARRIVAL EVENT GENERATES A SERVE EVENT
~SERVE~ IF THERE ARE AVAILABLE SERVERS,
~WAIT~ IF THERE ARE NO AVAILABLE SERVERS
~LEAVE~ ???

SERVE EVENT:
GENERATE A DONE EVENT

DONE EVENT:
GENERATE STATISTICS
*/

public class Event {
    private final int id;
    private final double time;
    private final int serverId;
    private final EventState eventState;
    private final double serviceTime;

    public Event(int id, double time, int serverId, EventState eventState, double serviceTime) {
        this.id = id;
        this.time = time;
        this.serverId = serverId;
        this.eventState = eventState;
        this.serviceTime = serviceTime;
    }

    public int getServerId () { return this.serverId; }

    public EventState getEventState() {return this.eventState;}

    public double getServiceCompletionTime() {
        return this.time + this.serviceTime;
    }

    public double getTime() {
        return this.time;
    }

    public int getId() {
        return this.id;
    }

    public double getServiceTime() { return this.serviceTime; }

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
