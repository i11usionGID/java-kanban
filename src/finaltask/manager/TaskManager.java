package finaltask.manager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private int generatedID = 0;

    HashMap <Integer, Task> taskMap = new HashMap<>();
    HashMap <Integer, Epic> epicMap = new HashMap<>();
    HashMap <Integer, SubTask> subTaskMap = new HashMap<>();

    public ArrayList<Task> getAllTasks(){
        return new ArrayList(taskMap.values());
    }
    public ArrayList<SubTask> getAllSubTasks(){
        return new ArrayList(subTaskMap.values());
    }
    public ArrayList<Epic> getAllEpics(){
        return new ArrayList(epicMap.values());
    }
    public void deleteAllTasks(){
        taskMap.clear();
    }
    public void deleteAllSubTasks(){
        subTaskMap.clear();
        for(Integer key: epicMap.keySet()){
            epicMap.get(key).getSubTaskIdsArrayList().clear();
            epicMap.get(key).setStatus("NEW");
        }
    }
    public void deleteAllEpics(){
        subTaskMap.clear();
        epicMap.clear();
    }
    public Task getTaskById(int id){
        return taskMap.get(id);
    }
    public SubTask getSubTaskById(int id){
        return subTaskMap.get(id);
    }
    public Epic getEpicById(int epicId){
        return epicMap.get(epicId);
    }
    public Task createTask(Task task){
        int id = generateId();
        task.setID(id);
        taskMap.put(id, task);
        return task;
    }
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
    public Epic createEpic(Epic epic){
        int id = generateId();
        epic.setID(id);
        epicMap.put(id, epic);
        return epic;
    }
    public void updateTask(Task task){
        Task taskId = taskMap.get(task.getID());
        if(taskId == null){
            return;
        }
        taskMap.put(task.getID(), task);
    }
    public void updateSubTask(SubTask subTask){
        SubTask saveId = subTaskMap.get(subTask.getID());
        if(saveId==null){
            return;
        }
        subTaskMap.put(subTask.getID(), subTask);
        checkEpicsStatusBySubtask(subTask);
    }
    public void updateEpic(Epic epic){
        Epic epicId = epicMap.get(epic.getID());
        if(epicId==null){
            return;
        }
        epicMap.put(epic.getID(), epic);
    }
    public void deleteTaskById(int id){
        taskMap.remove(id);
    }
    public void deleteSubTaskById(int id){
        epicMap.get(subTaskMap.get(id).getEpicId()).getSubTaskIdsArrayList().remove(id);
        subTaskMap.remove(id);
        checkEpicsStatusBySubtask(subTaskMap.get(id));
    }
    public void deleteEpicById(int epicId){
        for(Integer key: subTaskMap.keySet()){
            if (subTaskMap.get(key).getEpicId() == epicId) {
                subTaskMap.put(key, null);
            }
        }
        epicMap.remove(epicId);
    }
    public ArrayList<SubTask> getAllSubTasksByEpic(int idOfEpic){
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
    private void checkEpicsStatusBySubtask(SubTask subTask){
        for(Integer keyOfEpics:epicMap.keySet()) {
            for (Integer keyOfSubTasks : subTaskMap.keySet()) {
                if(subTaskMap.get(keyOfSubTasks).getEpicId()==epicMap.get(keyOfEpics).getID()){
                    if(subTaskMap.get(keyOfSubTasks).getStatus().equals("IN_PROGRESS")){
                        epicMap.get(subTaskMap.get(keyOfSubTasks).getEpicId()).setStatus("IN_PROGRESS");
                    }
                }
            }
        }
        if(subTask.getStatus().equals("DONE")){
            int doneCounter=0;
            int subTasksCounter=epicMap.get(subTask.getEpicId()).getSubTaskIdsArrayList().size();
            for(Integer keyOfEpics:epicMap.keySet()){
                for(Integer keyOfSubTasks:subTaskMap.keySet()){
                    if(subTaskMap.get(keyOfSubTasks).getEpicId()==epicMap.get(keyOfEpics).getID()){
                        if(subTaskMap.get(keyOfSubTasks).getStatus().equals("DONE")){
                            doneCounter++;
                        }
                    }
                }
            }
            if(doneCounter==subTasksCounter){
                epicMap.get(subTask.getEpicId()).setStatus("DONE");
            }
        }
    }
}
