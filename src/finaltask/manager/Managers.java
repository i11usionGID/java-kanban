package finaltask.manager;

import finaltask.manager.file.FileBackedTasksManager;

import java.io.File;
import java.nio.file.Paths;

public final class Managers {

    private Managers(){}

    public static TaskManager getDefault(String file){
        File taksFile = new File((Paths.get(file).toUri()));
        return new FileBackedTasksManager(taksFile);
    }
    public static TaskManager getDefault(){
         return new InMemoryTaskManager();
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }

}
