public class Roster extends KeyableMap<Student> {
    Roster(String key) {
        super(key);
    }

    @Override
    Roster put(Student s) {
        return (Roster) super.put(s);
    }

    String getGrade(String studentId, String moduleCode, String assessment) {
        return this.get(studentId)
        .flatMap(x -> x.get(moduleCode))
        .flatMap(x -> x.get(assessment))
        .map(Assessment::getGrade)
        .orElse(String.format("No such record: %s %s %s", studentId, moduleCode, assessment)); 
    }

}
