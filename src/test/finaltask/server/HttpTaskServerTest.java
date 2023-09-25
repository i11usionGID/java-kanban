package finaltask.server;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.sun.net.httpserver.HttpServer;
import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.task.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {
    private HttpTaskServer taskServer;
    private final Gson gson = Managers.getGson();
    private TaskManager manager;
    @BeforeEach
    public void init() throws IOException {
        manager = Managers.getFileDefault();
        taskServer = new HttpTaskServer((FileBackedTasksManager) manager);
        Task task = new Task("name", "description", LocalDateTime.now().withNano(0), 10);
        manager.createTask(task);
        taskServer.start();
    }

    @Test
    public void getTaskById1() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create("http://localhost:8080/tasks/task/?id=1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        Type taskType = new TypeToken<ArrayList<Task>>() {}.getType();
        List<Task> actual = gson.fromJson(response.body(), taskType);
        assertNotNull(actual, "задачи не возвращаются");
        assertEquals(1, actual.size(), "Неверное кол-во");
        assertEquals(manager.getTaskById(1), actual.get(0), "объекты не идентичны");

    }


}