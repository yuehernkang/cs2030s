import java.util.function.Supplier;
import java.util.function.Function;

class LazyList<T> {
    //Properties of lazy list
    final Supplier<T> head;
    final Supplier<LazyList<T>> tail;

    private LazyList(Supplier<T> head, Supplier<LazyList<T>> tail) {
        this.head = head;
        this.tail = tail;
    }

    //Function to print out the first 5 values of the list
    //Terminal Function, should take in a consumer, then see how it want
    //to consumer
    //CHECK SLIDE 14, LECTURE 9 
    void forEach() {
        LazyList<T> curr = this;
        for(int i = 0; i < 5; i++) {
            //Print the head, prepare for next traversal
            System.out.println(curr.head.get());
            curr = curr.tail.get();
        }
    }

    static <T> LazyList<T> iterate(T seed, Function<T,T> next) {
        //Deal with head
        Supplier<T> newHead = () -> seed;
        //Next is the function to generate the next elements
        Supplier<LazyList<T>> newTail = () -> 
            LazyList.<T>iterate(next.apply(seed), next);
                    
        return new LazyList<T>(newHead, newTail);
    }

    <R> LazyList<R> map(Function<? super T, ? extends R> mapper) {
        //Apply to whatever value in the head
        Supplier<R> newHead = () -> mapper.apply(this.head.get());
        Supplier<LazyList<R>> newTail = () -> this.tail.get().map(mapper);
        return new LazyList<R>(newHead, newTail);
    }

}
