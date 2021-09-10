class Loader {
    private final int identifier;
    private final Cruise cruise;

    Loader(int identifier, Cruise firstCruise) {
        this.identifier = identifier;
        this.cruise = firstCruise;
    }

    boolean canServe(Cruise cruise) {
        if (this.cruise.getServiceCompletionTime() <= cruise.getServiceCompletionTime()) {
            return true;
        }
        return false;
    }

    Loader serve(Cruise cruise) {
        if (canServe(cruise)) {
            return new Loader(this.identifier, cruise);
        }
        
    }

    int getNextAvailableTime() {
        return cruise.getServiceCompletionTime();
    }

    int getIdentifier() {
        return this.identifier;
    }

    public String toString() {
        return "Loader " + this.identifier + " serving " + this.cruise;
    }
}
