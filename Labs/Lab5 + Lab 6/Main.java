import java.util.Scanner;
import java.util.List;
import java.util.ArrayList;
import java.io.File;
import java.io.FileNotFoundException;

public class Main {
    private static final int ASSIGNMENT = 3;

    /**
     * Main Method.
     */

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int num = sc.nextInt();
        sc.nextLine();
        ArrayList<Student> list = new ArrayList<Student>();
        Roster roster = new Roster("AY2021");
        for (int i = 0; i < num; i++) {

            String[] item = sc.nextLine().split("\\s+");
            Student s = new Student(item[0]);
            Module m = new Module(item[1]);
            Assessment a = new Assessment(item[2], item[ASSIGNMENT]);

            roster.get(s.getKey()).ifPresentOrElse(
                o -> o.get(m.getKey()).ifPresentOrElse(k -> k
                    .put(new Assessment(a.getKey(), a.getGrade())),
                    () -> o.put(new Module(m.getKey())
                            .put(new Assessment(a.getKey(), a.getGrade())))),
                () -> roster.put(new Student(s.getKey())
                            .put(new Module(m.getKey())
                            .put(new Assessment(a.getKey(), a.getGrade())))));
        }
        while (sc.hasNext()) {
            String[] item = sc.nextLine().split("\\s+");
            System.out.println(roster.getGrade(item[0], item[1], item[2]));
        }
    }
}