class SmallCruise extends Cruise {
    //REQUIREMENTS:
    //1. has an identifier that starts with an uppercase
    //character S;
    //2. takes a fixed 30 minutes for a loader to fully
    //load;
    //3. requires only one loader for it to be fully served;

    private static final int TIME_TAKEN_FOR_LOADER = 30;
    private static final int LOADERS_REQUIRED = 1;

    SmallCruise(String identifier, int arrivalTime) {
      super(identifier, arrivalTime, LOADERS_REQUIRED, TIME_TAKEN_FOR_LOADER);
        
      if (identifier.charAt(0) != 'S') {
            throw new IllegalArgumentException("SMALL CRUISE NAME MUST START WITH S");    
      } 
    }

}
