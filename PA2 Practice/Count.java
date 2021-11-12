class Count<T> {
    private final T v;
    private final int count;

    Count(T v, int count) {
        this.v = v;
        this.count = count;
    }
    
    static<T> Count<T> of(T v, int count) {
        return new Count<T>(v, count);
    }

    @Override
    public String toString() {
        return "(" + this.count + ", " + v.toString() + ")";
    }
}