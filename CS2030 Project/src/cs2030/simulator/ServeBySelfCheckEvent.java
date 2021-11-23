package cs2030.simulator;

import java.util.ArrayList;
import java.util.function.Supplier;

public class ServeBySelfCheckEvent extends Event {
    public ServeBySelfCheckEvent(int id, double time, int serverId, EventState eventState,
                                 Supplier<Double> serviceTime, ArrayList<Double> cache,
                                 CustomerType customerType) {
        super(id, time, serverId, eventState, serviceTime, cache, customerType);
    }

    @Override
    public String toString() {
        String greedyState = super.getCustomerType() == CustomerType.GREEDY ? "(greedy)" : "";
        return String.format("%.3f", super.getTime()) + " " + super.getId() + greedyState
                + " serves by self-check " + this.getServerId();
    }
}
