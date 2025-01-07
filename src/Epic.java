import java.util.ArrayList;

public class Epic extends Task{
    private final ArrayList<Integer> epic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }


    public Epic(String name) {
        super(name);
    }

    public ArrayList<Integer> getSubtasks() {
        return epic;
    }

    public void addSubtask(int subtaskID) {
        for (Integer ID: epic) {
            if (ID == subtaskID) {
                return;
            }
        }
        epic.add(subtaskID);
    }

    public void deleteSubtask(int ID) {
        for (int i = 0; i < epic.size(); i++) {
            if (epic.get(i) == ID){
                epic.remove(i);
            }
        }
    }

    public void clearEpic() {
        if (!epic.isEmpty()) {
            epic.clear();
            setStatus(Status.NEW);
        }
    }


    @Override
    public String toString() {
        return "Epic{'name='" + name + ", 'ID'=" + ID +", 'Status'="+ status + "\n Subtasks:" + epic + "}";
    }
}
