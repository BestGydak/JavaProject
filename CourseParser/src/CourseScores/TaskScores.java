package CourseScores;

import Course.Task;

public class TaskScores {
    public final Task Task;
    private int score;

    public TaskScores(Task task, int score){
        this.score = score;
        Task = task;
    }

    public void setScore(int value)
    {
        if(value < 0){
            throw new IllegalArgumentException("Score can't be less than zero!");
        }
        if(value > Task.MaxScore)
        {
            throw new IllegalArgumentException("Score can't be greater than max score of the task!");
        }
        score = value;
    }

    public int getScore(){
        return score;
    }
}
