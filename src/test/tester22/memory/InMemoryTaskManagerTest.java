package tester2.memory;

import finaltask.exception.TaskValidationExeption;
import tester2.TaskManagerTest;
import finaltask.TaskStatus.TaskStatus;
import finaltask.manager.InMemoryTaskManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    public void setUp() {
        taskManager = new InMemoryTaskManager();
        init();
    }
//Добрый день, уважаемый ревьюер! Прошу сразу меня извинить, если в коде не хватает тестов или я выбрал неправильные структуры данных для работы кода
//Это был пока что самый тяжелый спринт и, возможно, я много где ошибся
//В ТЗ было не очень много уточнений, поэтому сделал логику программы по большей части на свое усмотрение
//Прошу указать на все мои ошибки и подсказать более хорошие структуры данных, я обязательно все исправлю! ^^
    
    @Test
    public void sholdBeEndTimeEpic2023_12_20T10_20(){
        Epic epicTask1 = new Epic("глобальная задача 1", "описание глобальной задачи 1");
        taskManager.createEpic(epicTask1);
        SubTask subTaskForEpicTask11 = new SubTask("подзадача 1 глобальной задачи 1",
                "описание подзад. 1", epicTask1.getID(),
                LocalDateTime.of(2021,12,20,10,0), Duration.ZERO.plusMinutes(240));
        SubTask subTaskForEpicTask12 = new SubTask("подзадача 2 глобальной задачи 1",
                "описание позад. 2", epicTask1.getID(),
                LocalDateTime.of(2023,12,20,10,0), Duration.ZERO.plusMinutes(20));

        taskManager.createSubTask(subTaskForEpicTask11);
        taskManager.createSubTask(subTaskForEpicTask12);
        Assertions.assertEquals(LocalDateTime.of(2023, 12,20,10,20),
                taskManager.getEpicById(epicTask1.getID()).getEndTime(taskManager.getSubTaskMap()),
                "неверный рачет времени для эпика");
    }
    @Test
    public void shouldBeEndTimeTask2021_12_20T14_00(){
        Task task = new Task("name", "description",
                LocalDateTime.of(2021,12,20,10,0), Duration.ZERO.plusMinutes(240));
        taskManager.createTask(task);
        Assertions.assertEquals(LocalDateTime.of(2021, 12,20,14,0),
                task.getEndTime(), "неверный рассчет времени");
    }
    @Test
    public void shouldBeTaskValidatorExeption(){
        Task task27 = new Task("задача 1", "описание 1-ой задачи",
                LocalDateTime.of(2023, 9, 18, 10, 15), Duration.ZERO.plusMinutes(250));
        Task task28 = new Task("задача 2", "описание 2-ой задачи",
                LocalDateTime.of(2023, 9, 18, 10, 18), Duration.ZERO.plusMinutes(250));
        TaskValidationExeption exeption = Assertions.assertThrows(
                TaskValidationExeption.class,
                ()->{
                    taskManager.createTask(task27);
                    taskManager.createTask(task28);
                });
        Assertions.assertEquals("Время выполнения пересекается с другой задачей!",
                exeption.getMessage(), "неправильная работа с пересекающимися датами выполнения задачи");
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
        taskManager.deleteTaskById(10);

        Assertions.assertEquals(taskManager.getAllTasks().size(), 1, "Задача не удалилась");
    }

    @Test
    public void deleteSubTaskById(){
        taskManager.deleteSubTaskById(5);
        Assertions.assertEquals(taskManager.getAllSubTasks().size(), 3, "Задача не удалилась");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.deleteSubTaskById(10));
        Assertions.assertEquals(null, exception.getMessage(),
                "Неправильная работа при удалении несуществующей задачи");
    }
    @Test
    public void deleteEpicById(){
        taskManager.deleteEpicById(3);
        Assertions.assertEquals(taskManager.getAllEpics().size(), 1, "Задача не удалилась");
        final NullPointerException exception = Assertions.assertThrows(
                NullPointerException.class,
                ()-> taskManager.deleteEpicById(10));
        Assertions.assertEquals(null, exception.getMessage(),
                "Неправильная работа при удалении несуществующей задачи");
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