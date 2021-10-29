public class Module extends KeyableMap<Assessment> {
    Module(String key) {
        super(key);
    }

    @Override
    Module put(Assessment assessment) {
        return (Module) super.put(assessment);
    }
}
