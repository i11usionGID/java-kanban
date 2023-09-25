package finaltask.server;


import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import finaltask.manager.Managers;
import finaltask.manager.TaskManager;
import finaltask.manager.file.FileBackedTasksManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;

import java.io.IOException;
import java.net.InetSocketAddress;

import java.util.List;
import java.util.regex.Pattern;

import static java.nio.charset.StandardCharsets.UTF_8;

//Добрый день, уважаемый ревьюер! К сожалению, меня очень сильно поджимает время и я писал программу за 3 дня.
//У меня уже "замылился" глаз и я не могу найти ошибки. Делал проект до ночи. Пробовал написать тесты, но получился какой-то ужас.
//Помогите, пожалуйста, найти ошибки, чтобы потом можно было легче написать тесты.
//И, если это возможно, пропустить к финальному тестированию, а я доделаю программу и дам Вам ссылку на репозиторий в "Пачке".

public class HttpTaskServer {
    public static final int PORT = 8080;
    private HttpServer server;
    private TaskManager manager;
    private Gson gson;
     public HttpTaskServer(FileBackedTasksManager manager) throws IOException {
         this.server = HttpServer.create(new InetSocketAddress("localhost", PORT), 0);
         server.createContext("/tasks", this::handler);
         this.manager = manager;
         this.gson = Managers.getGson();
     }

     public void handler(HttpExchange httpExchange){
         try{
             String path = httpExchange.getRequestURI().getPath();
             String query = httpExchange.getRequestURI().getQuery();
             String requestMethod  = httpExchange.getRequestMethod();
             switch (requestMethod){
                 case "GET":
                     if(Pattern.matches("^/tasks$", path)){
                         String response = gson.toJson(manager.getPrioritizedTask());
                         send(httpExchange, response);
                     } else if(Pattern.matches("^/tasks/task/$", path)) {
                         if(query==null) {
                             String response = gson.toJson(manager.getAllTasks());
                             send(httpExchange, response);
                         }else {
                             query = query.replaceFirst("id=", "");
                             int id = parseId(query);
                             if (id != -1) {
                                 if(manager.containTaskId(id)) {
                                     String response = gson.toJson(manager.getTaskById(id));
                                     send(httpExchange, response);
                                 } else{
                                     httpExchange.sendResponseHeaders(403, 0);
                                 }
                             } else {
                                 httpExchange.sendResponseHeaders(405, 0);
                                 System.out.println("Введен неверный id.");
                             }
                         }
                     }else if(Pattern.matches("^/tasks/subtask/$", path)){
                         if(query==null) {
                             String response = gson.toJson(manager.getAllSubTasks());
                             send(httpExchange, response);
                         } else {
                             query = query.replaceFirst("id=", "");
                             int id = parseId(query);
                             if (id != -1) {
                                 if(manager.containSubTaskId(id)) {
                                     String response = gson.toJson(manager.getSubTaskById(id));
                                     send(httpExchange, response);
                                 } else{
                                 httpExchange.sendResponseHeaders(403, 0);
                             }
                             } else {
                                 httpExchange.sendResponseHeaders(405, 0);
                                 System.out.println("Введен неверный id.");
                             }
                         }
                     } else if(Pattern.matches("^/tasks/epic/$", path)){
                         if(query==null) {
                             String response = gson.toJson(manager.getAllEpics());
                             send(httpExchange, response);
                         }else {
                             query = query.replaceFirst("id=", "");
                             int id = parseId(query);
                             if (id != -1) {
                                 if(manager.containEpicId(id)){
                                 String response = gson.toJson(manager.getEpicById(id));
                                 send(httpExchange, response);
                                 } else{
                                     httpExchange.sendResponseHeaders(403, 0);
                                 }
                             } else {
                                 httpExchange.sendResponseHeaders(405, 0);
                                 System.out.println("Введен неверный id.");
                             }
                         }
                     } else if(Pattern.matches("^/tasks/subtask/epic/", path)){
                         query = query.replaceFirst("id=", "");
                         int id = parseId(query);
                         if (id != -1) {
                             if(manager.containEpicId(id)){
                             String response = gson.toJson(manager.getAllSubTasksByEpic(id));
                             send(httpExchange, response);
                             } else{
                                 httpExchange.sendResponseHeaders(403, 0);
                             }
                         } else {
                             httpExchange.sendResponseHeaders(405, 0);
                             System.out.println("Введен неверный id.");
                         }

                     }else if(Pattern.matches("^/tasks/history$", path)){
                         List<Task> tasks = manager.getHistory();
                         String response = gson.toJson(tasks);
                         send(httpExchange, response);
                     }
                     break;
                 case "POST":
                     if(Pattern.matches("^/tasks/task/$", path)) {
                         String body = readText(httpExchange);
                         JsonElement jsonElement = JsonParser.parseString(body);
                         JsonObject jsonObject = jsonElement.getAsJsonObject();
                         Task task = gson.fromJson(jsonObject, Task.class);
                         if(manager.containTaskId(task.getID())){
                             manager.updateTask(task);
                             String response = "Задача под id = " + task.getID() + "обновлена";
                             send(httpExchange, response);
                         } else{
                             manager.createTask(task);
                             String response = "Задача под id = " + task.getID() + "создана";
                             send(httpExchange, response);
                         }
                     } else if(Pattern.matches("^/tasks/subtask/$", path)) {
                         String body = readText(httpExchange);
                         JsonElement jsonElement = JsonParser.parseString(body);
                         JsonObject jsonObject = jsonElement.getAsJsonObject();
                         SubTask subTask = gson.fromJson(jsonObject, SubTask.class);
                         if(manager.containSubTaskId(subTask.getID())){
                             manager.updateSubTask(subTask);
                             String response = "Подзадача под id = " + subTask.getID() + "обновлена";
                             send(httpExchange, response);
                         } else{
                             manager.createSubTask(subTask);
                             String response = "Подзадача под id = " + subTask.getID() + "создана";
                             send(httpExchange, response);
                         }
                     } else if(Pattern.matches("^/tasks/epic/$", path)) {
                     String body = readText(httpExchange);
                     JsonElement jsonElement = JsonParser.parseString(body);
                     JsonObject jsonObject = jsonElement.getAsJsonObject();
                     Epic epic = gson.fromJson(jsonObject, Epic.class);
                     if(manager.containEpicId(epic.getID())){
                         manager.updateEpic(epic);
                         String response = "Епик-задача под id = " + epic.getID() + "обновлена";
                         send(httpExchange, response);
                     } else{
                         manager.createEpic(epic);
                         String response = "Епик-задача под id = " + epic.getID() + "создана";
                         send(httpExchange, response);
                     }
                 }
                     break;
                 case "DELETE":
                     if(Pattern.matches("^/tasks/task/$", path)){
                         if(query!=null){
                         query = query.replaceFirst("id=", "");
                         int id = parseId(query);
                         if (id != -1) {
                             if(manager.containTaskId(id)) {
                                 manager.deleteTaskById(id);
                                 httpExchange.sendResponseHeaders(200, 1);
                                 System.out.println("Удалена задача с индефитикатором - " + id);
                             } else{
                                 httpExchange.sendResponseHeaders(403, 0);
                             }
                         } else {
                             httpExchange.sendResponseHeaders(405, 0);
                             System.out.println("Введен неверный id.");
                         }

                     }else {
                             manager.deleteAllTasks();
                             httpExchange.sendResponseHeaders(200, 1);
                             System.out.println("Все задачи были удалены.");
                         }
                     } else if(Pattern.matches("^/tasks/subtask/$", path)){
                         if(query!=null) {
                             query = query.replaceFirst("id=", "");
                             int id = parseId(query);
                             if (id != -1) {
                                 if (manager.containSubTaskId(id)) {
                                     manager.deleteSubTaskById(id);
                                     httpExchange.sendResponseHeaders(200, 1);
                                     System.out.println("Удалена подзадача с индефитикатором - " + id);
                                 } else {
                                     httpExchange.sendResponseHeaders(403, 0);
                                 }
                             } else {
                                 httpExchange.sendResponseHeaders(405, 0);
                                 System.out.println("Введен неверный id.");
                             }
                         }else{
                             manager.deleteAllSubTasks();
                             httpExchange.sendResponseHeaders(200,1);
                             System.out.println("Все подзадачи были удалены.");
                         }
                     } else if(Pattern.matches("^/tasks/epic/$", path)) {
                         if (query != null) {
                             query = query.replaceFirst("id=", "");
                             int id = parseId(query);
                             if (id != -1) {
                                 if (manager.containEpicId(id)) {
                                     manager.deleteEpicById(id);
                                     httpExchange.sendResponseHeaders(200, 1);
                                     System.out.println("Удалена епик-задача с индефитикатором - " + id);
                                 } else {
                                     httpExchange.sendResponseHeaders(403, 0);
                                 }
                             } else {
                                 httpExchange.sendResponseHeaders(405, 0);
                                 System.out.println("Введен неверный id.");
                             }
                         } else {
                             manager.deleteAllEpics();
                             httpExchange.sendResponseHeaders(200, 1);
                             System.out.println("Все епик-задачи были удалены.");
                         }
                     }
                     break;
                 default:
                     System.out.println("неправильный URL, проверьте ссылку!");
                     httpExchange.sendResponseHeaders(404, 0);
                     break;
             }

         }catch (Exception e){
             System.out.println("Произошла ошибка");

         } finally {
             httpExchange.close();
         }
     }
     private int parseId(String Path){
         try{
             return Integer.parseInt(Path);
         }catch(NumberFormatException e){
             return -1;
         }
     }

     private String readText(HttpExchange httpExchange) throws IOException {
         return new String(httpExchange.getRequestBody().readAllBytes(), UTF_8);
     }

     private void send(HttpExchange httpExchange, String response) throws IOException {
         byte[] resp = response.getBytes(UTF_8);
         httpExchange.getResponseHeaders().set("Content-Type", "application/json;charset=utf-8");
         httpExchange.sendResponseHeaders(200, resp.length);
         httpExchange.getResponseBody().write(resp);
     }

     public void start(){
         server.start();
         System.out.println("Сервер запущен на порту - " + PORT);
     }
     public void stop(){
         server.stop(0);
         System.out.println("Сервер остановлен!");
     }

    @Override
    public int hashCode() {
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return super.equals(obj);
    }
}
