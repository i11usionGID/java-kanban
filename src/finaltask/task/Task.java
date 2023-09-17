package finaltask.task;

import finaltask.TaskStatus.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;


public class Task {
    protected String taskName;
    protected String description;
    protected int ID;
    protected TaskStatus status;
    protected  TaskType type;
    protected LocalDateTime startTime = LocalDateTime.of(0,1,1,0,1);
    protected Duration duration = Duration.ZERO;

    public TaskType getType() {
        return type;
    }

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.type = TaskType.TASK;
        this.status = TaskStatus.NEW;
    }
    public Task(String taskName, String description, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.type = TaskType.TASK;
        this.status = TaskStatus.NEW;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTaskName() {
        return taskName;
    }
    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }
    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public int getID() {
        return ID;
    }
    public void setID(int ID) {
        this.ID = ID;
    }
    public TaskStatus getStatus() {
        return status;
    }
    public void setStatus(TaskStatus status) {
        this.status = status;
        if(status==TaskStatus.DONE){
        }
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime(){
        int minutes = (int) duration.toMinutes();
        LocalDateTime endTime = startTime.plusMinutes(minutes);
        return endTime;
    }


    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status='" + status + '\'' +
                ", startTime='" + startTime + '\'' +
                ", duration='" + duration + '\'' +
                '}';
    }

}
