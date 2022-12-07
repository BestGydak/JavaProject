package Person;

import CourseScores.CourseScores;

import java.time.LocalDate;
import java.util.ArrayList;

public class Student extends Person {
    public final String GroupName;
    public final ArrayList<CourseScores> AssignedCourseScores = new ArrayList<>();
    public Student(String name, String surname, String gender, String phone, String homeTown, LocalDate birthDate, String groupName) {
        super(name, surname, gender, phone, homeTown, birthDate);
        GroupName = groupName;
    }

}
