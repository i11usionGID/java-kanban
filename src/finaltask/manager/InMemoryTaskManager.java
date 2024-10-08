package finaltask.manager;

import finaltask.TaskStatus.TaskStatus;
import finaltask.exception.TaskValidationExeption;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;
import finaltask.task.TaskType;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager  {
    protected int generatedID = 0;

    protected HashMap<Integer, Task> taskMap = new HashMap<>();
    protected HashMap <Integer, Epic> epicMap = new HashMap<>();
    protected static HashMap <Integer, SubTask> subTaskMap = new HashMap<>();
    protected  Map <LocalDateTime, Task> prioritizedTask = new TreeMap<>();
    protected final HistoryManager historyManager = Managers.getHistoryDefault();


    @Override
    public List<Task> getAllTasks(){
        return new ArrayList(taskMap.values());
    }
    @Override
    public List<SubTask> getAllSubTasks()  {
        return new ArrayList(subTaskMap.values());
    }
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList(epicMap.values());
    }
    @Override
    public void deleteAllTasks() {
        Map<Integer, Task>  temporaryMap = new HashMap<>(taskMap);
        for (Integer tasksId: temporaryMap.keySet()) {
            deleteTaskById(tasksId);
        }
    }
    @Override
    public void deleteAllSubTasks() {
        Map<Integer, SubTask>  temporaryMap = new HashMap<>(subTaskMap);
        for (Integer subTasksId:temporaryMap.keySet()) {
            deleteSubTaskById(subTasksId);
        }
        for(Integer key: epicMap.keySet()){
            epicMap.get(key).getSubTaskIdsArrayList().clear();
            epicMap.get(key).setStatus(TaskStatus.NEW);
            calculateEpicDuration(epicMap.get(key).getSubTaskIdsArrayList());
        }
    }
    @Override
    public void deleteAllEpics() {
        deleteAllSubTasks();
        Map<Integer, Epic>  temporaryMap = new HashMap<>(epicMap);
        for (Integer EpicsId: temporaryMap.keySet()) {
            deleteEpicById(EpicsId);
        }
    }
    @Override
    public Task getTaskById(int id){
        Task task = taskMap.get(id);
        historyManager.add(task);
        return task;
    }
    @Override
    public SubTask getSubTaskById(int id){
        SubTask task = subTaskMap.get(id);
        historyManager.add(task);
        return task;
    }
    @Override
    public Epic getEpicById(int epicId){
        Epic task = epicMap.get(epicId);
        historyManager.add(task);
        return task;
    }
    @Override
    public Task createTask(Task task){
        int id = generateId();
        task.setID(id);
        putTasksInMaps(task, id);
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
            if(!(subTask.getStartTime().equals(LocalDateTime.of(0,1,1,0,1)))) {
                if (epicMap.get(subTask.getEpicId()).getSubTaskIdsArrayList().isEmpty()) {
                    epicMap.get(subTask.getEpicId()).setStartTime(subTask.getStartTime());
                } else {
                    for (Integer ids : epicMap.get(subTask.getEpicId()).getSubTaskIdsArrayList()) {
                        if (subTask.getStartTime().isBefore(subTaskMap.get(ids).getStartTime())) {
                            epicMap.get(subTask.getEpicId()).setStartTime(subTask.getStartTime());
                        }
                    }
                }
            }
            putTasksInMaps(subTask, subTaskId);
            epicMap.get(subTask.getEpicId()).setSubTaskIdInArrayList(subTaskId);
            if(!(subTask.getStartTime().equals(LocalDateTime.of(0,1,1,0,1)))) {
                calculateEpicDuration(epicMap.get(subTask.getEpicId()).getSubTaskIdsArrayList());
            }
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
        putTasksInMaps(task, task.getID());
    }
    @Override
    public void updateSubTask(SubTask subTask){
        SubTask saveId = subTaskMap.get(subTask.getID());
        if(saveId==null){
            return;
        }
        putTasksInMaps(subTask, subTask.getID());
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

    public HashMap<Integer, SubTask> getSubTaskMap() {
        return subTaskMap;
    }

    @Override
    public void deleteTaskById(int id){
        try {
            for(LocalDateTime dateTime: prioritizedTask.keySet()){
                if(prioritizedTask.get(dateTime).equals(taskMap.get(id))){
                    prioritizedTask.remove(dateTime);
                    break;
                }
            }
            historyManager.remove(id);
            taskMap.remove(id);
        } catch (NullPointerException exception){
            throw new NullPointerException("Неверный id задачи");
        }
    }
    @Override
    public void deleteSubTaskById(int id){
        try {
            prioritizedTask.remove(subTaskMap.get(id).getStartTime());
            int epicId = subTaskMap.get(id).getEpicId();
            epicMap.get(epicId).getSubTaskIdsArrayList().remove((Integer) id);
            if(!(subTaskMap.get(id).getStartTime().equals(LocalDateTime.of(0,1,1,0,1)))) {
                calculateEpicDuration(epicMap.get(epicId).getSubTaskIdsArrayList());
            }
            historyManager.remove(id);

            subTaskMap.remove(id);
            checkEpicsStatusBySubtask(epicId);
        } catch (NullPointerException exception){
            throw new NullPointerException("Неверный id задачи");
        }

    }
    @Override
    public void deleteEpicById(int epicId){
        try {
            for(Integer key: subTaskMap.keySet()){
                if (subTaskMap.get(key).getEpicId() == epicId) {
                    historyManager.remove(subTaskMap.get(key).getID());
                    subTaskMap.put(key, null);
                }
            }
            historyManager.remove(epicId);
            epicMap.remove(epicId);
        } catch (NullPointerException exception){
            throw new NullPointerException("Неверный id задачи");
        }
    }
    @Override
    public List<SubTask> getAllSubTasksByEpic(int idOfEpic){
        ArrayList<SubTask> subTasks = new ArrayList<>();
        for(Integer key:subTaskMap.keySet()){
            if(subTaskMap.get(key).getEpicId()==idOfEpic){
                subTasks.add(subTaskMap.get(key));
                historyManager.add(subTaskMap.get(key));
            }
        }
        return subTasks;
    }
    protected int generateId(){
        return ++generatedID;
    }
    private void checkEpicsStatusBySubtask(int epicId){
        for(Integer ID:epicMap.get(epicId).getSubTaskIdsArrayList()){
            if(subTaskMap.get(ID).getStatus().equals(TaskStatus.IN_PROGRESS) ||
                    subTaskMap.get(ID).getStatus().equals(TaskStatus.DONE)){
                epicMap.get(epicId).setStatus(TaskStatus.IN_PROGRESS);
            }
        }
        int doneCounter=0;
        int subTasksCounter=epicMap.get(epicId).getSubTaskIdsArrayList().size();
        for(Integer ID:epicMap.get(epicId).getSubTaskIdsArrayList()) {
            if(subTaskMap.get(ID).getStatus().equals(TaskStatus.DONE)){
                doneCounter++;
            }
        }
        if(doneCounter==subTasksCounter&doneCounter!=0){
            calculateEpicDuration(epicMap.get(epicId).getSubTaskIdsArrayList());
            epicMap.get(epicId).setStatus(TaskStatus.DONE);
        }
    }
    private void calculateEpicDuration(ArrayList<Integer> subTasksId){
        if (subTasksId.isEmpty()){
            return;
        }
        for(Integer ids:subTasksId){
            if(subTaskMap.get(ids).getStartTime().equals(LocalDateTime.of(0,1,1,0,1))){
                return;
            }
        }
        int epicDuration = 0;
        for (Integer ids: subTasksId) {
            epicDuration = epicDuration + subTaskMap.get(ids).getDuration();
        }
        epicMap.get(subTaskMap.get(subTasksId.get(0)).getEpicId()).setDuration(epicDuration);

    }
    public List<Task> getPrioritizedTask(){
        return new ArrayList<>((Collection) prioritizedTask);
    }
    public void validate(Task task){
        if(task.getStartTime().equals(LocalDateTime.of(0,1,1,0,1))){
            task.setStartTime(LocalDateTime.now().withNano(0));
        }
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        Integer result = prioritizedTask.values().stream()
                .map(it->{
                    if(startTime.isAfter(it.getStartTime())&&endTime.isBefore(it.getEndTime())){
                        return 1;
                    }
                    if(endTime.isAfter(it.getStartTime())&&endTime.isBefore(it.getEndTime())){
                        return 1;
                    }
                    if(startTime.isAfter(it.getStartTime())&&startTime.isBefore(it.getEndTime())){
                        return 1;
                    }
                    return 0;
                })
                .reduce(Integer::sum)
                .orElse(0);
        if(result>0){
            throw new TaskValidationExeption("Время выполнения пересекается с другой задачей!");
        }
    }
    public boolean containTaskId(int id){
        if(taskMap.containsKey(id)){
            return true;
        }
        return false;
    }
    public boolean containSubTaskId(int id){
        if(subTaskMap.containsKey(id)){
            return true;
        }
        return false;
    }
    public boolean containEpicId(int id){
        if(epicMap.containsKey(id)){
            return true;
        }
        return false;
    }
    public void putTasksInMaps(Task task, int id){
        validate(task);
        if(task.getType()== TaskType.TASK) {
            taskMap.put(id, task);
        } else {
            subTaskMap.put(id,(SubTask) task);
        }
        prioritizedTask.put(task.getStartTime(), task);
    }

    public List<Task> getHistory(){
        return historyManager.getHistory();
    }

}



