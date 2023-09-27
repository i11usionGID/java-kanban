package finaltask.manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.adapters.LocalDateTimeAdapter;

import java.io.File;
import java.time.LocalDateTime;

public final class Managers {

    private Managers(){}

    private static File file = new File("C:\\dev\\java-kanban\\src\\resources\\file.csv");
    public static FileBackedTasksManager getFileDefault(){
        return new FileBackedTasksManager(file);
    }
    public static HttpTaskManager getDefault(){
         return new HttpTaskManager(8080);
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }
    public static Gson getGson(){
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        return gsonBuilder.create();
    }

}
