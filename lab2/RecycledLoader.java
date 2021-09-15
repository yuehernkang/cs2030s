class RecycledLoader extends Loader{
    private final int identifier;
    private final Cruise cruise;
    private static final int RECYCLED_LOADER_MAINTENANCE_TIME = 60;

    Loader(int identifier, Cruise firstCruise) {
        super(identifier, firstCruise);
    }

    //return true if the loader is available to
    //serve the given cruise, or false otherwise
    @Override
    boolean canServe(Cruise cruise) {
        if (this.cruise.getServiceCompletionTime() + RECYCLED_LOADER_MAINTENANCE_TIME <= cruise.getArrivalTime()) {
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
