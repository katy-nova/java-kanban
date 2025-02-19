import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.taskmanager.FileBackedTaskManager;
import static org.junit.jupiter.api.Assertions.*;
import java.io.File;

class FileBackedTaskManagerTest {

    File file = new File("src" + File.separator + "base" + File.separator + "base.csv");
    FileBackedTaskManager manager = new FileBackedTaskManager(file);

    @Test
    void fromString() {
        Subtask subtaskFromString = new Subtask("3,SUBTASK,Test addNewTask,DONE,description,2");
        Subtask subtask = new Subtask("Test addNewTask", "description", 3, Status.DONE, 2);
        assertEquals(subtask, subtaskFromString);
    }

    @Test
    void shouldSaveAndReload() {
        Task task1 = new Task("Tes1", "Test addNewTask description", Status.NEW, 1);
        Task task2 = new Task("Tes2", "Test addNewTask description", Status.NEW, 2);
        Task task3 = new Task("Tes3", "Test addNewTask description", Status.NEW, 3);
        manager.addTask(task1);
        manager.addTask(task2);
        manager.addTask(task3);
        FileBackedTaskManager loadedManager = FileBackedTaskManager.loadFromFile(file);
        assertArrayEquals(manager.getTasks().toArray(), loadedManager.getTasks().toArray());
    }

}