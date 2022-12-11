package CSVParser;

import Course.Module;
import Course.Task;

import java.util.ArrayList;
import java.util.List;

public class ModuleBuilder {
    public String Name = "";
    public ArrayList<Task> Tasks = new ArrayList<>();
    public int MaxActivityScore = 0;
    public int MaxExerciseScore = 0;
    public int MaxHomeWorkScore = 0;
    public int MaxSeminarScore = 0;
    public int MaxBonusScore = 0;

    public Module toModule(){
        return new Module(Name, Tasks, MaxActivityScore, MaxExerciseScore, MaxHomeWorkScore, MaxSeminarScore, MaxBonusScore);
    }
}
