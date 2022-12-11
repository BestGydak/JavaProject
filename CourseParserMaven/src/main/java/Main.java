import CSVParser.CourseSheetParser;
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
        var students = CSParser.parseCourseSheet("Сишарпик", "src/main/resources/new.csv");
        for(var s : students){
            System.out.println(s);
        }
    }
}
