import model.Status;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.history.HistoryManager;

import static org.junit.jupiter.api.Assertions.*;

public abstract class HistoryManagerTest<T extends HistoryManager> {
    protected abstract T getHistoryManager();

    protected HistoryManager historyManager;
    protected Task task;
    protected Task task1;
    protected Task task2;
    protected Task task3;

    @BeforeEach
    void setUp() {
        historyManager = getHistoryManager();
        task = new Task("Tes", "Test addNewTask description", Status.NEW, 1);
        task1 = new Task("Tes1", "Test addNewTask description", Status.NEW, 2);
        task2 = new Task("Tes2", "Test addNewTask description", Status.NEW, 3);
        task3 = new Task("Tes3", "Test addNewTask description", Status.NEW, 4);
        historyManager.addTask(task);
        historyManager.addTask(task1);
        historyManager.addTask(task2);
        historyManager.addTask(task3);
    }

    @Test
    void shouldRemove() {
        historyManager.remove(task1.getID());
        Task[] array1 = {task, task2, task3};
        assertArrayEquals(array1, historyManager.getTasks().toArray());
        historyManager.remove(task3.getID());
        Task[] array2 = {task, task2};
        assertArrayEquals(array2, historyManager.getTasks().toArray());
        historyManager.remove(task.getID());
        Task[] array3 = {task2};
        assertArrayEquals(array3, historyManager.getTasks().toArray());

    }

    @Test
    void shouldRewriteSameTaskAndSaveWordOrder() {
        Task task1Copy = task1.clone();
        task1Copy.setName("Test1copy");
        Task task2Copy = task2.clone();
        task2Copy.setName("Test2copy");
        historyManager.addTask(task1Copy);
        historyManager.addTask(task2Copy);
        assertEquals(4, historyManager.getTasks().size());
        Task[] array = {task, task3, task1Copy, task2Copy};
        assertArrayEquals(array, historyManager.getTasks().toArray());
    }

    @Test
    void add() {
        Task[] correctOrder = {task, task1, task2, task3};
        assertNotNull(historyManager.getTasks(), "История не пустая.");
        assertEquals(4, historyManager.getTasks().size(), "История не пустая.");
        assertArrayEquals(correctOrder, historyManager.getTasks().toArray());
    }

}
