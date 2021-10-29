public class JustRide implements Service {
    JustRide() {
        
    }

    @Override
    public int computeFare(Request request) {
        int fare = 0;
        if (request.getTime() >= 600 && request.getTime() <= 900) {
            fare += 500;
        }
        fare = 22 * request.getDistance();
        return fare; 
    }

    @Override
    public String toString() {
        return "JustRide";
    }
}
