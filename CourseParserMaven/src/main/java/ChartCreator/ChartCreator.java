package ChartCreator;

import Person.Student;
import org.javatuples.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import org.jfree.chart.ChartUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Objects;


public class ChartCreator {
    public static void boysGirlsChart(ArrayList<Student> students){
        var dataset = new DefaultPieDataset();
        var boysCount = 0;
        var girlsCount = 0;
        for(var student : students){
            if (student.Gender.equals("MALE")){
                boysCount += 1;
            } else if (student.Gender.equals("FEMALE")) {
                girlsCount += 1;
            }
        }

        dataset.setValue("Женщины", girlsCount);
        dataset.setValue("Мужчины", boysCount);
        var chart = ChartFactory.createPieChart(
                "Мужчины/Женщины",
                dataset,
                true,
                true,
                false);

        createPNGChart(chart, "BoysAndGirls.png", 500, 500);
    }

    public static void HomeTownChart(ArrayList<Student> students){
        var dataset = new DefaultCategoryDataset();
        var townMap = new HashMap<String, Integer>();
        for(var student : students){
            if(Objects.isNull(student.HomeTown) || student.HomeTown.equals("unknown")) continue;
            if(!townMap.containsKey(student.HomeTown)){
                townMap.put(student.HomeTown, 1);
            }
            else {
                townMap.put(student.HomeTown, townMap.get(student.HomeTown) + 1);
            }
        }
        for(var townKey : townMap.keySet()){
            dataset.addValue(townMap.get(townKey), townKey, townKey);
        }
        var chart = ChartFactory.createBarChart(
                "Численность по городам",
                "Города",
                "Численность",
                dataset);

        chart.getLegend().setHeight(1000);
        createPNGChart(chart, "towns.png", 2000, 500);
    }

    public static void performanceChart(ArrayList<Student> students){
        var dataset = new DefaultPieDataset();
        var awfulPerformanceCount = 0;
        var decentPerformanceCount = 0;
        var goodPerformanceCount = 0;
        var excellentPerformanceCount = 0;
        for(var student : students) {
            var performance = (double) student.AssignedCourseScores.getFullScore() / student.AssignedCourseScores.Course.MaxFullScore;
            if (0 <= performance && performance <= 0.4) {
                awfulPerformanceCount++;
            } else if (0.4 < performance && performance <= 0.6) {
                decentPerformanceCount++;
            } else if (0.6 < performance && performance <= 0.8) {
                goodPerformanceCount++;
            } else if (0.8 < performance && performance <= 1) {
                excellentPerformanceCount++;
            }


        }
        dataset.setValue("0-40", awfulPerformanceCount);
        dataset.setValue("41-60", decentPerformanceCount);
        dataset.setValue("61-80", goodPerformanceCount);
        dataset.setValue("81-100", excellentPerformanceCount);
        var chart = ChartFactory.createPieChart(
                "График успеваемости",
                dataset,
                true,
                true,
                false);
        createPNGChart(chart, "PerformanceChart.png", 800, 800);
    }

    public static void averageModulePerformanceChart(ArrayList<Student> students){
        var dataset = new DefaultCategoryDataset();
        var performanceString = "Средний процент успеваемости";
        var modules = students.get(0).AssignedCourseScores.Course.Modules;
        for(var module : modules){
            double performancesSum = 0;
            int performancesCount = 0;
            for(var student : students){
                var points = (double)student.AssignedCourseScores.getModuleScores(module.Name).getFullScore();
                var performance = points / module.MaxFullScore * 100;
                performancesSum += performance;
                performancesCount++;
            }

            dataset.addValue( performancesSum / performancesCount, module.Name, module.Name);
        }

        var chart = ChartFactory.createBarChart(
                "Успеваемость по блокам",
                "Модули",
                "Успеваемость",
                dataset);
        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-10);
        createPNGChart(chart, "modulePerformanceChart.png", 1000, 1000);
    }

    public static void BoysGirlsPerformanceChart(ArrayList<Student> students) {
        var dataset = new DefaultCategoryDataset();
        double boysPerformanceSum = 0;
        int boysCount = 0;
        double girlsPerformanceSum = 0;
        int girlsCount = 0;
        for (var student : students){
            if(student.Gender.equals("MALE")){
                boysPerformanceSum += getStudentPerformance(student);
                boysCount++;
            }
            else if(student.Gender.equals("FEMALE")){
                girlsPerformanceSum += getStudentPerformance(student);
                girlsCount++;
            }
        }

        var boysPerformance =  boysPerformanceSum / boysCount * 100;
        var girlsPerformance = girlsPerformanceSum / girlsCount * 100;

        dataset.addValue(boysPerformance, "Мужчины", "Мужчины");
        dataset.addValue(girlsPerformance, "Женщины", "Женщины");

        var chart = ChartFactory.createBarChart(
                "Средняя успеваемость мужчин и женщин",
                "Пол",
                "Успеваемость",
                dataset);
        createPNGChart(chart, "boysVSgirlsChart.png", 600, 600);
    }

    public static void GroupsChart(ArrayList<Student> students){
        var dataset = new DefaultCategoryDataset();
        var groupPerformanceMap = new HashMap<String, Pair<Double, Integer>>();
        for(var student : students){
            var performance = getStudentPerformance(student);
            if(!groupPerformanceMap.containsKey(student.GroupName)){
                groupPerformanceMap.put(student.GroupName, new Pair<>(performance, 1));
            }
            else{
                var oldPair = groupPerformanceMap.get(student.GroupName);
                groupPerformanceMap.put(student.GroupName, new Pair<>(oldPair.getValue0() + performance, oldPair.getValue1() + 1));
            }
        }
        for(var groupName : groupPerformanceMap.keySet()){
            var pair = groupPerformanceMap.get(groupName);
            var performance = pair.getValue0() / pair.getValue1();
            dataset.addValue(performance, groupName, groupName);
        }

        var chart = ChartFactory.createBarChart(
                "Средняя успеваемость по группам",
                "Группа",
                "Успеваемость",
                dataset);

        BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
        renderer.setItemMargin(-10);
        createPNGChart(chart, "groupPerformance.png", 2000, 1000);
    }

    public static void ExerciseHomeWorkChart(ArrayList<Student> students){
        var series = new XYSeries("Ученик");
        for (var student : students){
            series.add( (double) student.AssignedCourseScores.getExercisesScore() / student.AssignedCourseScores.Course.MaxExerciseScore * 100
                    ,(double) student.AssignedCourseScores.getHomeworksScore() / student.AssignedCourseScores.Course.MaxHomeWorkScore * 100);
        }
        var dataset = new XYSeriesCollection(series);
        JFreeChart chart = ChartFactory.createScatterPlot(
                "Соотношение баллов за упражнение и ДЗ",
                "Упражнения",
                "Домашние задания",
                dataset);

        createPNGChart(chart, "exerciseHomeWork.png", 1000, 1000);
    }
    private static void createPNGChart(JFreeChart chart, String pathname, int width, int height){
        try {
            ChartUtils.saveChartAsPNG(new File(pathname), chart, width, height);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static double getStudentPerformance(Student student){
        var course = student.AssignedCourseScores.Course;
        var points = (double) student.AssignedCourseScores.getFullScore();
        return points / course.MaxFullScore * 100;
    }

}
