package service;

import model.Task;
import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager{

    private final List<Task> history = new LinkedList<>();
    private final int MAX_SIZE_OF_HISTORY = 10;

    @Override
    public void addTask(Task task) {
        if (Task.check(task)) {
            if (history.size() >= MAX_SIZE_OF_HISTORY) {
                history.removeFirst();
            }
            history.add(task.clone());
            // если не использовать клонирование, то как реализовать хранение предыдущей версии задачи?
            // в ТЗ сказано:
            // "убедитесь, что задачи, добавляемые в HistoryManager, сохраняют предыдущую версию задачи и её данных."
        }
    }

    @Override
    public List<Task> getHistory() {
        return  List.copyOf(history);
    }
}
