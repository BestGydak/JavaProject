package Course;

import java.util.ArrayList;

public class Course {
    public final String Name;
    public final ArrayList<Module> Modules;
    public final int MaxActivityScore;
    public final int MaxExerciseScore;
    public final int MaxHomeWorkScore;

    public Course(String name, ArrayList<Module> modules, int maxActivityScore, int maxExerciseScore, int maxHomeWorkScore) {
        Name = name;
        Modules = modules;
        MaxActivityScore = maxActivityScore;
        MaxExerciseScore = maxExerciseScore;
        MaxHomeWorkScore = maxHomeWorkScore;
    }
}
