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

    public Subtask(String name, String description, int ID, Status status, int epicId) {
        super(name, description, status, ID);
        this.epicId = epicId;
    }

    @Override
    public Subtask clone() {
        return new Subtask(this.name, this.description, this.ID, this.status, this.epicId);
    }

    public int getEpicId() {
        return epicId;
    }
}
