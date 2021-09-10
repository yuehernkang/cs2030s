class Cruise {
    private final String identifier;
    private final int arrivalTime;
    private final int numOfLoader;
    private final int serviceTime;

    private static final int DIVIDE_BY = 100;
    private static final int MINUTE_IN_A_HOUR = 60;

    Cruise(String identifier, int arrivalTime, int numOfLoader, int serviceTime) {
        this.arrivalTime = arrivalTime;
        this.identifier = identifier;
        this.numOfLoader = numOfLoader;
        this.serviceTime = serviceTime;
    }
    

    int getServiceCompletionTime() {
        return this.getArrivalTime() + this.serviceTime;
    }

    int getArrivalTime() {
        //Return the arrival time (in
        //minutes), since midnight
        int hour = this.arrivalTime / DIVIDE_BY;
        int minutes = this.arrivalTime % DIVIDE_BY;
        int arrivalTime = (hour * MINUTE_IN_A_HOUR)  + minutes;
        return arrivalTime;
    }

    int getNumOfLoadersRequired() {
        return this.numOfLoader;
    }
    
    public String toString() {
        return this.identifier + "@" + String.format("%04d", this.arrivalTime);
    }
}
