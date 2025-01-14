import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Integer> subtasks = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name, String description, Status status, int ID) {
        super(name, description, status, ID);
    }

    public Epic(String name) {
        super(name);
    }

    public ArrayList<Integer> getSubtasks() {
        return subtasks;
    }


    public void addSubtask(int subtaskID) {
        for (Integer ID: subtasks) {
            if (ID == subtaskID) {
                return;
            }
        }
        subtasks.add(subtaskID);
    }

    public void deleteSubtask(int ID) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i) == ID){
                subtasks.remove(i);
            }
        }
    }

    public void clearEpic() {
        if (!subtasks.isEmpty()) {
            subtasks.clear();
        }
    }


    @Override
    public String toString() {
        return "Epic{'name='" + name + ", 'ID'=" + ID +", 'Status'="+ status + "\n Subtasks:" + subtasks + "}";
    }
}
