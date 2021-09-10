import java.lang.Math;

class BigCruise extends Cruise {
    //1. has an identifier that starts with an uppercase
    //character B;
    //2. takes one minute to serve every 50 passengers;
    //3. requires one loader per every 40 meters in length
    //of the cruise (or part thereof) to fully loaaded
    private static final int LOADER_PER_LENGTH = 40;
    private static final int PASSENGERS_PER_TIME = 50;
   
    BigCruise(String identifier, int arrivalTime, int lengthOfCruise, int numOfPassangers) {
        super(identifier, arrivalTime, 
                (int) Math.ceil((double)lengthOfCruise / LOADER_PER_LENGTH), 
                (int) Math.ceil((double)numOfPassangers / PASSENGERS_PER_TIME));

        //CHECK IF BIG CRUISE NAME START WITH A B
        if (identifier.charAt(0) != 'B') {
            throw new IllegalArgumentException("BIG CRUISE NAME MUST START WITH B");    
        }

    }
}
