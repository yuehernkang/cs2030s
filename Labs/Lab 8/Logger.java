import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Supplier;

class Logger<T> {
    Supplier<T> t;
    Supplier<String> data;

    Logger(T t) {
        this.t = () -> t;
        this.data = () -> "";
    }

    Logger(Supplier<T> t, Supplier<String> data) {
        this.t = t;
        this.data = data;
    }

    Logger(T t, Supplier<String> data) {
        this.t = () -> t;
        this.data = data;
    }

    static <T> Logger<T> of(T t) {
        if (t instanceof Logger) {
            throw new IllegalArgumentException("already a Logger");
        }
        Optional<T> optional = Optional.ofNullable(t);
        // Cannot use null
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("argument cannot be null");
        }

        return new Logger<T>(t);
    }

    <U> Logger<U> map(Function<? super T, ? extends U> mapper) {
        Supplier<U> updatedValue = () -> mapper.apply(this.t.get());
        Supplier<String> updatedData = () -> this.data.get() + 
            String.format("\n%s -> %s", this.t.get(), updatedValue.get());
        Logger<U> newObject = new Logger<U>(updatedValue, updatedData);
        return newObject;
    }

    @Override
    public boolean equals(Object obj) {
        //SAME OBJECT IN MEMORY
        if (this == obj) {
            return true;
            //CHECK IF OBJECT IS A LOGGER
        } else if (obj instanceof Logger) {
            Logger<?> loggerObject = (Logger<?>) obj;
            //CHECK IF T AND THE LIST IS EQUAL
            return this.t.equals(loggerObject.t) && this.data.equals(loggerObject.data);
        } else {
            return false;
        }

    }



    <U> Logger<U> flatMap(Function<? super T,? extends Logger<? extends U>> mapper) { 
        Logger<? extends U> updated = mapper.apply(this.t.get());
        Supplier<String> newData = () -> (this.t.get().toString() + updated.t.get());
        return new Logger<U>(updated.t.get(), newData);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Logger[" + this.t.get().toString() + "]");
        if (!this.data.isEmpty()) {
            for (int i = 1; i < this.data.size(); i += 2) {
                sb.append("\n");
                sb.append(this.data.get(i - 1));
                sb.append(" -> ");
                sb.append(this.data.get(i));
            }
        }
        return sb.toString();
    }
}
