package finaltask;
import finaltask.TaskStatus.TaskStatus;
import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.manager.file.ManagerSaveException;
import finaltask.task.Task;
import finaltask.task.Epic;
import finaltask.task.SubTask;

import java.io.File;
import java.util.List;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File("C:/dev/java-kanban/src/resources/file.csv"));

        Task task1 = new Task("задача 1", "описание 1-ой задачи");
        Task task2 = new Task("задача 2", "описание 2-ой задачи");
        Epic epicTask1 = new Epic("глобальная задача 1", "описание глобальной задачи 1");


        task1 = fileBackedTasksManager.createTask(task1);
        task2 = fileBackedTasksManager.createTask(task2);
        epicTask1 = fileBackedTasksManager.createEpic(epicTask1);

        SubTask subTaskForEpicTask11 = new SubTask("подзадача 1 глобальной задачи 1",
                "описание подзад. 1", epicTask1.getID());
        SubTask subTaskForEpicTask12 = new SubTask("подзадача 2 глобальной задачи 1",
                "описание позад. 2", epicTask1.getID());

        subTaskForEpicTask11 = fileBackedTasksManager.createSubTask(subTaskForEpicTask11);
        subTaskForEpicTask12 = fileBackedTasksManager.createSubTask(subTaskForEpicTask12);

        Epic epicTask2 = new Epic("глобальная задача 2", "описание глобальной задачи 2");
        epicTask2 = fileBackedTasksManager.createEpic(epicTask2);
        SubTask subTaskForEpicTask2 = new SubTask("подзадача 1 глобальной задачи 2",
                "описание подзад. 1", epicTask2.getID());
        subTaskForEpicTask2 = fileBackedTasksManager.createSubTask(subTaskForEpicTask2);

        System.out.println(fileBackedTasksManager.getAllTasks());
        System.out.println(fileBackedTasksManager.getAllEpics());
        System.out.println(fileBackedTasksManager.getAllSubTasks());

        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getTaskById(1);
        fileBackedTasksManager.getAllSubTasks();
        fileBackedTasksManager.getAllEpics();

        System.out.println(fileBackedTasksManager.getHistory());
        fileBackedTasksManager.deleteAllEpics();
        fileBackedTasksManager.deleteAllTasks();
        fileBackedTasksManager.deleteAllSubTasks();
        System.out.println(fileBackedTasksManager.getHistory());

        FileBackedTasksManager newFile = fileBackedTasksManager.loadFromFile(new File( "C:/dev/java-kanban/src/resources/file.csv"));
        System.out.println(newFile.getAllTasks());
        System.out.println(newFile.getAllEpics());
        System.out.println(newFile.getAllSubTasks());
        System.out.println(newFile.getHistory());




    }
}
