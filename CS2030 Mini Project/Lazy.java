import java.util.Optional;
import java.util.function.Function;

public class Lazy<T> {
    private final T t;
    private Optional<T> cache;

    Lazy(T t){
        this.t = t;
        this.cache = Optional.<T>empty();
    }

    static <T> Lazy<T> ofNullable(T v) {
        return new Lazy<T>(v);
    }

    Optional<T> get() {
        T v = this.cache.orElseGet(() -> t);
        return Optional.<T>of(v);
    }

    <R> Lazy<R> map(Function<? super T, ? extends R> mapper) {
        R newResult = mapper.apply(t);
        return Lazy.<R>ofNullable(newResult);
    }

    @Override
    public String toString() {
        return "Lazy[" + this.t.toString() + "]";
    }
}
