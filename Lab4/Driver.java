public abstract class Driver {
    protected final String license;
    protected final int waitingTime;

    Driver(String license, int waitingTime) {
        this.license = license;
        this.waitingTime = waitingTime;
    }

    abstract String getServiceName();
}
