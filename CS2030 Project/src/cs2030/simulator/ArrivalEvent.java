package cs2030.simulator;

import java.util.function.Supplier;

public class ArrivalEvent extends Event {
    public ArrivalEvent(int id, double time, int serverId, EventState eventState,
                        Supplier<Double> serviceTime, CustomerType customerType) {
        super(id, time, serverId, eventState, serviceTime, customerType);
    }

    @Override
    public String toString() {
        String greedyState = super.getCustomerType() == CustomerType.GREEDY ? "(greedy)" : "";
        return String.format("%.3f", super.getTime()) + " "
                + super.getId() + greedyState + " arrives";
    }
}
