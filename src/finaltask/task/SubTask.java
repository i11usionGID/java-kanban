package finaltask.task;

public class SubTask extends Task {
    private int epicId;

    public SubTask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
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
                    '}';
        } else {
            return "";
        }
    }
}
