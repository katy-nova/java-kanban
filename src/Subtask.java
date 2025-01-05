public class Subtask extends Task{
    int epicId;

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
        super(name, description, ID, status);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }
}
