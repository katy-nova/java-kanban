import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.taskmanager.FileBackedTaskManager;
import service.taskmanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    File temp = File.createTempFile("prak", "csv");
    FileBackedTaskManager manager = new FileBackedTaskManager(temp);

    FileBackedTaskManagerTest() throws IOException {
    }

    @Test
    void fromString() {
        Subtask subtask = new Subtask("Test addNewTask", "description", 3, Status.DONE, 2,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 10, 2, 14, 20));
        String s = subtask.toFileString();
        Subtask subtaskFromString = new Subtask(s);
        assertEquals(subtask, subtaskFromString);
    }

    @Test
    void shouldSaveAndReload() {
        Task task1 = new Task("Tes1", "Test addNewTask description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 5, 23, 45));
        String s = task1.toFileString();
        Task copy = new Task(s);
        assertEquals(task1, copy);
        Task task2 = new Task("Tes2", "Test addNewTask description", Status.NEW,
                Duration.ofMinutes(90), LocalDateTime.of(2025, 3, 5, 20, 0));
        Task task3 = new Task("Tes3", "Test addNewTask description");
        Epic epic = new Epic("epic", "desc");
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        manager.addEpic(epic); // заносим пустой эпик без продолжительности и времени
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(temp);
        assertArrayEquals(manager.getTasks().toArray(), loadedManager.getTasks().toArray());
        assertArrayEquals(manager.getEpics().toArray(), loadedManager.getEpics().toArray());
    }

    @Override
    protected FileBackedTaskManager getTaskManager() {
        return new FileBackedTaskManager(temp);
    }
}