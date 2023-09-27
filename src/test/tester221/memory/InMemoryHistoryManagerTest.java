package tester221.memory;

import finaltask.manager.InMemoryHistoryManager;
import finaltask.task.Task;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

class InMemoryHistoryManagerTest {
    Task task;
    Task task2;
    Task task3;
    Task task4;
    Task task5;
    InMemoryHistoryManager manager;

    @BeforeEach
    public void setUp(){
        manager = new InMemoryHistoryManager();
        task = new Task("name", "description");
        task.setID(1);
        task2 = new Task("name", "description");
        task2.setID(2);
        task3 = new Task("name", "description");
        task3.setID(3);
        task4 = new Task("name", "description");
        task4.setID(4);
        task5 = new Task("name", "description");
        task5.setID(5);
    }

    @Test
    public void shoulBeEmptyList(){
        List<Task> list = manager.getHistory();
        Assertions.assertNotNull(list, "null");
        Assertions.assertTrue(list.isEmpty(), "список не пустой");
    }
    @Test
    public void shouldBe1Task(){
        manager.add(task);
        manager.add(task);
        List<Task> list = manager.getHistory();
        Assertions.assertNotNull(list, "null");
        Assertions.assertEquals(list.size(), 1, "в списке 2 задачи");
    }
    @Test
    public void shouldBe2Task(){
        manager.add(task);
        manager.add(task2);
        manager.add(task3);
        manager.add(task4);
        manager.add(task5);
        manager.remove(1);
        manager.remove(3);
        manager.remove(5);
        List<Task> list = manager.getHistory();
        Assertions.assertNotNull(list, "null");
        Assertions.assertEquals(list.size(), 2, "в списке неверное кол-во задач");

    }

}