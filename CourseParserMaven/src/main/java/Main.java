import CSVParser.CourseSheetParser;
import ChartCreator.ChartCreator;
import VkUtils.VkConnector;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws IOException {
        var sc = new Scanner(System.in);
        System.out.print("Введите id: ");
        var id = Integer.parseInt(sc.nextLine());
        System.out.print("Введите token: ");
        var token = sc.nextLine();
        var vkParser = new VkConnector(id, token);
        var CSParser = new CourseSheetParser(vkParser);
        var students = CSParser.parseCourseSheet("Сишарпик",
                "src/main/resources/basicprogramming_3.csv");
        ChartCreator.boysGirlsChart(students);
        ChartCreator.HomeTownChart(students);
        ChartCreator.averageModulePerformanceChart(students);
        ChartCreator.performanceChart(students);
        ChartCreator.BoysGirlsPerformanceChart(students);
        ChartCreator.GroupsChart(students);
        ChartCreator.ExerciseHomeWorkChart(students);
    }
}
