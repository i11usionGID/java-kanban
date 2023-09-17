package finaltask.manager.file;

import finaltask.TaskStatus.TaskStatus;
import finaltask.manager.HistoryManager;
import finaltask.task.Epic;
import finaltask.task.SubTask;
import finaltask.task.Task;
import finaltask.task.TaskType;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVFormatHandler {

    private static final String DELIMITER = ",";
    String toString(Task task){

        String result = task.getID() + DELIMITER +
                task.getType() + DELIMITER +
                task.getTaskName() + DELIMITER +
                task.getStatus() + DELIMITER +
                task.getDescription() + DELIMITER +
                task.getStartTime() + DELIMITER +
                task.getDuration() + DELIMITER;
        if(task.getType() == TaskType.SUBTASK){
            result = result + ((SubTask) task).getEpicId();
        }
        return result;
    }

    Task fromString(String value){
        String [] elements = value.split(",");
        TaskType.valueOf(elements[1]);
        if(TaskType.valueOf(elements[1]).equals(TaskType.TASK)){
            Task task = new Task(elements[2], elements[4]);
            task.setStatus(TaskStatus.valueOf(elements[3]));
            if(elements[5].equals("null") && elements[6].equals("null")) {
                task.setStartTime(null);
                task.setDuration(null);
            } else {
                task.setStartTime(LocalDateTime.parse(elements[5]));
                task.setDuration(Duration.parse(elements[6]));
            }
            task.setID(Integer.parseInt(elements[0]));
            return task;

        } else if(TaskType.valueOf(elements[1]).equals(TaskType.EPIC)) {

            Epic epic = new Epic(elements[2], elements[4]);
            epic.setStatus(TaskStatus.valueOf(elements[3]));
            if(elements[5].equals("null") && elements[6].equals("null")) {
                epic.setStartTime(null);
                epic.setDuration(null);
            } else {
                epic.setStartTime(LocalDateTime.parse(elements[5]));
                epic.setDuration(Duration.parse(elements[6]));
            }
            epic.setID(Integer.parseInt(elements[0]));
            return epic;

        } else{

            SubTask subTask = new SubTask(elements[2], elements[4], Integer.parseInt(elements[7]));
            subTask.setStatus(TaskStatus.valueOf(elements[3]));
            if(elements[5].equals("null") && elements[6].equals("null")) {
                subTask.setStartTime(null);
                subTask.setDuration(null);
            } else {
                subTask.setStartTime(LocalDateTime.parse(elements[5]));
                subTask.setDuration(Duration.parse(elements[6]));
            }
            subTask.setID(Integer.parseInt(elements[0]));
            return subTask;
        }
    }

    String historyToString(HistoryManager manager){

        List<String> result = new ArrayList<>();

        for(Task task: manager.getHistory()){
            result.add(String.valueOf(task.getID()));
        }

        return String.join(DELIMITER, result);
    }

    List<Integer> historyFromString(String value){
        List<Integer> historyNumbers = new ArrayList<>();
        String [] numbers = value.split(",");
        for(String number: numbers){
            historyNumbers.add(Integer.parseInt(number));
        }
        return historyNumbers;
    }

    String getHeader(){
        return "id,type,name,status,description,epic";
    }
}
