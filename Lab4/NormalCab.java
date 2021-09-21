public class NormalCab extends Driver {
    
    NormalCab(String license, int waitingTime) {
        super(license, waitingTime);
    }

    String getServiceName() {
        return "NormalCab";
    }


    public String toString() {
        return super.license + " (" + super.waitingTime + " mins away) " + "NormalCab";
    }
}
