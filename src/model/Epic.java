package model;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Integer> subtasks = new ArrayList<>();

    public Epic(String name, String description) {
        super(name, description, Status.NEW); // исправила, но у меня в конструкторе класса Task если статус не задан,
        // то он автоматически присваивается как NEW
    }

    public Epic(String name, String description, Status status, int id) {
        super(name, description, status, id);
    }

    public Epic(String name) {
        super(name);
    }

    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void addSubtask(int subtaskID) {
        if (subtaskID == id) { // если добавляем эпик сам в себя, он не добавляется
            return;
        }
        for (Integer id : subtasks) {
            if (id == subtaskID) {
                return;
            }
        }
        subtasks.add(subtaskID);
    }

    public void deleteSubtask(int id) {
        for (int i = 0; i < subtasks.size(); i++) {
            if (subtasks.get(i) == id) {
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
    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s\n", id, TaskType.EPIC, name, status, description);
    }

    @Override
    public String toString() {
        return "model.Epic{'name='" + name + ", 'id'=" + id + ", 'model.Status'=" + status + "\n Subtasks:" + subtasks + "}";
    }

    @Override
    public Epic clone() {
        Epic epic = new Epic(this.name, this.description, this.status, this.id);
        for (Integer subtask : subtasks) {
            epic.addSubtask(subtask);
        }
        return epic;
    }
}
