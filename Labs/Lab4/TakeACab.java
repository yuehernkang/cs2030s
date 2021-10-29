public class TakeACab implements Service {
    TakeACab() {

    }

    public int computeFare(Request request) {
        return 200 + (33 * request.getDistance());
    }

    @Override
    public String toString() {
        return "TakeACab";
    }
    
}
