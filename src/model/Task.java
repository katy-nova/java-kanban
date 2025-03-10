package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {
    protected String name;
    protected String description;
    protected int id;
    protected Status status;
    protected Duration duration;
    protected LocalDateTime startTime;

    public Task(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        //конструктор для создания копии (имеет другой модификатор, чтобы не использовался в других целях)
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Status status, int id) {
        //конструктор для эпика
        this.name = name;
        this.description = description;
        this.id = id;
        this.status = status;
    }

    public Task(String name, String description) {
        //конструктор для эпика
        this.name = name;
        this.description = description;
        status = Status.NEW;
    }

    public Task(String string) {
        String[] parts = string.split(",");
        this.name = parts[2];
        this.description = parts[4];
        this.status = Status.valueOf(parts[3]);
        this.id = Integer.parseInt(parts[0]);
        this.duration = Duration.ofMinutes(Integer.parseInt(parts[5]));
        if (!parts[6].equals("null")) { // заносим дату из строчки, только если она существует
            this.startTime = LocalDateTime.parse(parts[6]);
        }
    }

    public Task(String name, String description, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, String description, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        status = Status.NEW;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(String name, Status status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    @Override
    public int compareTo(Task task) {
        return this.getStartTime().compareTo(task.getStartTime());
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    public Task clone() {
        return new Task(this.name, this.description, this.status, this.id, this.duration, this.startTime);
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
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
        return task != null && !task.getName().isEmpty() && (task.getDuration() == null && task.getStartTime() == null
                || task.getDuration() != null && task.getStartTime() != null);
        // временнЫе поля должны либо оба существовать, либо оба отсутствовать
    }

    public String toFileString() {
        if (duration == null) {
            duration = Duration.ofMinutes(0); // выставляем 0 минут для корректной записи в вайл
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s,\n", id, TaskType.TASK, name, status, description,
                duration.toMinutes(), startTime);
    }

    @Override
    public String toString() {
        if (duration == null || startTime == null) { // чтобы избежать исключений, если время не задано
            return "model.Task{'name='" + name +
                    ", 'id'=" + id +
                    ", 'model.Status'=" + status +
                    "', time' = no information}";
        }
        return "model.Task{'name='" + name +
                ", 'id'=" + id +
                ", 'model.Status'=" + status +
                ", 'duration'=" + duration.toMinutes() +
                "min, 'startTime'=" + startTime + "}";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id && Objects.equals(name, task.name)
                && Objects.equals(description, task.description)
                && status == task.status
                && Objects.equals(duration, task.duration)
                && Objects.equals(startTime, task.startTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, id, status, duration, startTime);
    }
}
