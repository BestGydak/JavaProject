package Course;

public class Task {
    public final TaskType Type;
    public final String Name;
    public final int MaxScore;
    public Task(TaskType type, String name, int maxScore) {
        Type = type;
        Name = name;
        MaxScore = maxScore;
    }

    @Override
    public String toString() {
        return String.format("Task (%s, type: %s, max score: %s)", Name, Type.name(), MaxScore);
    }
}
