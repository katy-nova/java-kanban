package service;

import service.history.HistoryManager;
import service.history.InMemoryHistoryManager;
import service.taskmanager.InMemoryTaskManager;
import service.taskmanager.TaskManager;

public final class Managers {

    private Managers() {

    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistoryManager() {
        return new InMemoryHistoryManager();
    }
}
