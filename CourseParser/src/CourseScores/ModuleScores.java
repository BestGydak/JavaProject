package CourseScores;

import Course.Module;

import java.util.ArrayList;
import java.util.Objects;

public class ModuleScores {
    public final Module Module;
    private final ArrayList<TaskScores> tasksScores;

    public ModuleScores(Module module) {
        Module = module;
        tasksScores = new ArrayList<>();
        for(var task : Module.Tasks) {
            tasksScores.add(new TaskScores(task, 0));
        }
    }

    public void setTaskScores(int value, String taskName)
    {
        for(var taskScores : tasksScores){
            if(taskName.equals(taskScores.Task.Name))
            {
                taskScores.setScore(value);
                break;
            }
        }
    }

    public TaskScores getTaskScores(String taskName) {
        for (var taskScores : tasksScores)
            if (taskName.equals(taskScores.Task.Name))
                return taskScores;
        throw new IllegalArgumentException("tasksScores doesn't contain TaskScores named " + taskName);
    }
}

