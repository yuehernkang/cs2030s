import java.util.Map;
import java.util.HashMap;
import java.util.Optional;

class KeyableMap<V extends Keyable> implements Keyable {
    private final String key;
    private final Map<String, V> map;

    KeyableMap(String key) {
        this.key = key;
        this.map = new HashMap<String, V>();
    }

    KeyableMap(String key, Map<String, V> map) {
        this.key = key;
        this.map = map;
    }

    KeyableMap<V> put(V item) {
        this.map.put(item.getKey(), item);
        return this;
    }

    Optional<V> get(String name) {
        return Optional.ofNullable(this.map.get(name));

    }

    boolean checkKey(String key) {
        return this.map.containsKey(key);
    }

    @Override
    public String getKey() {
        return this.key;
    }

    public String printMap() {
        String printThis = "";
        for (Map.Entry<String, V> e: this.map.entrySet()) {
            printThis += e.getValue() + ", ";
        }
        return printThis != "" ? printThis.substring(0, printThis.length() - 2) : printThis;
    }

    @Override 
    public String toString() { 
        // StringBuilder stringBuilder = new StringBuilder(this.key);
        // stringBuilder.append(": {");
        // stringBuilder.append(printMap());
        // stringBuilder.append("}");
        // return stringBuilder.toString();
        return this.key + ": {" + printMap() + "}";
    }

}
