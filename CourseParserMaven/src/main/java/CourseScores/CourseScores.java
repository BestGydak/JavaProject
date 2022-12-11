package CourseScores;

import Course.Course;

import java.util.HashMap;

public class CourseScores {
    public final Course Course;
    private final HashMap<String, ModuleScores> modulesScores;

    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;
    private int bonusScore;

    public CourseScores(Course course) {
        Course = course;
        modulesScores = new HashMap<>();
        for(var module : course.Modules){
            if(modulesScores.containsKey(module.Name))
                throw new IllegalArgumentException("Several modules has identical names!:" + module.Name);
            modulesScores.put(module.Name, new ModuleScores(module));
        }
    }

    public ModuleScores getModuleScores(String name) {
        if(!modulesScores.containsKey(name))
            throw new IllegalArgumentException("modulesScores doesn't contain ModuleScores named " + name);
        return modulesScores.get(name);
    }
    public int getActivitiesScore() {
        return activitiesScore;
    }

    public int getExercisesScore() {
        return exercisesScore;
    }

    public int getHomeworksScore() {
        return homeworksScore;
    }

    public int getSeminarsScore() {
        return seminarsScore;
    }
    public int getBonusScore(){
        return bonusScore;
    }
    public int getFullScore() {
        return getActivitiesScore() + getExercisesScore() + getHomeworksScore() + getSeminarsScore() + getBonusScore();
    }

    public void setActivitiesScore(int value) {
        this.activitiesScore = checkCorrectValue(value, Course.MaxActivityScore);
    }

    public void setExercisesScore(int value) {
        this.exercisesScore = checkCorrectValue(value, Course.MaxExerciseScore);
    }

    public void setHomeworksScore(int value) {
        this.homeworksScore = checkCorrectValue(value, Course.MaxHomeWorkScore);
    }

    public void setSeminarsScore(int value) {
        this.seminarsScore = checkCorrectValue(value, Course.MaxSeminarScore);
    }

    public void setBonusScore(int value){
        this.bonusScore = value;
    }

    private int checkCorrectValue(int value, int max) {
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than zero!");
        if (value > max)
            throw new IllegalArgumentException("Value cannot be greater than max!");
        return value;
    }

    @Override
    public String toString() {
        return String.format("CourseScores (%s) \n" +
                "activities: %s, exercises: %s, homework: %s, seminar: %s, bonus: %s",
                Course, getActivitiesScore(), getExercisesScore(), getHomeworksScore(), getHomeworksScore(), getBonusScore());
    }

    public String getFullInfo(){
        var stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        stringBuilder.append("\n");
        stringBuilder.append(String.format("Contains %s modules: \n", modulesScores.size()));
        for(var module : modulesScores.keySet()){
            stringBuilder.append("\n");
            stringBuilder.append(modulesScores.get(module).getFullInfo());
        }
        return stringBuilder.toString();
    }
}
