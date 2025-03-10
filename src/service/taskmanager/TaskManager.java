package service.taskmanager;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.TreeSet;

public interface TaskManager {

    void addTask(Task task) throws IllegalStateException;

    void addSubtask(Subtask subtask) throws IllegalStateException;

    void addEpic(Epic epic) throws IllegalStateException;

    void updateTask(Task task) throws IllegalStateException;

    void updateSubtask(Subtask subtask) throws IllegalStateException;

    void updateEpic(Epic newEpic) throws IllegalStateException;

    void clearTasks();

    void clearSubtasks();

    void clearEpics();

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Optional<Task> getTask(int id);

    Optional<Subtask> getSubtask(int id);

    Optional<Epic> getEpic(int id);

    void deleteTask(int id);

    void deleteSubtask(int id);

    void deleteEpic(int id);

    ArrayList<Task> getHistory();

    TreeSet<Task> getPrioritizedTasks();
}
