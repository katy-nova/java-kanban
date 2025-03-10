import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import service.taskmanager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected abstract T getTaskManager();

    protected TaskManager taskManager;
    protected Epic epic;
    protected Subtask subtask1;
    protected Subtask subtask2;
    protected Task task;

    @BeforeEach
    void setUp() {
        taskManager = getTaskManager();
        epic = new Epic("epic", "desc");
        taskManager.addEpic(epic);
        subtask1 = new Subtask("Test addNewTask", "description", Status.DONE, epic.getID(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 3, 2, 14, 20));
        subtask2 = new Subtask("Test addNewTask", "description", 4, Status.DONE, epic.getID(),
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 2, 18, 40));
        task = new Task("Tes1", "Test addNewTask description", Status.NEW,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 5, 23, 45));
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addTask(task);
    }

    @Test
    void addTaskWithoutTime() {
        Task taskWithoutTime = new Task("name", "desc");
        taskManager.addTask(taskWithoutTime);
        assertEquals(2, taskManager.getTasks().size(), "Неверное количество задач.");
    }

    @Test
    void getTasks() {
        assertNotNull(taskManager.getTasks(), "Задачи не возвращаются.");
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        assertEquals(task, taskManager.getTasks().getFirst(), "Задачи не совпадают.");
    }

    @Test
    void getSubtasks() {
        assertNotNull(taskManager.getSubtasks(), "Задачи не возвращаются.");
        assertEquals(2, taskManager.getSubtasks().size(), "Неверное количество задач.");
        Subtask[] subtasks = {subtask1, subtask2};
        Integer[] subtaskIDs = {subtask1.getID(), subtask2.getID()};
        assertArrayEquals(subtasks, taskManager.getSubtasks().toArray());
        assertTrue(taskManager.getEpic(epic.getID()).isPresent());
        assertArrayEquals(subtaskIDs, taskManager.getEpic(epic.getID()).get().getSubtasks().toArray());
        // проверяем, что задачи добавились и в мапу, и в эпик
    }

    @Test
    void getEpics() {
        assertNotNull(taskManager.getEpics(), "Задачи не возвращаются.");
        assertEquals(1, taskManager.getEpics().size(), "Неверное количество задач.");
        assertEquals(epic, taskManager.getEpics().getFirst(), "Задачи не совпадают.");
    }

    @Test
    void clearAll() {
        taskManager.clearTasks();
        taskManager.clearSubtasks();
        taskManager.clearEpics();
        assertEquals(0, taskManager.getTasks().size());
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void shouldDelete() {
        taskManager.deleteSubtask(subtask2.getID());
        assertEquals(1, taskManager.getSubtasks().size());
        assertTrue(taskManager.getEpic(epic.getID()).isPresent());
        assertEquals(1, taskManager.getEpic(epic.getID()).get().getSubtasks().size());
        taskManager.deleteTask(task.getID());
        assertEquals(0, taskManager.getTasks().size());
        taskManager.deleteEpic(epic.getID());
        assertEquals(0, taskManager.getSubtasks().size());
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void shouldGetById() {
        assertTrue(taskManager.getTask(task.getID()).isPresent());
        assertEquals(task, taskManager.getTask(task.getID()).get());
        assertTrue(taskManager.getSubtask(subtask2.getID()).isPresent());
        assertEquals(subtask2, taskManager.getSubtask(subtask2.getID()).get());
        assertTrue(taskManager.getEpic(epic.getID()).isPresent());
        assertEquals(epic, taskManager.getEpic(epic.getID()).get());
    }

    @Test
    void shouldCalculateCorrectTime() {
        assertEquals(90, taskManager.getEpics().getFirst().getDuration().toMinutes());
        Subtask subtask3 = new Subtask("Test addNewTask", "description", Status.DONE, epic.getID(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 3, 2, 12, 10));
        taskManager.addSubtask(subtask3);
        assertEquals(120, taskManager.getEpics().getFirst().getDuration().toMinutes());
        Subtask subtask4 = new Subtask("Test addNewTask", "description", Status.DONE, epic.getID(),
                Duration.ofMinutes(45), LocalDateTime.of(2025, 3, 2, 16, 30));
        taskManager.addSubtask(subtask4);
        assertEquals(165, taskManager.getEpics().getFirst().getDuration().toMinutes());
    }

    @Test
    void shouldGetCorrectEndTime() {
        assertEquals(task.getEndTime(), LocalDateTime.of(2025, 3, 6, 0, 45));
        assertEquals(subtask1.getEndTime(), LocalDateTime.of(2025, 3, 2, 14, 50));
        assertEquals(subtask2.getEndTime(), LocalDateTime.of(2025, 3, 2, 19, 40));
    }

    @Test
    void validationChecker() {
        assertEquals(3, taskManager.getPrioritizedTasks().size());
        Subtask subtask3 = new Subtask("Test addNewTask", "description", Status.DONE, epic.getID(),
                Duration.ofMinutes(30), LocalDateTime.of(2025, 3, 2, 12, 10));
        taskManager.addSubtask(subtask3);
        assertEquals(4, taskManager.getPrioritizedTasks().size());
        Task[] prioritizedArray = {subtask3, subtask1, subtask2, task};
        assertArrayEquals(prioritizedArray, taskManager.getPrioritizedTasks().toArray());
        Subtask subtask4 = new Subtask("Test addNewTask", "description", Status.DONE, epic.getID(),
                Duration.ofMinutes(45), LocalDateTime.of(2025, 3, 2, 14, 30));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            taskManager.addTask(subtask4); // эта задача перекрывается и не должна быть добавлена
        });
        assertEquals("Задача составлена некорректно", exception.getMessage());
        assertEquals(4, taskManager.getPrioritizedTasks().size());
    }

    @Test
    void shouldThrowExceptions() {
        Task incorrectTask = new Task("", "desc");
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () ->
                taskManager.addTask(incorrectTask));
        assertEquals("Задача составлена некорректно", exception.getMessage());
        Subtask empty = null;
        exception = assertThrows(IllegalArgumentException.class, () ->
                taskManager.addSubtask(empty));
        assertEquals("Задача составлена некорректно", exception.getMessage());
        Task withNoDuration = task.clone();
        withNoDuration.setDuration(null);
        exception = assertThrows(IllegalArgumentException.class, () -> taskManager.updateTask(withNoDuration));
        assertEquals("Задача составлена некорректно", exception.getMessage());
    }

    @Test
    void shouldGetCorrectHistory() {
        assertTrue(taskManager.getTask(task.getID()).isPresent());
        taskManager.getTask(task.getID()).get();
        assertTrue(taskManager.getSubtask(subtask1.getID()).isPresent());
        taskManager.getSubtask(subtask1.getID()).get();
        Task updatedTask = task.clone(); // делаем копию, чтобы был старый айди
        updatedTask.setName("New Name");
        taskManager.updateTask(updatedTask);
        assertEquals(1, taskManager.getTasks().size(), "Неверное количество задач.");
        taskManager.getTask(task.getID());
        assertEquals(2, taskManager.getHistory().size());
        Task[] array1 = {subtask1, updatedTask};
        assertArrayEquals(array1, taskManager.getHistory().toArray());
        taskManager.deleteSubtask(subtask1.getID()); // удалили задачу
        Task[] array2 = {updatedTask};
        assertArrayEquals(array2, taskManager.getHistory().toArray());
        taskManager.clearTasks();
        Task[] emptyArray = {};
        assertArrayEquals(emptyArray, taskManager.getHistory().toArray());
    }
}