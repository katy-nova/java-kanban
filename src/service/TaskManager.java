package service;

import model.Epic;
import model.Subtask;
import model.Task;
import java.util.List;

public interface TaskManager {

    void addTask(Task task);

    void addSubtask(Subtask subtask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubtask(Subtask subtask);

    void updateEpic(Epic newEpic);

    void clearTasks();

    void clearSubtasks();

    void clearEpics();

    List<Task> getTasks();

    List<Subtask> getSubtasks();

    List<Epic> getEpics();

    Task getTask(int ID);

    Subtask getSubtask(int ID);

    Epic getEpic(int ID);

    void deleteTask(int ID);

    void deleteSubtask(int ID);

    void deleteEpic(int ID);

    List<Task> getHistory();

}
