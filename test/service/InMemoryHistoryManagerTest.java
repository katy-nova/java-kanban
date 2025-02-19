import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.history.HistoryManager;
import service.Managers;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {

    HistoryManager historyManager = Managers.getDefaultHistoryManager();
    Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);

    @Test
    void shouldRewriteSameTaskAndSaveWordOrder() {
        Task task1 = new Task("Tes1", "Test addNewTask description", Status.NEW, 1);
        Task task2 = new Task("Tes2", "Test addNewTask description", Status.NEW, 2);
        Task task3 = new Task("Tes3", "Test addNewTask description", Status.NEW, 3);
        historyManager.addTask(task);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
        System.out.println(historyManager.getTasks());
        Task task1Copy = new Task("Tes1Copy", "Test addNewTask description", Status.NEW, 1);
        Task task2Copy = new Task("Tes2Copy", "Test addNewTask description", Status.NEW, 2);
        historyManager.addTask(task1Copy);
        historyManager.addTask(task2Copy);
        assertEquals(4, historyManager.getTasks().size());
        System.out.println(historyManager.getTasks());
    }

    @Test
    void add() {
        historyManager.addTask(task);
        assertNotNull(historyManager.getTasks(), "История не пустая.");
        assertEquals(1, historyManager.getTasks().size(), "История не пустая.");
    }

    @Test
    void checkImmutabilityOfTask() {
        Task clone = task.clone();
        historyManager.addTask(task);
        task.renovateTask("Change", "Change", Status.DONE);
        assertEquals(clone, historyManager.getTasks().get(task.getID()));
    }
}