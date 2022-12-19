package Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Course {
    public final String Name;
    public final List<Module> Modules;
    public final int MaxActivityScore;
    public final int MaxExerciseScore;
    public final int MaxHomeWorkScore;
    public final int MaxSeminarScore;
    public final int MaxBonusScore;

    public final int MaxFullScore;

    public Course(String name, ArrayList<Module> modules, int maxActivityScore, int maxExerciseScore, int maxHomeWorkScore, int maxSeminarScore, int maxBonusScore) {
        Name = name;
        Modules = Collections.unmodifiableList(modules);
        MaxActivityScore = maxActivityScore;
        MaxExerciseScore = maxExerciseScore;
        MaxHomeWorkScore = maxHomeWorkScore;
        MaxSeminarScore = maxSeminarScore;
        MaxBonusScore = maxBonusScore;
        MaxFullScore = maxActivityScore + maxExerciseScore + maxHomeWorkScore + maxBonusScore + maxSeminarScore;
    }

    @Override
    public String toString() {
        return String.format("Course (%s, max activity score: %s," +
                " max exercise score: %s," +
                " max homework score: %s," +
                " max seminar score: %s," +
                " max bonus score %s", Name, MaxActivityScore, MaxExerciseScore, MaxHomeWorkScore, MaxSeminarScore, MaxBonusScore);
    }

    public String getFullInfo(){
        var stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        stringBuilder.append("\n");
        stringBuilder.append(String.format("Contains %s modules: \n", Modules.size()));
        for(var module : Modules){
            stringBuilder.append("\n");
            stringBuilder.append(module.getFullInfo());
        }
        return stringBuilder.toString();
    }
}
