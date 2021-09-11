import java.awt.Color;

class FilledCircle extends Circle {
    private final Color color;

    FilledCircle(double radius, Color color) {
        //CALL PARENTS CLASS CONSTRUCTOR
        //INVOKE PARENT'S CONSTRUCTOR
        super(radius);
        this.color = color;
    }

    FilledCircle fillColor (Color color) {
        //super.radius call to get the protected
        //modifier from parent class 
        return new FilledCircle(super.radius, color);
    }

    @Override
    public String toString() {
        return super.toString() + "," + this.color;
    }
}
