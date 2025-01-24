import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void testClone() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW );
        Task clone = task.clone();
        task.renovateTask("Change", "Change", Status.DONE);
        assertNotEquals(task, clone);
    }
}