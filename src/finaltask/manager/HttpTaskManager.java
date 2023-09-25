package finaltask.manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import finaltask.client.KVTaskClient;
import finaltask.exception.ManagerSaveException;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.task.Task;

import java.io.File;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class HttpTaskManager extends FileBackedTasksManager {

    private KVTaskClient client;
    private Gson gson;
    public static final String TASKS_KEY = "tasks";
    public static final String HISTORY_KEY = "tasks";
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
    public void load(){
        String historyJson = client.load(HISTORY_KEY);
        List<Integer> historyIds = gson.fromJson(historyJson, historyType);
    }
}
