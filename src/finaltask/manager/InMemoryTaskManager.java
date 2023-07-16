package finaltask.manager;

import finaltask.TaskStatus.TaskStatus;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {
    private int generatedID = 0;

    private HashMap<Integer, Task> taskMap = new HashMap<>();
    private HashMap <Integer, Epic> epicMap = new HashMap<>();
    private HashMap <Integer, SubTask> subTaskMap = new HashMap<>();
    private final HistoryManager historyManager = Managers.getHistoryDefault();

    @Override
    public List<Task> getAllTasks(){
        return new ArrayList(taskMap.values());
    }
    @Override
    public List<SubTask> getAllSubTasks(){
        return new ArrayList(subTaskMap.values());
    }
    @Override
    public List<Epic> getAllEpics(){
        return new ArrayList(epicMap.values());
    }
    @Override
    public void deleteAllTasks(){
        taskMap.clear();
    }
    @Override
    public void deleteAllSubTasks(){
        subTaskMap.clear();
        for(Integer key: epicMap.keySet()){
            epicMap.get(key).getSubTaskIdsArrayList().clear();
            epicMap.get(key).setStatus(TaskStatus.NEW);
        }
    }
    @Override
    public void deleteAllEpics(){
        subTaskMap.clear();
        epicMap.clear();
    }
    @Override
    public Task getTaskById(int id){
        Task task = taskMap.get(id);
        historyManager.addTask(task);
        return task;
    }
    @Override
    public SubTask getSubTaskById(int id){
        SubTask task = subTaskMap.get(id);
        historyManager.addTask(task);
        return task;
    }
    @Override
    public Epic getEpicById(int epicId){
        Epic task = epicMap.get(epicId);
        historyManager.addTask(task);
        return task;
    }
    @Override
    public Task createTask(Task task){
        int id = generateId();
        task.setID(id);
        taskMap.put(id, task);
        return task;
    }
    @Override
    public SubTask createSubTask(SubTask subTask){
        if(epicMap.get(subTask.getEpicId())==null){
            return subTask;
        } else {
            int subTaskId = generateId();
            subTask.setID(subTaskId);
            subTask.setEpicId(subTask.getEpicId());
            epicMap.get(subTask.getEpicId()).setSubTaskIdInArrayList(subTaskId);
            subTaskMap.put(subTaskId, subTask);
            return subTask;
        }
    }
    @Override
    public Epic createEpic(Epic epic){
        int id = generateId();
        epic.setID(id);
        epicMap.put(id, epic);
        return epic;
    }
    @Override
    public void updateTask(Task task){
        Task taskId = taskMap.get(task.getID());
        if(taskId == null){
            return;
        }
        taskMap.put(task.getID(), task);
    }
    @Override
    public void updateSubTask(SubTask subTask){
        SubTask saveId = subTaskMap.get(subTask.getID());
        if(saveId==null){
            return;
        }
        subTaskMap.put(subTask.getID(), subTask);
        checkEpicsStatusBySubtask(subTask.getEpicId());
    }
    @Override
    public void updateEpic(Epic epic){
        Epic epicId = epicMap.get(epic.getID());
        if(epicId==null){
            return;
        }
        epicMap.put(epic.getID(), epic);
    }
    @Override
    public void deleteTaskById(int id){
        taskMap.remove(id);
    }
    @Override
    public void deleteSubTaskById(int id){
        int epicId = subTaskMap.get(id).getEpicId();
        epicMap.get(subTaskMap.get(id).getEpicId()).getSubTaskIdsArrayList().remove(id);
        subTaskMap.remove(id);
        checkEpicsStatusBySubtask(epicId);
    }
    @Override
    public void deleteEpicById(int epicId){
        for(Integer key: subTaskMap.keySet()){
            if (subTaskMap.get(key).getEpicId() == epicId) {
                subTaskMap.put(key, null);
            }
        }
        epicMap.remove(epicId);
    }
    @Override
    public List<SubTask> getAllSubTasksByEpic(int idOfEpic){
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for(Integer key:subTaskMap.keySet()){
            if(subTaskMap.get(key).getEpicId()==idOfEpic){
                subTasks.add(subTaskMap.get(key));
            }
        }
        return subTasks;
    }
    private int generateId(){
        return ++generatedID;
    }
    private void checkEpicsStatusBySubtask(int epicId){
        for(Integer ID:epicMap.get(epicId).getSubTaskIdsArrayList()){
            if(subTaskMap.get(ID).getStatus().equals("IN_PROGRESS")){
                epicMap.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
        int doneCounter=0;
        int subTasksCounter=epicMap.get(epicId).getSubTaskIdsArrayList().size();
        for(Integer ID:epicMap.get(epicId).getSubTaskIdsArrayList()) {
            if(subTaskMap.get(ID).getStatus().equals("DONE")){
                doneCounter++;
            }
        }
        if(doneCounter==subTasksCounter){
            epicMap.get(epicId).setStatus(TaskStatus.DONE);
        }
    }

    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

}


