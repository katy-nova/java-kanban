package service;

import model.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager{

    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void addTask(Task task) {
        if (Task.check(task)) {
            if (history.size() >= 10) {
                history.remove(0);
            }
            history.add(task.clone());
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }
}
