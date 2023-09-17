package finaltask.task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds=new ArrayList<>();

    @Override
    public TaskType getType() {
        return super.getType();
    }

    @Override
    public Duration getDuration() {
        return super.getDuration();
    }

    @Override
    public void setDuration(Duration duration) {
        super.setDuration(duration);
    }

    @Override
    public LocalDateTime getStartTime() {
        return super.getStartTime();
    }

    @Override
    public void setStartTime(LocalDateTime startTime) {
        super.setStartTime(startTime);
    }

    public LocalDateTime getEndTime(ArrayList<SubTask> subTasks) {
        LocalDateTime max = startTime;
        for (SubTask subTask:subTasks) {
           LocalDateTime time = subTask.getEndTime();
           if(max.isBefore(time)){
               max = time;
           }
        }
        return max;
    }

    public Epic(String taskName, String description) {
        super(taskName, description);
        this.type=TaskType.EPIC;
    }

    public LocalDateTime getEndTime(HashMap<Integer, SubTask> subTaskMap) {
        LocalDateTime lattestDate = LocalDateTime.of(0, 1, 1, 1, 1);
        for (Integer id : subTaskIds) {
            if (subTaskMap.containsKey(id)) {
                if (subTaskMap.get(id).getEndTime().isAfter(lattestDate)) {
                    lattestDate = subTaskMap.get(id).getEndTime();
                }
            }
        }
        return lattestDate;
    }


    public void setSubTaskIdInArrayList(int subTaskId){
        subTaskIds.add(subTaskId);
    }
    public ArrayList<Integer> getSubTaskIdsArrayList(){
        return subTaskIds;
    }

    @Override
    public String toString() {
        return "Epic{" +
                "subTaskIds=" + subTaskIds +
                ", taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }
}
