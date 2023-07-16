package finaltask.manager;

import finaltask.task.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private static final int HISTORY_SIZE = 10;
    private final LinkedList<Task> history = new LinkedList<>();
    @Override
    public List<Task> getHistory() {
        return List.copyOf(history);
    }

    @Override
    public void addTask(Task task) {
        if(task==null){
            return;
        }
        if(history.size()>=HISTORY_SIZE){
            history.removeFirst();
        }
        history.add(task);
    }
}
