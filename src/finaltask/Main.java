package finaltask;
import finaltask.manager.InMemoryTaskManager;
import finaltask.manager.Managers;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.exception.ManagerSaveException;
import finaltask.server.HttpTaskServer;
import finaltask.task.Task;
import finaltask.task.Epic;
import finaltask.task.SubTask;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws ManagerSaveException, IOException {
       /* System.out.println("Введите путь к файлу");
        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(new File(Paths.get(file).toUri()));

        */

        FileBackedTasksManager manager1 = Managers.getFileDefault();


        Task task1 = new Task("задача 1", "описание 1-ой задачи");
        Task task2 = new Task("задача 2", "описание 2-ой задачи");
        manager1.createTask(task1);
        manager1.createTask(task2);
        manager1.getTaskById(1);
        manager1.getTaskById(2);
        Task task3 = new Task("задача 1", "описание 1-ой задачи");
        Task task4 = new Task("задача 2", "описание 2-ой задачи");
        manager1.createTask(task3);
        manager1.createTask(task4);
        Epic epic = new Epic("name", "description");
        manager1.createEpic(epic);
        SubTask subTask1 = new SubTask("задача 1", "описание 1-ой задачи", 5);
        SubTask subTask2 = new SubTask("задача 1", "описание 1-ой задачи", 5, LocalDateTime.of(2018, 9, 18, 10, 18), 250);
        manager1.createSubTask(subTask1);
        manager1.createSubTask(subTask2);
        manager1.save();

        HttpTaskServer httpTaskServer = new HttpTaskServer(manager1);
        httpTaskServer.start();

    }
}
