package cs2030.simulator;

import java.util.function.Supplier;

public class FinishEvent extends Event {
    public FinishEvent(int id, double time, int serverId, EventState eventState,
                       Supplier<Double> serviceTime, CustomerType customerType) {
        super(id, time, serverId, eventState, serviceTime, customerType);
    }
}
