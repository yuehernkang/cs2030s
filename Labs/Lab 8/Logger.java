import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;

class Logger<T> {
    private final T t;
    private final List<String> data;

    Logger(T t) {
        if (t instanceof Logger) {
            throw new IllegalArgumentException("already a Logger");
        }
        Optional<T> optional = Optional.ofNullable(t);
        // Cannot use null
        if (optional.isEmpty()) {
            throw new IllegalArgumentException("argument cannot be null");
        }

        this.t = t;
        this.data = new ArrayList<>();
    }

    Logger(T t, List<String> data) {
        this.t = t;
        this.data = data;
    }

    static <T> Logger<T> of(T data) {
        return new Logger<T>(data);
    }

    <U> Logger<U> map(Function<? super T, ? extends U> mapper) {
        List<String> list = new ArrayList<>(this.data);
        U updatedValue = mapper.apply(this.t);
        list.add(this.t.toString());
        list.add(updatedValue.toString());
        Logger<U> newObject = new Logger<U>(updatedValue, list);
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
        Logger<? extends U> updated = mapper.apply(this.t);
        List<String> newList = new ArrayList<>();
        newList.addAll(this.data);
        newList.addAll(updated.data);
        return new Logger<U>(updated.t,newList);
    }


    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("Logger[" + this.t + "]");
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
