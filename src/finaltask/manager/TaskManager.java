package finaltask.manager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;

import java.util.List;

public interface TaskManager {

    public List<Task> getAllTasks();
    public List<SubTask> getAllSubTasks();

    public List<Epic> getAllEpics();
    public void deleteAllTasks();
    public void deleteAllSubTasks();
    public void deleteAllEpics();
    public Task getTaskById(int id);
    public SubTask getSubTaskById(int id);
    public Epic getEpicById(int epicId);
    public Task createTask(Task task);
    public SubTask createSubTask(SubTask subTask);
    public Epic createEpic(Epic epic);
    public void updateTask(Task task);
    public void updateSubTask(SubTask subTask);
    public void updateEpic(Epic epic);
    public void deleteTaskById(int id);
    public void deleteSubTaskById(int id);
    public void deleteEpicById(int epicId);
    public List<SubTask> getAllSubTasksByEpic(int idOfEpic);
    public List<Task> getHistory();
}
