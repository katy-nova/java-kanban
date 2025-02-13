package service;

import model.Task;

import java.util.ArrayList;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int id);

    ArrayList<Task> getTasks();

    ArrayList<Task> getHistory();
}
