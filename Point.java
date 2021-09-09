class Point {
    private final double x;
    private final double y;

    Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double distanceTo(Point p){
        double dx = p.x - this.x;
        double dy = p.y - this.y;
        double dist = Math.sqrt(dx * dx + dy * dy);
        return dist;
    }

    @Override 
    public String toString(){
        return "point (" + String.format("%.3f", this.x)
            + ", " + String.format("%.3f", this.y) + ")";
    }
}
