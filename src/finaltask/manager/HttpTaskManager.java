package finaltask.manager;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;
import finaltask.client.KVTaskClient;
import finaltask.exception.ManagerSaveException;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private static KVTaskClient client;
    private static Gson gson;
    public static final String TASKS_KEY = "tasks";
    public static final String HISTORY_KEY = "tasks";
    public static final String EPICS_KEY = "epics";
    public static final String SUBTASKS_KEY = "subtasks";
    private Type historyType = new TypeToken<ArrayList<HistoryManager>>() {}.getType();
    public HttpTaskManager(int port) {
        super(null);
        this.client = new KVTaskClient(port);
        gson = Managers.getGson();
    }

    @Override
    public void save() throws ManagerSaveException {
        String jsonTask = gson.toJson(getAllTasks());
        client.save(TASKS_KEY, jsonTask);
        List<Integer> historyIds = historyManager.getHistory().stream()
                .map(Task::getID)
                .collect(Collectors.toList());
        String historyJson = gson.toJson(historyIds);
        client.save(HISTORY_KEY, historyJson);
    }

    public static HttpTaskManager loadFromServer(int port){
        HttpTaskManager httpTaskManager = new HttpTaskManager(port);
        List<Integer> ids = new ArrayList<>();
        String loadTasks = client.load(TASKS_KEY);
        JsonElement jsonTasks = JsonParser.parseString(loadTasks);
        if(!jsonTasks.isJsonNull()){
            JsonArray jsonTaskArray = jsonTasks.getAsJsonArray();
            for(JsonElement jsonElement: jsonTaskArray){
                Task task = gson.fromJson(jsonElement, Task.class);
                if(task!=null){
                    ids.add(task.getID());
                    httpTaskManager.updateTask(task);
                }
            }
        }
        String loadEpics = client.load(EPICS_KEY);
        JsonElement jsonEpics = JsonParser.parseString(loadEpics);
        if(!jsonEpics.isJsonNull()){
            JsonArray jsonEpicArray = jsonEpics.getAsJsonArray();
            for(JsonElement jsonElement: jsonEpicArray){
                Epic epic = gson.fromJson(jsonElement, Epic.class);
                if(epic!=null){
                    ids.add(epic.getID());
                    httpTaskManager.updateEpic(epic);
                }
            }
        }
        String loadSubTasks = client.load(SUBTASKS_KEY);
        JsonElement jsonSubtasks = JsonParser.parseString(loadSubTasks);
        if(!jsonSubtasks.isJsonNull()){
            JsonArray jsonSubtasksArray = jsonSubtasks.getAsJsonArray();
            for(JsonElement jsonElement: jsonSubtasksArray){
                SubTask subTask = gson.fromJson(jsonElement, SubTask.class);
                if(subTask!=null){
                    ids.add(subTask.getID());
                    httpTaskManager.updateSubTask(subTask);
                }
            }
        }
        JsonElement jsonHistory = JsonParser.parseString(client.load(HISTORY_KEY));
        if(!jsonHistory.isJsonNull()){
            JsonArray jsonArray = jsonHistory.getAsJsonArray();
            for(JsonElement jsonId: jsonArray){
                int id = jsonId.getAsInt();
                if(httpTaskManager.taskMap.containsKey(id)){
                    httpTaskManager.historyManager.add((httpTaskManager.getTaskById(id)));
                } else if(httpTaskManager.epicMap.containsKey(id)){
                    httpTaskManager.historyManager.add((httpTaskManager.getEpicById(id)));
                } else if(HttpTaskManager.subTaskMap.containsKey(id)){
                    httpTaskManager.historyManager.add((httpTaskManager.getSubTaskById(id)));
                } else {
                    System.out.println("Неверный id");
                }
            }
        }
        return httpTaskManager;
    }

}
