package model;

import java.util.Objects;

public class Task {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;

    public Task(String name, String description, Status status, int id) { //конструктор для создания копии
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String string) {
        String[] parts = string.split(",");
        this.name = parts[2];
        this.description = parts[4];
        this.status = Status.valueOf(parts[3]);
        this.id = Integer.parseInt(parts[0]);
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

    public Task(String name, Status status) {
        this.name = name;
        this.status = status;
    }


    public Task clone() {
        return new Task(this.name, this.description, this.status, this.id);
    }

    public void setID(int id) {
        this.id = id;
    }

    public Status getStatus() {
        return status;
    }

    public int getID() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDescription(String description) {
        this.description = description;
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
        return task != null && !task.getName().isEmpty();
    }

    public String toFileString() {
        return String.format("%s,%s,%s,%s,%s\n", id, TaskType.TASK, name, status, description);
    }

    @Override
    public String toString() {
        return "model.Task{'name='" + name + ", 'id'=" + id + ", 'model.Status'=" + status + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
