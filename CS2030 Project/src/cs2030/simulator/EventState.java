package cs2030.simulator;

/**
 *  EventState.
 *  Represents the possible states Event objects
 */

public enum EventState {
    ARRIVAL,
    SERVE,
    SERVEBYSELFCHECKOUT,
    WAIT,
    WAITSELFCHECKOUT,
    DONE,
    DONESELFCHECKOUT,
    LEAVE,
    FINISH,
}
