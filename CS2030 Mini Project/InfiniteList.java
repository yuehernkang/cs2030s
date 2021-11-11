public interface InfiniteList {
    final Supplier<T> head;
    final Supplier<LazyList<T>> tail;

    private InfiniteList(Supplier<T> head, Supplier<InfiniteList<T>> tail) {
        this.head = head;
        this.tail = tail;
    }

}