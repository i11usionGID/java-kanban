package finaltask.manager;

import finaltask.manager.file.FileBackedTasksManager;

import java.io.File;

public final class Managers {

    private Managers(){}

    public static TaskManager getDefault(){
       // return new InMemoryTaskManager();
        File taksFile = new File("C:/dev/java-kanban/src/resources/file.csv");
        return new FileBackedTasksManager(taksFile);
    }
    public static HistoryManager getHistoryDefault(){
        return new InMemoryHistoryManager();
    }

}
