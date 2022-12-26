package ChartCreator;

import org.javatuples.Pair;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.renderer.category.BarRenderer;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;

import org.jfree.chart.ChartUtils;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.util.*;
import java.sql.Connection;

public class ChartCreator {
    public static void boysGirlsChart(Connection co) throws SQLException {
        try{
            var dataset = new DefaultPieDataset();
            var boysCount = 0;
            var girlsCount = 0;
            var st = co.createStatement();
            var rs = st.executeQuery("SELECT gender, count(student_id) AS count FROM students GROUP BY gender");
            while (rs.next()){
                if(rs.getString("gender").equals("FEMALE")){
                    girlsCount = rs.getInt("count");
                } else if (rs.getString("gender").equals("MALE")) {
                    boysCount = rs.getInt("count");
                }
            }
            rs.close();
            st.close();

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
        catch (Exception e){
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void homeTownChart(Connection co){
        try{
            var dataset = new DefaultCategoryDataset();
            var towns = new ArrayList<Pair<String, Integer>>();
            var st = co.createStatement();
            var rs = st.executeQuery("SELECT home_town, count(student_id) as count FROM students GROUP BY home_town");

            while(rs.next()){
                towns.add(new Pair<>(rs.getString("home_town"), rs.getInt("count")));
            }

            towns.stream()
                    .filter(entry -> !entry.getValue0().equals("null") && !entry.getValue0().equals("unknown") && !entry.getValue0().equals(""))
                    .sorted(new Comparator<>() {
                        @Override
                        public int compare(Pair<String, Integer> o1, Pair<String, Integer> o2) {
                            return o2.getValue1().compareTo(o1.getValue1());
                        }
                    })
                    .limit(10)
                    .forEach(entry -> dataset.addValue(entry.getValue1(), entry.getValue0(), entry.getValue0()));
            rs.close();
            st.close();
            var chart = ChartFactory.createBarChart(
                    "Численность по городам",
                    "Города",
                    "Численность",
                    dataset);
            chart.getLegend().setHeight(1000);
            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setItemMargin(-5);
            createPNGChart(chart, "towns.png", 2000, 500);
        }
        catch (Exception e){
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void performanceChart(Connection co){
        try{
            var dataset = new DefaultPieDataset();
            var awfulPerformanceCount = 0;
            var decentPerformanceCount = 0;
            var goodPerformanceCount = 0;
            var excellentPerformanceCount = 0;

            var st = co.createStatement();
            var rs = st.executeQuery("SELECT * FROM coursesScoresInfo");

            while(rs.next()) {
                var studentScore = rs.getInt("full_score");
                var courseId = rs.getInt("course_id");
                var courseSt = co.createStatement();
                var courseRs = courseSt.executeQuery("SELECT full_score FROM coursesInfo " +
                        "WHERE course_id = " + courseId);
                courseRs.next();
                var maxScore = courseRs.getInt("full_score");
                var performance = (double) studentScore / maxScore;
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
            rs.close();
            st.close();
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
        catch (Exception e){
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void averageModulePerformanceChart(Connection co){
        try{
            var dataset = new DefaultCategoryDataset();
            var performanceString = "Средний процент успеваемости";
            var modulesSt = co.createStatement();
            var modulesRs = modulesSt.executeQuery("SELECT module_id FROM modulesInfo");
            while(modulesRs.next()){
                var moduleId = modulesRs.getInt("module_id");
                double performancesSum = 0;
                int performancesCount = 0;
                var studentsSt = co.createStatement();
                var studentsRs = studentsSt.executeQuery("SELECT student_id FROM students");
                while (studentsRs.next()){
                    var studentId = studentsRs.getInt(1);
                    var maxScoreSt = co.createStatement();
                    var maxScoreRs = maxScoreSt.executeQuery("SELECT full_score FROM modulesInfo " +
                            "WHERE module_id = " + moduleId);
                    maxScoreRs.next();
                    var maxScore = maxScoreRs.getInt(1);
                    maxScoreRs.close();
                    maxScoreSt.close();

                    var courseScoresSt = co.createStatement();
                    var courseScoresRs = courseScoresSt.executeQuery("SELECT course_scores_id FROM coursesScoresInfo " +
                            "WHERE student_id = " + studentId);
                    courseScoresRs.next();
                    var courseScoresId = courseScoresRs.getInt(1);
                    courseScoresRs.close();
                    courseScoresSt.close();

                    var studentScoreSt = co.createStatement();
                    var studentScoreRs = studentScoreSt.executeQuery("SELECT full_score FROM modulesScoresInfo " +
                            "WHERE module_id = " + moduleId + " AND course_scores_id = " + courseScoresId);
                    studentScoreRs.next();
                    var studentsScore = studentScoreRs.getInt(1);
                    studentScoreRs.close();
                    studentScoreSt.close();

                    var performance = (double) studentsScore / maxScore * 100;
                    performancesSum += performance;
                    performancesCount++;
                }
                studentsRs.close();
                studentsSt.close();

                var moduleNameSt = co.createStatement();
                var moduleNameRs = moduleNameSt.executeQuery("SELECT name FROM modulesInfo " +
                        "WHERE module_id = " + moduleId);
                moduleNameRs.next();
                var moduleName = moduleNameRs.getString(1);
                moduleNameRs.close();
                moduleNameSt.close();
                var result = performancesSum /  performancesCount;
                dataset.addValue(result, moduleName, moduleName);
            }

            var chart = ChartFactory.createBarChart(
                    "Успеваемость по модулям",
                    "Модули",
                    "Успеваемость",
                    dataset);
            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setItemMargin(-10);
            createPNGChart(chart, "modulePerformanceChart.png", 1000, 1000);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void boysGirlsPerformanceChart(Connection co) {
        try{
            var dataset = new DefaultCategoryDataset();
            double boysPerformanceSum = 0;
            int boysCount = 0;
            double girlsPerformanceSum = 0;
            int girlsCount = 0;

            var studentsSt = co.createStatement();
            var studentsRs = studentsSt.executeQuery("SELECT student_id, gender FROM students");
            while (studentsRs.next()){
                var gender = studentsRs.getString("gender");
                var studentId = studentsRs.getInt("student_id");

                if(gender.equals("MALE")){
                    boysPerformanceSum += getStudentPerformance(studentId, co);
                    boysCount++;
                }
                else if(gender.equals("FEMALE")){
                    girlsPerformanceSum += getStudentPerformance(studentId, co);
                    girlsCount++;
                }
            }
            studentsRs.close();
            studentsSt.close();
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
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void GroupsChart(Connection co){
        try{
            var dataset = new DefaultCategoryDataset();
            var groupPerformanceMap = new HashMap<String, Pair<Double, Integer>>();

            var studentsSt = co.createStatement();
            var studentsRs = studentsSt.executeQuery("SELECT student_id, group_name FROM students");
            while (studentsRs.next()){
                var studentId = studentsRs.getInt("student_id");
                var performance = getStudentPerformance(studentId, co);
                var groupName = studentsRs.getString("group_name");
                if(!groupPerformanceMap.containsKey(groupName)){
                    groupPerformanceMap.put(groupName, new Pair<>(performance, 1));
                }
                else{
                    var oldPair = groupPerformanceMap.get(groupName);
                    groupPerformanceMap.put(groupName, new Pair<>(oldPair.getValue0() + performance, oldPair.getValue1() + 1));
                }
            }
            for(var groupName : groupPerformanceMap.keySet()){
                var pair = groupPerformanceMap.get(groupName);
                var performance = pair.getValue0() / pair.getValue1();
                dataset.addValue(performance, groupName, groupName);
            }
            studentsRs.close();
            studentsSt.close();
            var chart = ChartFactory.createBarChart(
                    "Средняя успеваемость по группам",
                    "Группа",
                    "Успеваемость",
                    dataset);

            BarRenderer renderer = (BarRenderer) chart.getCategoryPlot().getRenderer();
            renderer.setItemMargin(-10);
            createPNGChart(chart, "groupPerformance.png", 2000, 1000);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void homeworkExercisePerformance(Connection co){
        try{
            var dataset = new DefaultCategoryDataset();
            var moduleIdSt = co.createStatement();
            var moduleIdRs = moduleIdSt.executeQuery("SELECT module_id, exercise_score, homework_score, name FROM modulesInfo");
            while(moduleIdRs.next()){
                var moduleId = moduleIdRs.getInt("module_id");
                var moduleName = moduleIdRs.getString("name");
                var maxHomeworkScore = moduleIdRs.getInt("homework_score");
                var maxExerciseScore = moduleIdRs.getInt("exercise_score");

                double sumHomeworkPerformance = 0;
                double sumExercisePerformance = 0;

                var count = 0;

                var scoresSt = co.createStatement();
                var scoresRs = scoresSt.executeQuery("SELECT exercise_score, homework_score FROM modulesScoresInfo " +
                        "WHERE module_id = " + moduleId);
                while(scoresRs.next()){
                    sumHomeworkPerformance += (double) scoresRs.getInt("homework_score") / maxHomeworkScore * 100;
                    sumExercisePerformance += (double) scoresRs.getInt("exercise_score") / maxExerciseScore * 100;
                    count += 1;
                }
                scoresRs.close();
                scoresSt.close();

                var homeworkPerformance = sumHomeworkPerformance / count;
                var exercisePerformance = sumExercisePerformance / count;

                dataset.addValue(homeworkPerformance, "Домашняя работа", moduleName);
                dataset.addValue(exercisePerformance,"Упражнение", moduleName);
            }

            var chart = ChartFactory.createBarChart("Успеваемость студентов по упражнениям/практикам",
                    "Модуль",
                    "Успеваемость",
                    dataset);

            createPNGChart(chart, "homeworkExercisePerformance.png", 2000, 1000);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void exerciseHomeWorkChart(Connection co){
        try{
            var series = new XYSeries("Ученик");

            var studentsSt = co .createStatement();
            var studentsRs = studentsSt.executeQuery("SELECT homework_score, exercise_score, course_id FROM coursesScoresInfo");
            while (studentsRs.next()){
                var exerciseScore = studentsRs.getInt("exercise_score");
                var homeworkScore = studentsRs.getInt("homework_score");
                var courseId = studentsRs.getInt("course_id");

                var courseSt = co.createStatement();
                var courseRs = courseSt.executeQuery("SELECT exercise_score, homework_score FROM coursesInfo " +
                        "WHERE course_id = " + courseId);

                var maxExerciseScore = courseRs.getInt("exercise_score");
                var maxHomeworkScore = courseRs.getInt("homework_score");

                courseSt.close();
                courseRs.close();
                series.add( (double) exerciseScore / maxExerciseScore * 100
                        ,(double) homeworkScore / maxHomeworkScore * 100);
            }

            studentsRs.close();
            studentsSt.close();
            var dataset = new XYSeriesCollection(series);
            JFreeChart chart = ChartFactory.createScatterPlot(
                    "Соотношение баллов за упражнение и ДЗ",
                    "Упражнения",
                    "Домашние задания",
                    dataset);

            createPNGChart(chart, "exerciseHomeWork.png", 1000, 1000);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }

    public static void performanceChartByType(Connection co, String groupType){
        try{
            var dataset = new DefaultPieDataset();
            var awfulPerformanceCount = 0;
            var decentPerformanceCount = 0;
            var goodPerformanceCount = 0;
            var excellentPerformanceCount = 0;

            var st = co.createStatement();
            var rs = st.executeQuery("SELECT * FROM coursesScoresInfo");

            while(rs.next()) {
                var studentId = rs.getInt("student_id");

                var groupNameSt = co.createStatement();
                var groupNameRs = groupNameSt.executeQuery("SELECT group_name FROM students " +
                        "WHERE student_id = " + studentId);
                groupNameRs.next();
                var groupName = groupNameRs.getString("group_name");
                groupNameRs.close();
                groupNameSt.close();

                if(!groupName.contains(groupType)) continue;
                var studentScore = rs.getInt("full_score");
                var courseId = rs.getInt("course_id");
                var courseSt = co.createStatement();
                var courseRs = courseSt.executeQuery("SELECT full_score FROM coursesInfo " +
                        "WHERE course_id = " + courseId);
                courseRs.next();
                var maxScore = courseRs.getInt("full_score");
                var performance = (double) studentScore / maxScore;
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
            rs.close();
            st.close();
            dataset.setValue("0-40", awfulPerformanceCount);
            dataset.setValue("41-60", decentPerformanceCount);
            dataset.setValue("61-80", goodPerformanceCount);
            dataset.setValue("81-100", excellentPerformanceCount);
            var chart = ChartFactory.createPieChart(
                    "График успеваемости " + groupType,
                    dataset,
                    true,
                    true,
                    false);
            createPNGChart(chart, "PerformanceChart" + groupType  + ".png", 800, 800);
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var trace: e.getStackTrace()){
                System.out.println(trace);
            }
            throw new RuntimeException();
        }
    }
    private static void createPNGChart(JFreeChart chart, String pathname, int width, int height){
        try {
            ChartUtils.saveChartAsPNG(new File(pathname), chart, width, height);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    private static double getStudentPerformance(int studentId, Connection co) throws SQLException {
        var scoresSt = co.createStatement();
        var scoresRs = scoresSt.executeQuery("SELECT full_score, course_id FROM coursesScoresInfo " +
                "WHERE student_id = " + studentId);
        scoresRs.next();
        var studentScore = scoresRs.getInt("full_score");
        var courseId = scoresRs.getInt("course_id");
        scoresRs.close();
        scoresSt.close();

        var courseSt = co.createStatement();
        var courseRs = courseSt.executeQuery("SELECT full_score FROM coursesInfo " +
                "WHERE course_id = " + courseId);
        var maxScore = courseRs.getInt("full_score");
        courseRs.close();
        courseSt.close();

        return (double) studentScore / maxScore * 100;
    }
}
