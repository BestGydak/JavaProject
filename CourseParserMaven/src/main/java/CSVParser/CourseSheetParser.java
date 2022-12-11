package CSVParser;

import Course.Course;
import Course.Module;
import CourseScores.CourseScores;
import Person.Student;
import Course.TaskType;
import Course.Task;
import VkUtils.VkConnector;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CourseSheetParser {

    private final VkConnector vk;

    public CourseSheetParser(VkConnector vk){
        this.vk = vk;
    }
    public ArrayList<Student> parseCourseSheet(String courseName, String path) throws IOException {
        var lines = Files.readAllLines(Paths.get(path));
        var moduleNames = Arrays.stream(lines.get(0).split(";", -1)).skip(2).toList();
        var columnNames = Arrays.stream(lines.get(1).split(";")).skip(2).toList();
        var maxScores = Arrays.stream(lines.get(2).split(";")).skip(2).toList();
        var course = createNewCourse(courseName, moduleNames, columnNames, maxScores);
        var students = new ArrayList<Student>();
        for(var i = 3; i < 20; i++){
            var studentInfo = lines.get(i).split(";");
            var studentScores = Arrays.stream(studentInfo).skip(2).toList();
            var student = createNewStudent(studentInfo, course, moduleNames, columnNames);
            var courseScores = createCourseScores(course, moduleNames, columnNames, studentScores);
            student.AssignedCourseScores.add(courseScores);
            students.add(student);
        }
        return students;
    }

    private Student createNewStudent(String[] studentInfo, Course course, List<String> moduleNames, List<String> columnNames){
        var fullName = studentInfo[0].split(" ", 2);
        var name = fullName[0];
        var surname = "";
        if(fullName.length > 1) surname = fullName[1];
        var group = studentInfo[1];
        var vkUserInfo = vk.getUserInfo(studentInfo[0]);
        var dateFormat = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        return new Student(name,
                surname,
                vkUserInfo.get("Sex"),
                vkUserInfo.get("Phone"),
                vkUserInfo.get("Hometown"),
                LocalDate.parse(vkUserInfo.get("BDate"), dateFormat),
                group);
    }

    public Course createNewCourse(String courseName,
                                         List<String> moduleNames,
                                         List<String> columnNames,
                                         List<String> maxScores){
        var courseBuilder = new CourseBuilder();
        courseBuilder.Name = courseName;
        for(var i = 0;i < 4; i++){
            switch(convertStringToTaskType(columnNames.get(i))){
                case HOMEWORK -> courseBuilder.MaxHomeWorkScore = Integer.parseInt(maxScores.get(i));
                case SEMINAR -> courseBuilder.MaxSeminarScore = Integer.parseInt(maxScores.get(i));
                case EXERCISE -> courseBuilder.MaxExerciseScore = Integer.parseInt(maxScores.get(i));
                case ACTIVITY -> courseBuilder.MaxActivityScore = Integer.parseInt(maxScores.get(i));
                case BONUS -> courseBuilder.MaxBonusScore = Integer.parseInt(maxScores.get(i));
            }
        }
        var shortenedModuleNames = moduleNames.stream().skip(4).toList();
        var shortenedColumnNames = columnNames.stream().skip(4).toList();
        var shortenedMaxScores = maxScores.stream().skip(4).toList();
        courseBuilder.Modules = createModules(shortenedModuleNames,
                shortenedColumnNames,
                shortenedMaxScores);
        return courseBuilder.toCourse();
    }

    private ArrayList<Module> createModules(List<String> moduleNames,
                                                   List<String> columnNames,
                                                   List<String> maxScores) {
        var modules = new ArrayList<Module>();
        ModuleBuilder moduleBuilder = new ModuleBuilder();
        for (var i = 0; i < moduleNames.size(); i++){
            var moduleName = moduleNames.get(i);
            if(!moduleName.equals("")){
                if(i==0) {
                    moduleBuilder.Name = moduleName;
                }
                else{
                    modules.add(moduleBuilder.toModule());
                    moduleBuilder = new ModuleBuilder();
                    moduleBuilder.Name = moduleName;
                }
            }
            //System.out.println(columnNames.get(i));
            var taskInfo = columnNames.get(i).split(": ");
            var taskType = convertStringToTaskType(taskInfo[0]);
            if (taskInfo.length == 1){
                switch (taskType) {
                    case ACTIVITY -> moduleBuilder.MaxActivityScore = Integer.parseInt(maxScores.get(i));
                    case EXERCISE -> moduleBuilder.MaxExerciseScore = Integer.parseInt(maxScores.get(i));
                    case SEMINAR -> moduleBuilder.MaxSeminarScore = Integer.parseInt(maxScores.get(i));
                    case HOMEWORK -> moduleBuilder.MaxHomeWorkScore = Integer.parseInt(maxScores.get(i));
                    case BONUS -> moduleBuilder.MaxBonusScore = Integer.parseInt(maxScores.get(i));
                }
            }
            if (taskInfo.length == 2){
                var taskName = taskInfo[1];
                moduleBuilder.Tasks.add(new Task(taskType, taskName, Integer.parseInt(maxScores.get(i))));
            }
            if(taskInfo.length > 2){
                throw new IllegalArgumentException("File is incorrect: invalid task naming");
            }
        }
        modules.add(moduleBuilder.toModule());
        return modules;
    }

    private CourseScores createCourseScores(Course course,
                                                   List<String> moduleNames,
                                                   List<String> taskNames,
                                                   List<String> studentScores){
        var courseScores = new CourseScores(course);
        for(var i = 0; i< 4; i++){
            switch (convertStringToTaskType(taskNames.get(i))){
                case EXERCISE -> courseScores.setExercisesScore(Integer.parseInt(studentScores.get(i)));
                case ACTIVITY -> courseScores.setActivitiesScore(Integer.parseInt(studentScores.get(i)));
                case HOMEWORK -> courseScores.setHomeworksScore(Integer.parseInt(studentScores.get(i)));
                case SEMINAR -> courseScores.setSeminarsScore(Integer.parseInt(studentScores.get(i)));
            }
        }
        String currentModuleName = "";
        for(var i = 4; i < taskNames.size(); i++){
            var probableModuleName = moduleNames.get(i);
            if(!probableModuleName.equals("")){
                currentModuleName = probableModuleName;
            }
            var taskInfo = taskNames.get(i).split(": ");
            var taskType = convertStringToTaskType(taskInfo[0]);
            if(taskInfo.length == 1){
                switch (taskType){
                    case SEMINAR -> courseScores.getModuleScores(currentModuleName)
                            .setSeminarsScore(Integer.parseInt(studentScores.get(i)));
                    case BONUS -> courseScores.getModuleScores(currentModuleName)
                            .setBonusScore(Integer.parseInt(studentScores.get(i)));
                    case EXERCISE -> courseScores.getModuleScores(currentModuleName)
                            .setExercisesScore(Integer.parseInt(studentScores.get(i)));
                    case HOMEWORK -> courseScores.getModuleScores(currentModuleName)
                            .setHomeworksScore(Integer.parseInt(studentScores.get(i)));
                    case ACTIVITY -> courseScores.getModuleScores(currentModuleName)
                            .setActivitiesScore(Integer.parseInt(studentScores.get(i)));
                }
            }
            if(taskInfo.length == 2){
                var taskName = taskInfo[1];
                courseScores.getModuleScores(currentModuleName)
                        .getTaskScores(taskName)
                        .setScore(Integer.parseInt(studentScores.get(i)));
            }
            if(taskInfo.length > 2){
                throw new IllegalArgumentException("File is incorrect: invalid task naming");
            }
        }
        return courseScores;
    }
    private TaskType convertStringToTaskType(String taskStr){
        return switch (taskStr) {
            case "Акт" -> TaskType.ACTIVITY;
            case "Упр" -> TaskType.EXERCISE;
            case "ДЗ" -> TaskType.HOMEWORK;
            case "Сем" -> TaskType.SEMINAR;
            case "Доп" -> TaskType.BONUS;
            default -> throw new IllegalArgumentException("There is no TaskType named: " + taskStr);
        };
    }
}
