import CSVParser.CourseSheetParser;

import java.io.IOException;
import java.util.Arrays;

public class Main {
    public static void main(String[] args) throws IOException {
        var students = CourseSheetParser.parseCourseSheet("Сишарпик", "src/Resources/new.csv");
        var sampleStudent = students.get(0);
        System.out.println(sampleStudent.AssignedCourseScores.get(0).getFullInfo());
    }
}
