package SQLUtil;

import Course.Course;
import Course.Module;
import Course.Task;
import CourseScores.ModuleScores;
import CourseScores.TaskScores;
import Person.Student;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;


public class SQLCourseFiller {
    private final Connection co;
    private int tasksAdded = 0;
    public SQLCourseFiller(String dbName) {
        try{
            co = DriverManager.getConnection("jdbc:sqlite:" + dbName);
            createCoursesTable();
            createModulesTable();
            createTasksTable();
            createStudentsTable();
            createCoursesScoresTable();
            createModulesScoresTable();
            createTasksScoresTable();
        }
        catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }
    }

    public void addStudents(ArrayList<Student> students) {
        try{
            for(var student : students){
                addStudent(student);
            }
        }
        catch (Exception e){
            System.out.println(e.getMessage());
            for(var n : e.getStackTrace()){
                System.out.println(n);
            }
            throw new RuntimeException();
        }
    }

    public void addStudent(Student student) throws SQLException {
        var query = "INSERT INTO students " +
                "(name, surname, gender, phone, home_town, bdate, group_name) " +
                "VALUES ('" + student.Name + "', " +
                "'" + student.Surname + "', " +
                "'" + student.Gender + "', " +
                "'" + student.PhoneNumber + "', " +
                "'" + student.HomeTown + "', " +
                "'" + student.BirthDate + "', " +
                "'" + student.GroupName + "')";
        updateQuery(query);
        //var idQuery = "SELECT student_id FROM students " +
        //        "WHERE name = '" + student.Name + "' AND surname = '" + student.Surname + "' AND group_name  = '" + student.GroupName + "'";
        var idQuery = "SELECT last_insert_rowid()";
        var statement = co.createStatement();
        var rs = statement.executeQuery(idQuery);
        rs.next();
        var studentId = rs.getInt(1);
        rs.close();
        statement.close();
        addCourseScores(student, studentId);


    }

    public void addCourseScores(Student student, int studentId) throws SQLException{
        var courseScores = student.AssignedCourseScores;
        var idQuery = "SELECT course_id FROM coursesInfo " +
                "WHERE name = '" + courseScores.Course.Name + "'";
        var statement = co.createStatement();
        var resultSet = statement.executeQuery(idQuery);
        resultSet.next();
        var courseId = resultSet.getInt("course_id");
        resultSet.close();
        statement.close();
        var query = "INSERT INTO coursesScoresInfo " +
                "(full_score, activity_score, exercise_score, homework_score, seminar_score, bonus_score, course_id, student_id) " +
                "VALUES (" + courseScores.getFullScore() + ", " +
                courseScores.getActivitiesScore() + ", " +
                courseScores.getExercisesScore() + ", " +
                courseScores.getHomeworksScore() + ", " +
                courseScores.getSeminarsScore() + ", " +
                courseScores.getBonusScore() + ", " +
                courseId + ", " +
                studentId + ")";
        updateQuery(query);
        //var courseScoresIdQuery = "SELECT course_scores_id FROM coursesScoresInfo " +
        //        "WHERE course_id = " + courseId + " AND student_id = " + studentId;
        var courseScoresIdQuery = "SELECT last_insert_rowid()";
        var st = co.createStatement();
        var rs = st.executeQuery(courseScoresIdQuery);
        rs.next();
        //System.out.println(rs);
        var courseScoresId = rs.getInt(1);
        rs.close();
        st.close();
        for(var module : courseScores.Course.Modules){
            var moduleScores = courseScores.getModuleScores(module.Name);
            addModuleScores(moduleScores, courseScoresId, courseId);
        }


    }

    private void addModuleScores(ModuleScores moduleScores, int courseScoresId, int courseId) throws SQLException {
        var moduleIdQuery = "SELECT module_id FROM modulesInfo " +
               "WHERE name = '" + moduleScores.Module.Name + "' AND course_id = " + courseId;
        var st = co.createStatement();
        var rs = st.executeQuery(moduleIdQuery);
        rs.next();
        var moduleId = rs.getInt("module_id");
        var query = "INSERT INTO modulesScoresInfo " +
                "(full_score, activity_score, exercise_score, homework_score, seminar_score, bonus_score, course_scores_id, " +
                "module_id) " +
                "VALUES(" + moduleScores.getFullScore() + ", " +
                moduleScores.getActivitiesScore() + ", " +
                moduleScores.getExercisesScore() + ", " +
                moduleScores.getHomeworksScore() + ", " +
                moduleScores.getSeminarsScore() + ", " +
                moduleScores.getBonusScore() + ", " +
                courseScoresId + ", " +
                moduleId + ")";
        updateQuery(query);

        //var msQuery = "SELECT module_scores_id FROM modulesScoresInfo " +
        //       "WHERE course_scores_id = " + courseScoresId + " AND module_id = " + moduleId;
        var msQuery = "SELECT last_insert_rowid()";
        var msSt = co.createStatement();
        var msRs = msSt.executeQuery(msQuery);
        msRs.next();

        var modulesScoresId = msRs.getInt(1);
        for(var task : moduleScores.Module.Tasks){
            var taskScores = moduleScores.getTaskScores(task.Name);
            addTaskScores(taskScores, modulesScoresId, moduleId);
            tasksAdded++;
            if ((float) tasksAdded % 1000 == 0){
                System.out.println(tasksAdded);
            }
        }

    }

    private void addTaskScores(TaskScores taskScores, int moduleScoresId, int moduleId) throws SQLException {
        var taskIdQuery = "SELECT task_id FROM tasksInfo " +
                "WHERE module_id = " + moduleId + " AND name = '" + taskScores.Task.Name + "'";
        var taskSt = co.createStatement();
        var taskRs = taskSt.executeQuery(taskIdQuery);
        taskRs.next();
        var taskId = taskRs.getInt("task_id");
        taskRs.close();
        taskSt.close();
        var query = "INSERT INTO tasksScoresInfo " +
                "(score, task_id, module_scores_id) " +
                "VALUES (" + taskScores.getScore() + ", " + taskId + ", " + moduleScoresId + ")";
        updateQuery(query);
    }

    public void addCourse(Course course) throws SQLException {
        try{
            var query = "INSERT INTO coursesInfo " +
                    "(name, full_score, activity_score, exercise_score, homework_score, seminar_score, bonus_score) " +
                    "VALUES('" + course.Name + "', " + course.MaxFullScore + ", " + course.MaxActivityScore + ", " + course.MaxExerciseScore
                    + ", " + course.MaxHomeWorkScore + ", " + course.MaxSeminarScore + ", " + course.MaxBonusScore + ")";
            //System.out.println(query);
            updateQuery(query);
            var statement = co.createStatement();
            var resultSet = statement.executeQuery("SELECT * FROM coursesInfo " +
                    "WHERE name = " + "'" + course.Name + "'");
            var courseId = resultSet.getInt("course_id");
            resultSet.close();
            statement.close();
            for(var module : course.Modules){
                addModule(module, courseId);
            }
        }
        catch (Exception e){
            System.out.println(Arrays.toString(e.getStackTrace()));
            System.out.println(e.getMessage());
            throw new RuntimeException();
        }

    }

    private void addModule(Module module, int courseId) throws SQLException{
        var query = "INSERT INTO modulesInfo " +
                    "(name, full_score, activity_score, exercise_score, homework_score, seminar_score, bonus_score, course_id) " +
                    "VALUES('" + module.Name + "', " + module.MaxFullScore + ", " + module.MaxActivityScore + ", " + module.MaxExerciseScore
                    + ", " + module.MaxHomeWorkScore + ", " + module.MaxSeminarScore + ", " + module.MaxBonusScore + ", " + courseId + ")";
        //System.out.println(query);
        updateQuery(query);
        var idQuery = "SELECT module_id FROM modulesInfo " +
                "WHERE name = '" + module.Name + "' AND course_id = " + courseId;
        //System.out.println(idQuery);
        var statement = co.createStatement();
        var resultSet = statement.executeQuery(idQuery);
        var moduleId = resultSet.getInt("module_id");
        resultSet.close();
        statement.close();
        for(var task : module.Tasks){
            System.out.println(task.Name);
            addTask(task, moduleId);
        }
    }

    private void addTask(Task task, int moduleId) throws SQLException {
        var query = "INSERT INTO tasksInfo " +
                "(name, type, score, module_id) " +
                "VALUES('" + task.Name + "', '" + task.Type.name() + "', " + task.MaxScore + ", " + moduleId + ")";

        updateQuery(query);
    }

    private void createCoursesScoresTable() throws SQLException{
        var query = "CREATE TABLE coursesScoresInfo " +
                "(course_scores_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_score INTEGER, " +
                "activity_score INTEGER, " +
                "exercise_score INTEGER, " +
                "homework_score INTEGER, " +
                "seminar_score INTEGER, " +
                "bonus_score INTEGER, " +
                "course_id INTEGER, " +
                "student_id INTEGER, " +
                "FOREIGN KEY(course_id) REFERENCES courseInfo(course_id), " +
                "FOREIGN KEY(student_id) REFERENCES students(student_id))";
        System.out.println(query);
        updateQuery(query);
    }

    private void createModulesScoresTable() throws SQLException{
        var query = "CREATE TABLE modulesScoresInfo " +
                "(module_scores_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "full_score INTEGER, " +
                "activity_score INTEGER, " +
                "exercise_score INTEGER, " +
                "homework_score INTEGER, " +
                "seminar_score INTEGER, " +
                "bonus_score INTEGER, " +
                "course_scores_id INTEGER, " +
                "module_id INTEGER," +
                "FOREIGN KEY(course_scores_id) REFERENCES courseScoresInfo(course_scores_id), " +
                "FOREIGN KEY(module_id) REFERENCES modulesInfo(module_id))";
        updateQuery(query);
    }

    private void createTasksScoresTable() throws SQLException{
        var query = "CREATE TABLE tasksScoresInfo " +
                "(task_scores_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                "score INTEGER, " +
                "task_id INTEGER, " +
                "module_scores_id INTEGER, " +
                "FOREIGN KEY(task_id) REFERENCES tasksInfo(task_id), " +
                "FOREIGN KEY(module_scores_id) REFERENCES modulesScoresInfo(module_scores_id))";
        updateQuery(query);
    }

    private void createCoursesTable() throws SQLException {

        updateQuery("CREATE TABLE coursesInfo " +
                    "(course_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    "name VARCHAR(50), " +
                    "full_score INTEGER, " +
                    "activity_score INTEGER," +
                    "exercise_score INTEGER, " +
                    "homework_score INTEGER, " +
                    "seminar_score INTEGER, " +
                    "bonus_score INTEGER)");
    }

    private void createModulesTable() throws SQLException {
        updateQuery("CREATE TABLE modulesInfo " +
                "(module_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50), " +
                "full_score INTEGER, " +
                "activity_score INTEGER," +
                "exercise_score INTEGER, " +
                "homework_score INTEGER, " +
                "seminar_score INTEGER, " +
                "bonus_score INTEGER, " +
                "course_id INTEGER," +
                "FOREIGN KEY(course_id) REFERENCES coursesInfo(course_id))");

    }

    private void createTasksTable() throws SQLException {
        var query = "CREATE TABLE tasksInfo " +
                "(task_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50), " +
                "type VARCHAR(50), " +
                "score INTEGER, " +
                "module_id INTEGER, " +
                "FOREIGN KEY(module_id) REFERENCES modulesInfo(module_id))";
        //System.out.println(query);
        updateQuery(query);
    }

    private void createStudentsTable() throws SQLException {
        var query = "CREATE TABLE students " +
                "(student_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "name VARCHAR(50), " +
                "surname VARCHAR(50), " +
                "gender VARCHAR(50), " +
                "phone VARCHAR(50), " +
                "home_town VARCHAR(50), " +
                "bdate VARCHAR(50), " +
                "group_name VARCHAR(50))";
        //System.out.println(query);
        updateQuery(query);
    }

    public void close() throws SQLException {
        co.close();
    }

    private void updateQuery(String query) throws SQLException {
        var statement = co.createStatement();
        statement.executeUpdate(query);
        statement.close();
    }

    private ResultSet selectQuery(String query) throws SQLException {
        var statement = co.createStatement();
        return statement.executeQuery(query);
    }

}
