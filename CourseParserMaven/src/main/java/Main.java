import CSVParser.CourseSheetParser;
import ChartCreator.ChartCreator;
import SQLUtil.SQLCourseFiller;
import VkUtils.VkConnector;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        var sc = new Scanner(System.in);
        System.out.println("Выберите комманду: \n" +
                "1) наполнить БАЗУ \n" +
                "2) создать графики");
        var mode = sc.nextLine();
        if(mode.equals("1")){
            System.out.print("Введите id: ");
            var id = Integer.parseInt(sc.nextLine());
            System.out.print("Введите token: ");
            var token = sc.nextLine();
            var vkParser = new VkConnector(id, token);
            var CSParser = new CourseSheetParser(vkParser);
            var students = CSParser.parseCourseSheet("CSHARP",
                    "src/main/resources/basicprogramming_3.csv");
            var course = students.get(0).AssignedCourseScores.Course;
            //System.out.println(course.getFullInfo());
            var sqlFiller = new SQLCourseFiller("courseInfo1.db");
            sqlFiller.addCourse(course);
            sqlFiller.addStudents(students);
            sqlFiller.close();
        }
        /*ChartCreator.boysGirlsChart(students);
        ChartCreator.HomeTownChart(students);
        ChartCreator.averageModulePerformanceChart(students);
        ChartCreator.performanceChart(students);
        ChartCreator.BoysGirlsPerformanceChart(students);
        ChartCreator.GroupsChart(students);
        ChartCreator.ExerciseHomeWorkChart(students);*/
    }
}
