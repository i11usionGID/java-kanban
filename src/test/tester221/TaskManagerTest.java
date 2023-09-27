package tester221;

import finaltask.manager.TaskManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;


public class TaskManagerTest <T extends TaskManager>{

    protected T taskManager;
    protected Task task1;
    protected Task task2;
    protected Epic epic1;
    protected Epic epic2;
    protected SubTask subTask11;
    protected SubTask subTask12;
    protected SubTask subTask21;
    protected SubTask subTask22;


    protected void init(){
        task1 = new Task("name", "description");
        taskManager.createTask(task1);
        task2 = new Task("name", "description");
        taskManager.createTask(task2);
        epic1 = new Epic("name", "description");
        taskManager.createEpic(epic1);
        epic2 = new Epic("name", "description");
        taskManager.createEpic(epic2);
        subTask11 = new SubTask("name", "description", 3);
        taskManager.createSubTask(subTask11);
        subTask12 = new SubTask("name", "description", 3);
        taskManager.createSubTask(subTask12);
        subTask21 = new SubTask("name", "description", 4);
        taskManager.createSubTask(subTask21);
        subTask22 = new SubTask("name", "description", 4);
        taskManager.createSubTask(subTask22);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getEpicById(3);
        taskManager.getEpicById(4);
        taskManager.getSubTaskById(5);
        taskManager.getSubTaskById(6);
        taskManager.getSubTaskById(7);
        taskManager.getSubTaskById(8);



    }


    @Test
    public void getAllTasks(){
        List<Task> tasks = new ArrayList<>();
        tasks = taskManager.getAllTasks();
        Assertions.assertNotNull(tasks, "Задач нет");
        Assertions.assertEquals(tasks.size(), 2, "Неверное кол-во задач");
    }
    @Test
    public void getAllSubTasks(){
        List<SubTask> subTasks = taskManager.getAllSubTasks();
        Assertions.assertNotNull(subTasks, "Задач нет");
        Assertions.assertEquals(subTasks.size(), 4, "Неверное кол-во задач");
    }
    @Test
    public void getAllEpics(){
        List<Epic> epics = taskManager.getAllEpics();
        Assertions.assertNotNull(epics, "Задач нет");
        Assertions.assertEquals(epics.size(), 2, "Неверное кол-во задач");

    }
    @Test
    public void getTaskById(){
        Task task = taskManager.getTaskById(1);
        Assertions.assertEquals(task, task1, "Задачи разные");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.getTaskById(10));
        Assertions.assertEquals(null, exception.getMessage(),
                "Неправильная работа при удалении несуществующей задачи");


    }
    @Test
    public void getSubTaskById(){
        SubTask subTask = taskManager.getSubTaskById(5);
        Assertions.assertEquals(subTask, subTask11, "Задачи разные");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.getSubTaskById(10));
        Assertions.assertEquals(null, exception.getMessage(),
                "Неправильная работа при удалении несуществующей задачи");
    }
    @Test
    public void getEpicById(){
        Epic epic = taskManager.getEpicById(3);
        Assertions.assertEquals(epic, epic1, "Задачи разные");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.getEpicById(10));
        Assertions.assertEquals(null, exception.getMessage(),
                "Неправильная работа при удалении несуществующей задачи");
    }

    @Test
    public void getAllSubTasksByEpic(){
        List<SubTask> subTasks = taskManager.getAllSubTasksByEpic(3);
        Assertions.assertEquals(subTasks.size(), 2, "Неправильное кол-во задач");
        List<SubTask> subTasks2 = taskManager.getAllSubTasksByEpic(10);
        Assertions.assertEquals(subTasks2.size(), 0, "неправильная работа с несуществующим индификатором");
    }
}