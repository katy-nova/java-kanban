package model;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, Status status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public Subtask(String name, Status status, int epicId) {
        super(name, status);
        this.epicId = epicId;
    }

    public Subtask(String name, int epicId) {
        super(name);
        this.epicId = epicId;
    }

    public Subtask(String string) {
        super(string);
        String[] parts = string.split(",");
        this.epicId = Integer.parseInt(parts[5]);
    }

    public Subtask(String name, String description, int id, Status status, int epicId) {
        super(name, description, status, id);
        this.epicId = epicId;
    }

    @Override
    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s,%s\n", id, TaskType.SUBTASK, name, status, description, epicId);
    }

    @Override
    public String toString() {
        return "model.Subtask{'name='" + name +
                ", 'id'=" + id +
                ", 'model.Status'=" + status +
                ", 'epicID'=" + epicId + "}";
    }

    @Override
    public Subtask clone() {
        return new Subtask(this.name, this.description, this.id, this.status, this.epicId);
    }

    public int getEpicId() {
        return epicId;
    }
}
