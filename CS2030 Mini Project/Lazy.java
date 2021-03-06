import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class Lazy<T> {
    private final Supplier<? extends T> supplier;
    private boolean inCache;
    //CACHE CANNNOT BE FINAL BECAUSE IT IS THE GET THAT WILL MUTATE THE CACHE
    private Optional<T> cache;

    private Lazy(Supplier<? extends T> supplier) {
        this.supplier = supplier;
        this.inCache = false;
        this.cache = Optional.<T>empty();
    }

    private Lazy(T v) {
        this.supplier = () -> v;
        this.inCache = true;
        this.cache = Optional.<T>ofNullable(v);
    }

    static <T> Lazy<T> ofNullable(T v) {
        /*creates a lazy object with a given value (precomputed) v. 
        Note that the Lazy object can contain a null value.*/
        return new Lazy<T>(v);
    }

    Optional<T> get() {
        //Check if its in cache, it not use supplier get 
        if (!this.inCache) {
            this.cache = Optional.<T>ofNullable(this.supplier.get());
            this.inCache = true;
        }
        return this.cache;
    }

    static <T> Lazy<T> of(Supplier<? extends T> supplier) {
        return new Lazy<T>(Optional.<Supplier<? extends T>>ofNullable(supplier).orElseThrow());
    }

    <R> Lazy<R> map(Function<? super T, ? extends R> mapper) {
        return Lazy.<R>of(() -> this.get().map(mapper).orElse(null));
    }

    Lazy<T> filter(Predicate<? super T> predicate) {
        return Lazy.<T>of(() -> this.get().filter(predicate).orElse(null));
    }

    @Override
    public String toString() {
        String notInCacheString = this.inCache ? "Lazy[null]" : "Lazy[?]";
        return this.cache.map(v -> String.format("Lazy[%s]", v)).orElse(notInCacheString);

    }
}
