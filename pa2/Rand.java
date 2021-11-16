import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Stream;
import java.util.Random;

class Rand<T> {
    private final int seed;
    private final Function<Integer, T> func;

    
    Rand(int seed, Function<Integer, T> function) {
        this.seed = seed;
        this.func = function;
    }

    Rand(int seed, Optional<Function<Integer, T>> func) {
        this.seed = seed;
        this.func = func.get();
    }

    public static Rand<Integer>  of(int seed) {
        return new Rand<>(seed, x -> x);
    }

    Stream<T> stream() {
        return Stream.iterate(seed, x -> Rand.of(x).next().seed).map(x -> func.apply(x));
    }

    public static <T> Stream<T> randRange(int newSeed, Function<Integer, T> mapper) {
        return Rand.of(newSeed).stream().map(mapper);
    }

    T get() {
        return this.func.apply(seed);
    }

    <R> Rand<R> map(Function<T, R> mapper) {
        Rand<R> newRand = new Rand<>(seed, mapper.compose(this.func));
        return newRand;
    }

    <R> Rand<R> flatMap(Function<T, Rand<R>> mapper) {
        return map(mapper.andThen(x -> x.get()));
    }

    void forEach(Consumer<? super Number> consumer) {
        
    }

    Rand<T> next() {
        Random r = new Random(this.seed);
        int value = r.nextInt(Integer.MAX_VALUE);
        return new Rand<>(value, this.func);
    }

    @Override
    public String toString() {
        return "Rand";
    }
}
