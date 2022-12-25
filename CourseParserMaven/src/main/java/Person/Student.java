package Person;

import CourseScores.CourseScores;


public class Student extends Person {
    public final String GroupName;
    public CourseScores AssignedCourseScores;
    public Student(String name, String surname, String gender, String phone, String homeTown, String birthDate, String groupName) {
        super(name, surname, gender, phone, homeTown, birthDate);
        GroupName = groupName;
    }

}
