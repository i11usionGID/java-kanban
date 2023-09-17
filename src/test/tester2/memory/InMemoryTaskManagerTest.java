package tester2.memory;

import tester2.TaskManagerTest;
import finaltask.TaskStatus.TaskStatus;
import finaltask.manager.InMemoryTaskManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        init();
    }

    @Test
    public void sholdBeEndTimeEpic(){

    }
    @Test
    public void createTask(){
        Task task = new Task("name", "description");
        taskManager.createTask(task);
        assertEquals(task, taskManager.getTaskById(9), "Задача не создалась");
    }
    @Test
    public void createSubTask(){
        SubTask subTask = new SubTask("name", "description", 3);
        taskManager.createSubTask(subTask);
        assertEquals(subTask, taskManager.getSubTaskById(9), "Задача не создалась");
    }
    @Test
    public void createEpic(){
        Epic epic = new Epic("name", "description");
        taskManager.createEpic(epic);
        assertEquals(epic, taskManager.getEpicById(9), "Задача не создалась");
    }
    @Test
    public void deleteTaskById(){
        taskManager.deleteTaskById(1);
        Assertions.assertEquals(taskManager.getAllTasks().size(), 1, "Задача не удалилась");
        NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                () -> taskManager.deleteTaskById(10));

        Assertions.assertEquals(null, exception.getMessage(), "Неправильная работа при удалении несуществующей задачи");
    }

    @Test
    public void deleteSubTaskById(){
        taskManager.deleteSubTaskById(5);
        Assertions.assertEquals(taskManager.getAllSubTasks().size(), 3, "Задача не удалилась");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.deleteSubTaskById(10));
        Assertions.assertEquals(null, exception.getMessage(), "Неправильная работа при удалении несуществующей задачи");
    }
    @Test
    public void deleteEpicById(){
        taskManager.deleteEpicById(3);
        Assertions.assertEquals(taskManager.getAllEpics().size(), 1, "Задача не удалилась");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.deleteEpicById(10));
        Assertions.assertEquals(null, exception.getMessage(), "Неправильная работа при удалении несуществующей задачи");
    }
    @Test
    public void updateTask(){
        task1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task1);
        Assertions.assertEquals(task1.getStatus(), TaskStatus.IN_PROGRESS, "задача не обновилась");
    }
    @Test
    public void updateSubTask(){
        subTask11.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateSubTask(subTask11);
        Assertions.assertEquals(subTask11.getStatus(), TaskStatus.IN_PROGRESS, "задача не обновилась");
    }
    @Test
    public void updateEpic(){
        epic1.setStatus(TaskStatus.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        Assertions.assertEquals(epic1.getStatus(), TaskStatus.IN_PROGRESS, "задача не обновилась");
    }
    @Test
    public void deleteAllTasks(){
        taskManager.deleteAllTasks();
        Assertions.assertEquals(taskManager.getAllTasks().size(), 0, "Задачи не удалены");
    }
    @Test
    public void deleteAllSubTasks(){
        taskManager.deleteAllSubTasks();
        Assertions.assertEquals(taskManager.getAllSubTasks().size(), 0, "Задачи не удалены");
    }
    @Test
    public void deleteAllEpics(){
        taskManager.deleteAllEpics();
        Assertions.assertEquals(taskManager.getAllEpics().size(), 0, "Задачи не удалены");
    }


}