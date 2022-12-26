import CSVParser.CourseSheetParser;
import ChartCreator.ChartCreator;
import SQLUtil.SQLCourseFiller;
import VkUtils.VkConnector;

import java.io.IOException;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException, SQLException {
        var sc = new Scanner(System.in);
        System.out.println("Выберите команду: \n" +
                "1) Создать новую базу данных \n" +
                "2) Создать графики по существующей базе");
        var mode = sc.nextLine();
        if(mode.equals("1")){

            System.out.print("Введите название .csv файла: ");
            var csvFileName = sc.nextLine();
            System.out.print("Введите название базы данных: ");
            var dbName = sc.nextLine();
            System.out.print("Введите id: ");
            var id = Integer.parseInt(sc.nextLine());
            System.out.print("Введите token: ");
            var token = sc.nextLine();
            var vkParser = new VkConnector(id, token);
            var CSParser = new CourseSheetParser(vkParser);
            var students = CSParser.parseCourseSheet("CSHARP",
                    csvFileName);
            var course = students.get(0).AssignedCourseScores.Course;
            //System.out.println(course.getFullInfo());
            var sqlFiller = new SQLCourseFiller(dbName);
            sqlFiller.addCourse(course);
            sqlFiller.addStudents(students);
            sqlFiller.close();
        }
        else if(mode.equals("2")){
            System.out.print("Введите название базы данных: ");
            var db = sc.nextLine();
            var co = DriverManager.getConnection("jdbc:sqlite:" + db);
            ChartCreator.boysGirlsChart(co);
            ChartCreator.homeTownChart(co);
            ChartCreator.averageModulePerformanceChart(co);
            ChartCreator.performanceChart(co);
            ChartCreator.boysGirlsPerformanceChart(co);
            ChartCreator.GroupsChart(co);
            ChartCreator.exerciseHomeWorkChart(co);
            ChartCreator.performanceChartByType(co, "У1");
            ChartCreator.performanceChartByType(co, "У2");
            ChartCreator.homeworkExercisePerformance(co);
        }

    }


}
