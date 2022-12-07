package Course;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Module {
    public final String Name;
    public final List<Task> Tasks;
    public final int MaxActivityScore;
    public final int MaxExerciseScore;
    public final int MaxHomeWorkScore;
    public final int MaxSeminarScore;
    public final int MaxBonusScore;

    public Module(String name, ArrayList<Task> tasks
            , int maxActivityScore, int maxExerciseScore, int maxHomeWorkScore, int maxSeminarScore, int maxBonusScore) {
        Tasks = Collections.unmodifiableList(tasks);
        MaxActivityScore = maxActivityScore;
        MaxHomeWorkScore = maxHomeWorkScore;
        MaxExerciseScore = maxExerciseScore;
        MaxSeminarScore = maxSeminarScore;
        MaxBonusScore = maxBonusScore;
        Name = name;
    }

    @Override
    public String toString() {
        return String.format("Module (%s, max activity score: %s," +
                " max exercise score: %s," +
                " max homework score: %s," +
                " max seminar score: %s)", Name, MaxActivityScore, MaxExerciseScore, MaxHomeWorkScore, MaxSeminarScore);
    }

    public String getFullInfo(){
        var stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        stringBuilder.append("\n");
        stringBuilder.append(String.format("Contains %s tasks:", Tasks.size()));
        for(var task : Tasks){
            stringBuilder.append("\n");
            stringBuilder.append(task);
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}