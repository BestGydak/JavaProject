package CSVParser;

import Course.Module;
import Course.Course;

import java.util.ArrayList;
import java.util.List;

public class CourseBuilder {
    public String Name = "";
    public ArrayList<Module> Modules = new ArrayList<>();
    public int MaxActivityScore = 0;
    public int MaxExerciseScore = 0;
    public int MaxHomeWorkScore = 0;
    public int MaxSeminarScore = 0;
    public int MaxBonusScore = 0;

    public Course toCourse(){
        return new Course(Name, Modules, MaxActivityScore, MaxExerciseScore, MaxHomeWorkScore, MaxSeminarScore, MaxBonusScore);
    }
}
