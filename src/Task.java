import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected final int ID;
    protected Status status;
    protected static int count = 0;

    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.ID = addID();
        this.status = status;
    }

    public Task(String name, String description, int ID, Status status) { //конструктор для создания копии
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        this.ID = addID();
        status = Status.NEW;
    }

    public Task(String name,  Status status) {
        this.name = name;
        this.ID = addID();
        this.status = status;
    }

    public Task(String name) {
        this.name = name;
        this.ID = addID();
        status = Status.NEW;
    }


    public Status getStatus() {
        return status;
    }

    public int getID() {
        return ID;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    protected int addID() {
        count++;
        return count;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public void renovateTask(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public static boolean check(Task task) {
        return task != null && !task.getName().isEmpty() && task.getID() > 0;
    }

    @Override
    public String toString() {
        return "Task{'name='" + name + ", 'ID'=" + ID + ", 'Status'=" + status + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return ID == task.ID && Objects.equals(name, task.name) && Objects.equals(description, task.description) && status == task.status;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, ID, status);
    }
}
