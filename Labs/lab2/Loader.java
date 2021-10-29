class Loader {
    private final int identifier;
    private final Cruise cruise;

    Loader(int identifier, Cruise firstCruise) {
        this.identifier = identifier;
        this.cruise = firstCruise;
    }

    //return true if the loader is available to
    //serve the given cruise, or false otherwise
    boolean canServe(Cruise cruise) {
        if (this.cruise.getServiceCompletionTime() <= cruise.getArrivalTime()) {
            return true;
        } else {
        return false;
        }
    }

    //serve method to serve a given cruise, if
    //the loader is availble, the method returns
    //the loader serving this cruise, otherwise
    //the existing loader is returned
    Loader serve(Cruise cruise) {
        if (canServe(cruise)) {
            return new Loader(this.identifier, cruise);
        } else {
            return this;
        }
        
    }

    int getNextAvailableTime() {
        return this.cruise.getServiceCompletionTime();
    }

    int getIdentifier() {
        return this.identifier;
    }

    public String toString() {
        return "Loader " + this.identifier + " serving " + this.cruise;
    }
}
