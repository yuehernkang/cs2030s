import java.util.function.Supplier;
import java.util.function.Function;
import java.util.Optional;

class Lazy<T> {
    //CACHE CANNNOT BE FINAL BECAUSE IT IS THE GET THAT WILL MUTATE THE CACHE
    private final Supplier<T> supplier;
    Optional<T> cache;

    private Lazy(Supplier<T> supplier) {
        this.supplier = supplier;
        this.cache = Optional.<T>empty();
    }

    static <T> Lazy<T> of(Supplier<T> supplier) {
        return new Lazy<T>(supplier);
    }

    // static <T> Lazy<T> ofNullable(T v) {

    // }

    static <T> LazyList<T> generate(Supplier<T> supplier) {
        Supplier<T> newHead = supplier;
        Supplier<LazyList<T>> newTail = () -> LazyList.<T>generate(supplier);
        return new LazyList<T>(newHead, newTail);
    }

    static <T> LazyList<T> iterate(T seed, Function<T,T> next) {
        Supplier<T> newHead = () -> seed;
        Supplier<LazyList<T>> newTail =
        () -> LazyList.<T>iterate(next.apply(seed), next);
        return new LazyList<T>(newHead, newTail);
    }

    //DEFINE R TYPE BECAUSE ITS NEW
    //ARGUMENTS: TAKES IN A FUNCTION, mapper
    <R> Lazy<R> map(Function<? super T, ? extends R> mapper) {
        //this.get() will get the value, we map it to some other VALUE
        //Supplier<R> r = () -> mapper.apply(this.get());
        Supplier<R> r = new Supplier<R>() {
            public R get() {
                return mapper.apply(Lazy.this.get());    
            }
        };
        return new Lazy<R>(r);
    }

    static <T> Lazy<T> of(T t) {
        return new Lazy<T>(() -> t);
    }

    T get() {
        //GET VALUE FROM Optional
        //IF THERE IS ALREADY A VALUE U RETURN THE VALUE
        T v = this.cache.orElseGet(this.supplier);
        cache = Optional.<T>of(v);
        return v;
    }

    // @Override
    // public String toString() {
    //     return 
    // }
}
