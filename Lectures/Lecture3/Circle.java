class Circle {
    protected final double radius;

    Circle(double radius) {
        this.radius = radius; 
    }

    double getRadius() {
        return this.radius;
    }

    double getArea() {
        return Math.PI * this.radius * this.radius;
    }

    boolean equals (Circle circle) {
        return this.radius == circle.radius;
    }

    double getPerimeter() { 
        return 2 * Math.PI * this.radius;
    }

    public String toString() {
        return "area " + String.format("%.2f", this.getArea()) + ", perimeter " + String.format("%.2f", this.getPerimeter());
    }
}
