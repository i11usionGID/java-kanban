package tester22.file;

import finaltask.manager.file.FileBackedTasksManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tester22.TaskManagerTest;

import java.io.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    final File file = new File("src\\resources\\fileTest.csv");
    @BeforeEach
    public void setUp(){
        taskManager = new FileBackedTasksManager(file);
        init();
    }

    @Test
    void EmptyFile() throws IOException {
        taskManager.getAllSubTasks();
        taskManager.deleteAllSubTasks();
        taskManager.getAllTasks();
        taskManager.deleteAllTasks();
        taskManager.getAllEpics();
        taskManager.deleteAllEpics();
        taskManager.save();
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> strings = new ArrayList<>();
        while (br.ready()){
            String line = br.readLine();
            strings.add(line);
        }
        br.close();
        if(strings.size()==2) {
            Assertions.assertEquals("", strings.get(1), "Неправильное чтение из файла пустого списка задач");
        } else {
            Assertions.assertEquals("", strings.get(2), "Неправильное чтение из файла пустого списка задач");
        }
    }
    @Test
    void EpicWithoutSubTasks() throws IOException {
        taskManager.deleteAllSubTasks();
        taskManager.save();
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> strings = new ArrayList<>();
        while(br.ready()){
            String line = br.readLine();
            strings.add(line);
        }
        br.close();
        Assertions.assertEquals(strings.get(3), "3,EPIC,name,NEW,description,0000-01-01T00:01,PT0S,",
                "Неправильное сохранение в файл");
        Assertions.assertEquals(strings.get(4), "4,EPIC,name,NEW,description,0000-01-01T00:01,PT0S,",
                "Неправильное сохранение в файл");
        Assertions.assertEquals(strings.get(5), "", "Неправильное сохранение в файл");
        Assertions.assertEquals(strings.get(6), "1,2,3,4", "Неправильное сохранение в файл");
    }
    @Test
    void save() throws IOException {
        taskManager.save();
        BufferedReader br = new BufferedReader(new FileReader(file));
        List<String> strings = new ArrayList<>();
        while(br.ready()){
            String line = br.readLine();
            strings.add(line);
        }
        br.close();
        Assertions.assertEquals(strings.get(1), "1,TASK,name,NEW,description,"+ LocalDateTime.now().withNano(0) +",PT0S,",
                "Неправильное сохранение в файл");
        Assertions.assertEquals(strings.get(4), "4,EPIC,name,NEW,description,0000-01-01T00:01,PT0S,",
                "Неправильное сохранение в файл");
        Assertions.assertEquals(strings.get(7), "7,SUBTASK,name,NEW,description,"+ LocalDateTime.now().withNano(0) +",PT0S,4",
                "Неправильное сохранение в файл");
        Assertions.assertEquals(strings.get(10), "1,2,3,4,5,6,7,8", "Неправильное сохранение в файл");
    }

    @Test
    void loadFromFile() {
        taskManager.getAllTasks();
        taskManager.getAllSubTasks();
        taskManager.getAllEpics();
        taskManager.save();
        FileBackedTasksManager newTaskManager = taskManager.loadFromFile(new File(file.toURI()));
        Assertions.assertEquals(newTaskManager.getAllEpics().size(), 2, "неправильное чтение файла");
        Assertions.assertEquals(newTaskManager.getAllSubTasks().size(), 4, "неправильное чтение файла");
        Assertions.assertEquals(newTaskManager.getAllTasks().size(), 2, "неправильное чтение файла");
        Assertions.assertEquals(newTaskManager.getHistory().size(), 8, "неправильное чтение файла");
    }

}