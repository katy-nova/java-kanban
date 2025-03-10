import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.taskmanager.InMemoryTaskManager;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    protected InMemoryTaskManager getTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void shouldSetCorrectStatus() {
        assertEquals(epic.getStatus(), Status.DONE);
        subtask1.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask1);
        assertEquals(epic.getStatus(), Status.IN_PROGRESS);
        Task[] array = {subtask1, subtask2, task};
        assertArrayEquals(array, taskManager.getPrioritizedTasks().toArray());
        subtask2.setStatus(Status.NEW);
        taskManager.updateSubtask(subtask2);
        assertEquals(epic.getStatus(), Status.NEW);
    }

}