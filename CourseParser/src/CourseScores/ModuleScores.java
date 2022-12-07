package CourseScores;

import Course.Module;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.HashMap;

public class ModuleScores {
    public final Module Module;
    private final HashMap<String, TaskScores> tasksScores;
    private int activitiesScore;
    private int exercisesScore;
    private int homeworksScore;
    private int seminarsScore;
    private int bonusScore;

    public ModuleScores(Module module) {
        Module = module;
        tasksScores = new HashMap<>();
        for(var task : Module.Tasks) {
            tasksScores.put(task.Name, new TaskScores(task, 0));
        }
    }

    public void setTaskScores(int value, String taskName)
    {
        if (!tasksScores.containsKey(taskName))
            throw new IllegalArgumentException("tasksScores doesn't contain TaskScores named " + taskName);
        tasksScores.get(taskName).setScore(value);
    }

    public TaskScores getTaskScores(String taskName) {
        if (!tasksScores.containsKey(taskName))
            throw new IllegalArgumentException("tasksScores doesn't contain TaskScores named " + taskName);
        return tasksScores.get(taskName);
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
        this.activitiesScore = checkCorrectValue(value, Module.MaxActivityScore);
    }

    public void setExercisesScore(int value) {
        this.exercisesScore = checkCorrectValue(value, Module.MaxExerciseScore);
    }

    public void setHomeworksScore(int value) {
        this.homeworksScore = checkCorrectValue(value, Module.MaxHomeWorkScore);
    }

    public void setSeminarsScore(int value) {
        this.seminarsScore = checkCorrectValue(value, Module.MaxSeminarScore);
    }

    public void setBonusScore(int value){
        bonusScore = value;
    }

    //endregion
    private int checkCorrectValue(int value, int max) {
        if (value < 0)
            throw new IllegalArgumentException("Value cannot be less than zero!");
        if (value > max)
            throw new IllegalArgumentException("Value cannot be greater than max!");
        return value;
    }

    @Override
    public String toString() {
        return String.format("ModuleScore (%s) \n" +
                "activities: %s, exercises: %s, homework: %s, seminars: %s, bonus: %s",
                Module, getActivitiesScore(), getExercisesScore(), getHomeworksScore(), getSeminarsScore(), getBonusScore());
    }

    public String getFullInfo(){
        var stringBuilder = new StringBuilder();
        stringBuilder.append(this);
        stringBuilder.append("\n");
        stringBuilder.append(String.format("Contains %s tasks:", tasksScores.size()));
        for(var task : tasksScores.keySet()){
            stringBuilder.append("\n");
            stringBuilder.append(tasksScores.get(task));
        }
        stringBuilder.append("\n");
        return stringBuilder.toString();
    }
}

