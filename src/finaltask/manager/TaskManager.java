package finaltask.manager;
import finaltask.manager.file.ManagerSaveException;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;

import java.util.List;

public interface TaskManager {

    public List<Task> getAllTasks() throws ManagerSaveException;
    public List<SubTask> getAllSubTasks() throws ManagerSaveException;

    public List<Epic> getAllEpics() throws ManagerSaveException;
    public void deleteAllTasks() throws ManagerSaveException;
    public void deleteAllSubTasks() throws ManagerSaveException;
    public void deleteAllEpics() throws ManagerSaveException;
    public Task getTaskById(int id) throws ManagerSaveException;
    public SubTask getSubTaskById(int id) throws ManagerSaveException;
    public Epic getEpicById(int epicId) throws ManagerSaveException;
    public Task createTask(Task task);
    public SubTask createSubTask(SubTask subTask);
    public Epic createEpic(Epic epic);
    public void updateTask(Task task);
    public void updateSubTask(SubTask subTask);
    public void updateEpic(Epic epic);
    public void deleteTaskById(int id) throws ManagerSaveException;
    public void deleteSubTaskById(int id) throws ManagerSaveException;
    public void deleteEpicById(int epicId) throws ManagerSaveException;
    public List<SubTask> getAllSubTasksByEpic(int idOfEpic) throws ManagerSaveException;
    public List<Task> getHistory();
}
