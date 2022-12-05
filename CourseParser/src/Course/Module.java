package Course;

import java.util.ArrayList;

public class Module {
    public final Course Course;
    public final String Name;
    public final ArrayList<Task> Tasks;
    public final int MaxActivityScore;
    public final int MaxExerciseScore;
    public final int MaxHomeWorkScore;

    public Module(Course course, String name, ArrayList<Task> tasks
            , int maxActivityScore, int maxExerciseScore, int maxHomeWorkScore) {
        Course = course;
        Tasks = tasks;
        MaxActivityScore = maxActivityScore;
        MaxHomeWorkScore = maxHomeWorkScore;
        MaxExerciseScore = maxExerciseScore;
        Name = name;
    }

}
