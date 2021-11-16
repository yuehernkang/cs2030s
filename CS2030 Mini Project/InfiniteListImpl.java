import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

public class InfiniteListImpl<T> implements InfiniteList<T> {
    private final Lazy<T> head;
    private final Supplier<InfiniteListImpl<T>> tail;

    private InfiniteListImpl(Lazy<T> head, Supplier<InfiniteListImpl<T>> tail) {
        this.head = head;
        this.tail = tail;
    }

    /**
     * 
     * @param <T>
     * @param supply supplies the function to generate the next values
     * @return
     */
    static <T> InfiniteListImpl<T> generate(Supplier<? extends T> supply) {
        return new InfiniteListImpl<T>(Lazy.of(supply), () -> InfiniteListImpl.generate(supply));
    }

    static <T> InfiniteListImpl<T> iterate(T seed, Function<T,T> next) {
        Lazy<T> newHead = Lazy.ofNullable(seed);
        Supplier<InfiniteListImpl<T>> newTail = () -> 
            InfiniteListImpl.<T>iterate(next.apply(seed), next);
        return new InfiniteListImpl<T>(newHead, newTail);
    }

    public InfiniteListImpl<T> filter(Predicate<? super T> predicate) {
        Lazy<T> newHead = this.head.filter(predicate);
        Supplier<InfiniteListImpl<T>> newTail = () -> 
            this.tail.get().filter(predicate);

        return new InfiniteListImpl<T>(newHead, newTail);
    }


    public <R> InfiniteListImpl<R> map(Function<? super T, ? extends R> mapper) {
        Lazy<R> newHead = this.head.map(mapper);
        Supplier<InfiniteListImpl<R>> newTail = () -> this.tail.get().map(mapper);
        return new InfiniteListImpl<R>(newHead, newTail);
    }

    public InfiniteList<T> peek() {
        this.head.get().map(value -> value)
            .ifPresentOrElse(value -> {System.out.println(value);}, ()-> {});
        return this.tail.get();
    }
}