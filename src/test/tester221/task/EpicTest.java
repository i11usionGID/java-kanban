package tester221.task;

import finaltask.TaskStatus.TaskStatus;
import finaltask.manager.InMemoryTaskManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class EpicTest {

    private static InMemoryTaskManager manager;
    Epic epic;
    SubTask subTask1;
    SubTask subTask2;

    @BeforeEach
    public void setup(){
        manager = new InMemoryTaskManager();
        epic = new Epic("name", "description");
        subTask1 = new SubTask("name1", "description1", 1);
        subTask2 = new SubTask("name2", "description2", 1);
        manager.createEpic(epic);
    }

    @Test
    public void shouldBeStatusNewWithNoSubTasks(){
        Assertions.assertEquals(manager.getEpicById(1).getStatus(), TaskStatus.NEW, "неверный статус");
    }

    @Test
    public void shouldBeStatusNewWithAllNewSubTasks(){
        manager.createSubTask(subTask1);
        manager.createSubTask(subTask2);
        Assertions.assertEquals(manager.getEpicById(1).getStatus(), TaskStatus.NEW, "неверный статус");
    }
    @Test
    public void shouldBeStatusDoneWithAllDoneSubTasks(){
        subTask1.setStatus(TaskStatus.DONE);
        subTask2.setStatus(TaskStatus.DONE);
        subTask1 = manager.createSubTask(subTask1);
        subTask2 = manager.createSubTask(subTask2);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(manager.getEpicById(1).getStatus(), TaskStatus.DONE, "неверный статус");
    }
    @Test
    public void shouldBeStatusIn_progressWithNewAndDoneSubTasks(){
        subTask1.setStatus(TaskStatus.NEW);
        subTask2.setStatus(TaskStatus.DONE);
        subTask1 = manager.createSubTask(subTask1);
        subTask2 = manager.createSubTask(subTask2);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(manager.getEpicById(1).getStatus(), TaskStatus.IN_PROGRESS, "неверный статус");
    }
    @Test
    public void shouldBeStatusIn_progressWithIn_progressSubTasks(){
        subTask1.setStatus(TaskStatus.IN_PROGRESS);
        subTask2.setStatus(TaskStatus.IN_PROGRESS);
        subTask1 = manager.createSubTask(subTask1);
        subTask2 = manager.createSubTask(subTask2);
        manager.updateSubTask(subTask1);
        manager.updateSubTask(subTask2);
        Assertions.assertEquals(manager.getEpicById(1).getStatus(), TaskStatus.IN_PROGRESS, "неверный статус");
    }

}