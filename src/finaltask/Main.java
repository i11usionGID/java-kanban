package finaltask;
import finaltask.manager.InMemoryTaskManager;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.exception.ManagerSaveException;
import finaltask.task.Task;
import finaltask.task.Epic;
import finaltask.task.SubTask;

import java.io.File;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {
       /* System.out.println("Введите путь к файлу");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(Paths.get(file).toUri()));

        */
        InMemoryTaskManager manager1 = new InMemoryTaskManager();
        LocalDateTime DateTime = LocalDateTime.of(2023, 9, 18, 10, 15);
        Duration duration = Duration.ZERO.plusMinutes(250);

        Task task1 = new Task("задача 1", "описание 1-ой задачи", DateTime, duration);
        Task task2 = new Task("задача 2", "описание 2-ой задачи", LocalDateTime.of(2023, 9, 18, 10, 18), Duration.ZERO.plusMinutes(250));
        manager1.createTask(task1);
        manager1.createTask(task2);
/*
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

        FileBackedTasksManager newFile = fileBackedTasksManager.loadFromFile(new File((Paths.get(file).toUri())));
        System.out.println(newFile.getAllTasks());
        System.out.println(newFile.getAllEpics());
        System.out.println(newFile.getAllSubTasks());
        System.out.println(newFile.getHistory());



         */


    }
}
