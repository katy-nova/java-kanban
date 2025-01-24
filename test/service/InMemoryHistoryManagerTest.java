import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.HistoryManager;
import service.Managers;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistoryManager();

    @BeforeEach
    public void beforeEach() {
        // хотела создать задачу и добавить ее в историю в этом методе, но тогда она не видна в других методах(
    }

    @Test
    void add() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW );
        historyManager.addTask(task);
        final ArrayList<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
        assertEquals(1, history.size(), "История не пустая.");
    }

    @Test
    void checkImmutabilityOfTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW );
        Task clone = task.clone();
        historyManager.addTask(task);
        final ArrayList<Task> history = historyManager.getHistory();
        task.renovateTask("Change", "Change", Status.DONE);
        assertEquals(historyManager.getHistory().get(0), clone);
        assertNotEquals(historyManager.getHistory().get(0), task);
    }
}