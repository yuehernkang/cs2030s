abstract class Rubik implements Cloneable {
    @Override
    public abstract Rubik clone();

    abstract int[][][] toGrid();

    abstract Rubik right();

    abstract Rubik left();
    
    abstract Rubik half();

    abstract Rubik rightView();

    abstract Rubik leftView();

    abstract Rubik upView();

    abstract Rubik downView();

    abstract Rubik backView();

    abstract Rubik frontView();

}