package service;

import model.Task;
import java.util.ArrayList;

public interface HistoryManager {

    void addTask(Task task);

    void remove(int id);

    void linkLast(Task task);

    ArrayList<Task> getTasks();

    void removeNode(Node node);

    Node getNode(Task task);

    ArrayList<Task> getHistory();
}
