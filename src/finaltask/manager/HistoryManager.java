package finaltask.manager;
import finaltask.task.Task;

import java.util.List;

public interface HistoryManager {
    List<Task> getHistory();
    void addTask(Task task);
}
