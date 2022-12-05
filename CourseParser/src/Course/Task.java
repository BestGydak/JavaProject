package Course;

public class Task {
    public final TaskType Type;
    public final String Name;
    public final Module Module;
    public final int MaxScore;
    public Task(Module module, TaskType type, String name, int maxScore) {
        Type = type;
        Name = name;
        Module = module;
        MaxScore = maxScore;
    }
}
