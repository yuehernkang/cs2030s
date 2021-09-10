class Rectangle {
    protected final double height;
    protected final double width;

    Rectangle(double height, double width) {
        this.height = height;
        this.width = width;
    }

    double getArea() {
        return this.height * this.width;
    }

    @Override
    public String toString() {
        return "Rectangle: " + this.height + "x " + this.width;
    }
}

class Square extends Rectangle{
    Square(double length) {
        super(length, length);
    }
    
    @Override
    public String toString() {
        return "Square: " + super.height + " x " + super.width;
    }

}
