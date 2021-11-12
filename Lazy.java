import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

class Lazy<T> {
    private final Supplier<? extends T> supplier;
    private Optional<T> cache;
    private boolean isEvaluated;

    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
        this.cache = Optional.<T>empty();
        this.isEvaluated = false;
    }

    private Lazy(T v) {
        this.supplier = () -> v;
        this.cache = Optional.<T>ofNullable(v);
        this.isEvaluated = true;
    }

    static <T> Lazy<T> ofNullable(T v) {
        return new Lazy<T>(v);
    }
    
    static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        // check if null is passed in as the supplier
        return new Lazy<T>(Optional.<Supplier<? extends T>>ofNullable(supplier).orElseThrow());
    }
    
    Optional<T> get() {
        if (!this.isEvaluated) {
            this.cache = Optional.<T>ofNullable(this.supplier.get());
            this.isEvaluated = true;
        }
        return this.cache;
    }
    
    <R> Lazy<R> map(Function<? super T, ? extends R> mapper) {
        return Lazy.<R>of(() -> this.get().map(mapper).orElse(null));
    }

    Lazy<T> filter(Predicate<? super T> predicate) {
        return Lazy.<T>of(() -> this.get().filter(predicate).orElse(null));
    }

    public String toString() {
        return this.cache.map(v -> String.format("Lazy[%s]", v)).orElse(this.isEvaluated ? "Lazy[null]" : "Lazy[?]");
    }
}