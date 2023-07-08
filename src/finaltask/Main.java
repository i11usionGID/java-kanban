package finaltask;
import finaltask.task.Task;
import finaltask.task.Epic;
import finaltask.task.SubTask;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("задача 1", "описание 1-ой задачи");
        Task task2 = new Task("задача 2", "описание 2-ой задачи");
        Epic epicTask1 = new Epic("глобальная задача 1", "описание глобальной задачи 1");


        task1 = taskManager.createTask(task1);
        task2 = taskManager.createTask(task2);
        epicTask1 = taskManager.createEpic(epicTask1);

        SubTask subTaskForEpicTask11 = new SubTask("подзадача 1 глобальной задачи 1",
                "описание подзад. 1", epicTask1.getID());
        SubTask subTaskForEpicTask12 = new SubTask("подзадача 2 глобальной задачи 1",
                "описание позад. 2", epicTask1.getID());

        subTaskForEpicTask11 = taskManager.createSubTask(subTaskForEpicTask11);
        subTaskForEpicTask12 = taskManager.createSubTask(subTaskForEpicTask12);

        Epic epicTask2 = new Epic("глобальная задача 2", "описание глобальной задачи 2");
        epicTask2 = taskManager.createEpic(epicTask2);
        SubTask subTaskForEpicTask2 = new SubTask("подзадача 1 глобальной задачи 2",
                "описание подзад. 1", epicTask2.getID());
        subTaskForEpicTask2 = taskManager.createSubTask(subTaskForEpicTask2);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());

        subTaskForEpicTask12.setStatus("IN_PROGRESS");
        taskManager.updateSubTask(subTaskForEpicTask12);

        System.out.println(epicTask1);

        subTaskForEpicTask11.setStatus("DONE");
        subTaskForEpicTask12.setStatus("DONE");
        taskManager.updateSubTask(subTaskForEpicTask11);
        taskManager.updateSubTask(subTaskForEpicTask12);

        System.out.println(epicTask1);

        taskManager.deleteTaskById(task2.getID());
        taskManager.deleteEpicById(epicTask1.getID());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubTasks());
    }
}
