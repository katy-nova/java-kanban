import java.util.ArrayList;

public class Epic extends Task{
    ArrayList<Subtask> epic = new ArrayList<>();


    public Epic(String name, String description) {
        super(name, description);
    }


    public Epic(String name) {
        super(name);
    }

    public ArrayList<Subtask> getEpic() {
        return epic;
    }

    public void addSubtask(Subtask subtask) {
        for (Subtask epicSubtask: epic) {
            if (epicSubtask.equals(subtask)) {
                return;
            }
        }
        epic.add(subtask);
        setEpicStatus();
    }

    public void deleteSubtask(int ID) {
        for (int i = 0; i < epic.size(); i++) {
            if (epic.get(i).getID() == ID) {
                epic.remove(i);
            }
        }
        setEpicStatus();
    }

    public void clearEpic() {
        if (!epic.isEmpty()) {
            epic.clear();
            setStatus(Status.NEW);
        }
    }


    private void setEpicStatus() {
        boolean isNew = true;
        boolean isDone = true;
        for (Subtask epicSubtask: epic) {
            if (epicSubtask.getStatus() == Status.IN_PROGRESS) { // если хотя бы у 1 задачи статус "в процессе"
                setStatus(Status.IN_PROGRESS); // то весь эпик в статусе "в процессе"
                return;
            } else if (epicSubtask.getStatus() == Status.NEW) {
                isDone = false; //если хотя бы у 1 задачи статус новая, то эпик не может быть done
            } else if (epicSubtask.getStatus() == Status.DONE) {
                isNew = false; //если хотя бы у 1 задачи статус done, то эпик не может быть новым
            }
            if (!isNew && !isDone) {// если есть хотя бы по 1 задаче new и done, устанавливаем статус и выходим
                setStatus(Status.IN_PROGRESS); // дальше проверять нет смысла
                return;
            }
        }
        if (isNew) {
            setStatus(Status.NEW);
        } else {
            setStatus(Status.DONE);
        }
    }

    @Override
    public String toString() {
        return "Epic{'name='" + name + ", 'ID'=" + ID +", 'Status'="+ status + "\n Subtasks:" + epic + "}";
    }
}
