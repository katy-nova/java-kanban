package model;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class Epic extends Task {
    private final List<Integer> subtasks = new ArrayList<>();
    private LocalDateTime endTime;

    private Epic(String name, String description, Status status, int id, Duration duration, LocalDateTime startTime) {
        // конструктор для создания копии (имеет другой модификатор, чтобы не использовался в других целях)
        super(name, description, status, id, duration, startTime);
    }

    public Epic(String name, String description) {
        super(name, description);
    }

    public Epic(String name) {
        super(name);
    }

    public ArrayList<Integer> getSubtasks() {
        return new ArrayList<>(subtasks);
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
                break;
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
        if (duration == null) {
            duration = Duration.ofMinutes(0); // выставляем 0 минут для корректной записи в вайл
        }
        return String.format("%s,%s,%s,%s,%s,%s,%s\n", id, TaskType.EPIC, name, status, description,
                duration.toMinutes(), startTime);
    }

    @Override
    public String toString() {
        if (duration == null || startTime == null) {
            return "model.Epic{'name='" + name +
                    ", 'id'=" + id +
                    ", 'model.Status'=" + status +
                    ", 'time' = no information" +
                    "\n Subtasks:" + subtasks + "}";
        }
        return "model.Epic{'name='" + name +
                ", 'id'=" + id +
                ", 'model.Status'=" + status +
                ", 'duration'=" + duration.toMinutes() +
                "min, 'startTime'=" + startTime +
                ", 'endTime'=" + endTime.toString() +
                "\n Subtasks:" + subtasks + "}";
    }

    @Override
    public Epic clone() {
        Epic epic = new Epic(this.name, this.description, this.status, this.id, this.duration, this.startTime);
        epic.setEndTime(this.endTime);
        for (Integer subtask : subtasks) {
            epic.addSubtask(subtask);
        }
        return epic;
    }
}
