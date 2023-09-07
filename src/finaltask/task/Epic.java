package finaltask.task;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subTaskIds=new ArrayList<>();

    @Override
    public TaskType getType() {
        return super.getType();
    }

    public Epic(String taskName, String description) {
        super(taskName, description);
        this.type=TaskType.EPIC;
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
                '}';
    }
}
