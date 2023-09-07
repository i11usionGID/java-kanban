package finaltask.task;

import finaltask.TaskStatus.TaskStatus;

public class Task {
    protected String taskName;
    protected String description;
    protected int ID;
    protected TaskStatus status;
    protected  TaskType type;

    public TaskType getType() {
        return type;
    }

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.type = TaskType.TASK;
        this.status = TaskStatus.NEW;
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
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", ID=" + ID +
                ", status='" + status + '\'' +
                '}';
    }

}
