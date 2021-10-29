public class Student extends KeyableMap<Module> {
    Student(String name) {
        super(name);
    }

    @Override
    Student put(Module module) {
        return (Student) super.put(module);
    }
}
