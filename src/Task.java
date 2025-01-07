import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int ID;
    protected Status status;

    public Task(String name, String description, Status status, int ID) { //конструктор для создания копии
        this.name = name;
        this.description = description;
        this.ID = ID;
        this.status = status;
    }
    public Task(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task(String name,  Status status) {
        this.name = name;
        this.status = status;
    }

    public Task(String name) {
        this.name = name;
        status = Status.NEW;
    }

    public void setID(int ID) {
        this.ID = ID;
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


    public void setStatus(Status status) {
        this.status = status;
    }

    public void renovateTask(String name, String description, Status status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }


    public static boolean check(Task task) {
        return task!= null && !task.getName().isEmpty() && task.getID() == 0;
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
