package cs2030.simulator;

import java.util.Comparator;

public class EventComparator implements Comparator<Event> {

    @Override
    public int compare(Event e1, Event e2) {
        if(e1.getTime() == e2.getTime()){
            return Integer.compare(e1.getId(), e2.getId());
        } else {
            return Double.compare(e1.getTime(), e2.getTime());
        }
    }

}