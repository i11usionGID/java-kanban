package finaltask.manager.file;


import finaltask.exception.ManagerSaveException;
import finaltask.manager.InMemoryTaskManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;
import finaltask.task.TaskType;

import java.io.*;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private File file;
    private static CSVFormatHandler handler = new CSVFormatHandler();
    public FileBackedTasksManager(File file){
        if(file==null){
            this.file = null;
        }else {
            this.file = file;
        }
    }
    @Override
    public List<Task> getAllTasks(){
        List<Task> tasks = super.getAllTasks();
        save();
        return tasks;
    }

    @Override
    public List<SubTask> getAllSubTasks(){
        List<SubTask> subTasks = super.getAllSubTasks();
        save();
        return subTasks;
    }

    @Override
    public List<Epic> getAllEpics(){
        List<Epic> epics =  super.getAllEpics();
        save();
        return epics;
    }

    @Override
    public Task getTaskById(int id){
        Task taskById = super.getTaskById(id);
        save();
        return taskById;
    }
    @Override
    public SubTask getSubTaskById(int id){
        SubTask subTaskById = super.getSubTaskById(id);
        save();
        return subTaskById;
    }

    @Override
    public Epic getEpicById(int epicId){
        Epic epicById = super.getEpicById(epicId);
        save();
        return epicById;
    }

    @Override
    public List<Task> getPrioritizedTask() {
        return super.getPrioritizedTask();
    }

    @Override
    public void deleteSubTaskById(int id) {
        super.deleteSubTaskById(id);
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public List<SubTask> getAllSubTasksByEpic(int idOfEpic){
        List<SubTask> allSubTasks = super.getAllSubTasksByEpic(idOfEpic);
        save();
        return allSubTasks;
    }


    public void save() throws ManagerSaveException {
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(file))){

            writer.write(handler.getHeader());
            writer.newLine();

            for(Task task:taskMap.values()){
                writer.write(handler.toString(task));
                writer.newLine();
            }

            for(Epic epic:epicMap.values()){
                writer.write(handler.toString(epic));
                writer.newLine();
            }

            for(SubTask subTask:subTaskMap.values()){
                writer.write(handler.toString(subTask));
                writer.newLine();
            }

            writer.newLine();
            writer.write(handler.historyToString(historyManager));

            } catch (IOException e) {
            throw new ManagerSaveException("Невозможно прочитать файл!");
        }

    }
    //id,type,name,status,description,epic
    public static FileBackedTasksManager  loadFromFile(File file) throws ManagerSaveException {

        FileBackedTasksManager manager = new FileBackedTasksManager(file);
        try(BufferedReader br = new BufferedReader(new FileReader(file))){
            br.readLine();
            while (br.ready()){
               String line = br.readLine();
               if(line.isEmpty()){
                   String historyLine = br.readLine();
                   List<Integer> numbers = handler.historyFromString(historyLine);
                   for(Integer number: numbers){
                       if(number> manager.generatedID){
                           manager.generatedID = number;
                       }
                       if(manager.taskMap.containsKey(number)) {
                           manager.historyManager.add(manager.getTaskById(number));
                       }else if(manager.epicMap.containsKey(number)) {
                           manager.historyManager.add(manager.getEpicById(number));
                       }else {
                           manager.historyManager.add(manager.getSubTaskById(number));
                       }
                   }

               } else {
                   String[] elements = line.split(",");
                   if (TaskType.valueOf(elements[1]).equals(TaskType.TASK)) {
                       Task task = handler.fromString(line);
                       manager.taskMap.put(task.getID(), task);
                   } else if (TaskType.valueOf(elements[1]).equals(TaskType.EPIC)) {
                       Epic epic = (Epic) handler.fromString(line);
                       manager.epicMap.put(epic.getID(), epic);
                   } else {
                       SubTask subTask = (SubTask) handler.fromString(line);
                       manager.subTaskMap.put(subTask.getID(), subTask);
                       manager.getEpicById(subTask.getEpicId()).setSubTaskIdInArrayList(subTask.getID());
                   }
               }


            }

            return manager;

        } catch(IOException e){

            throw new ManagerSaveException("Невозможно открыть файл");
        }
    }
}
