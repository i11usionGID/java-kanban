package finaltask.task;

import java.time.Duration;
import java.time.LocalDateTime;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
        this.type=TaskType.SUBTASK;
    }

    public SubTask(String taskName, String description, int epicId, LocalDateTime startTime, Duration duration) {
        super(taskName, description);
        this.epicId = epicId;
        this.type=TaskType.SUBTASK;
        this.startTime = startTime;
        this.duration = duration;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public LocalDateTime getEndTime() {
        return super.getEndTime();
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        if (this != null) {
            return "SubTask{" +
                    "epicId=" + epicId +
                    ", taskName='" + taskName + '\'' +
                    ", description='" + description + '\'' +
                    ", ID=" + ID +
                    ", status='" + status + '\'' +
                    ", startTime='" + startTime + '\'' +
                    ", duration='" + duration + '\'' +
                    '}';
        } else {
            return " ";
        }
    }
}
