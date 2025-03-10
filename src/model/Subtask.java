package model;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    protected int epicId;

    public Subtask(String name, String description, Status status, int epicId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, String description, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, description, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String name, Status status, int epicId, Duration duration, LocalDateTime startTime) {
        super(name, status, duration, startTime);
        this.epicId = epicId;
    }

    public Subtask(String string) {
        super(string);
        String[] parts = string.split(",");
        this.epicId = Integer.parseInt(parts[7]);
    }

    public Subtask(String name, String description, int id, Status status, int epicId, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, id, duration, startTime);
        this.epicId = epicId;
    }

    @Override
    public String toFileString() {
        if (duration == null) {
            duration = Duration.ofMinutes(0); // выставляем 0 минут для корректной записи в вайл
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s,%s,\n", id, TaskType.SUBTASK, name, status, description,
                duration.toMinutes(), startTime, epicId);
    }

    @Override
    public String toString() {
        if (duration == null || startTime == null) {
            return "model.Subtask{'name='" + name +
                    ", 'id'=" + id +
                    ", 'model.Status'=" + status +
                    ", 'epicID'=" + epicId +
                    ", 'time' = no information}";
        }
        return "model.Subtask{'name='" + name +
                ", 'id'=" + id +
                ", 'model.Status'=" + status +
                ", 'epicID'=" + epicId +
                ", 'duration'=" + duration.toMinutes() +
                "min, 'startTime'=" + startTime + "}";
    }

    @Override
    public Subtask clone() {
        return new Subtask(this.name, this.description, this.id, this.status, this.epicId, this.duration,
                this.startTime);
    }

    public int getEpicId() {
        return epicId;
    }
}
