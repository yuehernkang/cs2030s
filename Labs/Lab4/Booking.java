public class Booking {
    private final Driver driver;
    private final Request request;

    Booking(Driver driver, Request request) {
        this.driver = driver;
        this.request = request;
    }

    int computeFare() {
        int fare = 0;
        if(driver.getServiceName() == "NormalCab"){
            int justRideFare = 0;
            int takeACabFare = 0;

            justRideFare = new JustRide().computeFare(this.request);
            takeACabFare = new TakeACab().computeFare(this.request);
            System.out.println("J: " + justRideFare + "\n");
            System.out.println("T: " + takeACabFare + "\n");
        }
        return 0;
    }
}
