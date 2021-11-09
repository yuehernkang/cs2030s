
public Logger<Integer> add(Logger<Integer> a, int b) {
    return a.map(x -> x + b);
}

//The sum of non-negative integers from 0 to n (inclusive of both)
//can be computed recursively using
Logger<Integer> sum(int n) {
    if (n == 0) {
        return 0;
    } else {
        return add(new Logger(0), ;
    }
}
