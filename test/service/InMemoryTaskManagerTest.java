
import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;
import service.Managers;
import service.TaskManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {

    TaskManager taskManager = Managers.getDefault();

    @Test
    void shouldDeleteSubtask() {
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("name1", "description", epic.getID());
        Subtask subtask2 = new Subtask("name2", "description", epic.getID());
        Subtask subtask3 = new Subtask("name3", "description", epic.getID());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(3, taskManager.getSubtasks().size());
        taskManager.deleteSubtask(subtask2.getID());
        assertEquals(2, taskManager.getSubtasks().size());
        assertEquals(2, epic.getSubtasks().size());
        taskManager.deleteEpic(epic.getID());
        assertTrue(taskManager.getEpics().isEmpty());
    }

    @Test
    void shouldGetCorrectHistory() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        taskManager.addTask(task);
        Epic epic = new Epic("name", "description");
        taskManager.addEpic(epic);
        Subtask subtask1 = new Subtask("name1", "description", epic.getID());
        Subtask subtask2 = new Subtask("name2", "description", epic.getID());
        Subtask subtask3 = new Subtask("name3", "description", epic.getID());
        taskManager.addSubtask(subtask1);
        taskManager.addSubtask(subtask2);
        taskManager.addSubtask(subtask3);
        taskManager.getTask(task.getID());
        taskManager.getSubtask(subtask1.getID());
        taskManager.getSubtask(subtask2.getID());
        taskManager.getSubtask(subtask3.getID());
        System.out.println(taskManager.getHistory());
        taskManager.getSubtask(subtask2.getID());
        System.out.println(taskManager.getHistory());
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        Task copy = task.clone();

        assertNotNull(copy, "Задача не найдена.");
        assertEquals(task, copy, "Задачи не совпадают.");

        taskManager.addTask(task);
        final List<Task> tasks = taskManager.getTasks();
        // правильно ли проверять одним тестом несколько пунктов из ТЗ или это нужно разбивать на разные тесты?
        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewSubtask() {
        Subtask subtask = new Subtask("name", "description", 2);
        Subtask copy = subtask.clone();

        assertNotNull(copy, "Задача не найдена.");
        assertEquals(subtask, copy, "Задачи не совпадают.");

        taskManager.addSubtask(subtask);
        final List<Subtask> tasks = taskManager.getSubtasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(subtask, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void addNewEpic() {
        Epic epic = new Epic("name", "description");
        Epic copy = epic.clone();

        assertNotNull(copy, "Задача не найдена.");
        assertEquals(epic, copy, "Задачи не совпадают.");

        taskManager.addEpic(epic);
        final List<Epic> epics = taskManager.getEpics();

        assertNotNull(epics, "Задачи не возвращаются.");
        assertEquals(1, epics.size(), "Неверное количество задач.");
        assertEquals(epic, epics.get(0), "Задачи не совпадают.");
    }

    @Test
    void checkManagersGeneration() {
        assertNotNull(taskManager);
        // нужно ли это проверять отдельным методом, ведь все тесты используют этот объект, тем самым проверяя его?
    }

    @Test
    void cannotMakeSubtaskAsEpic() {
        /* Совсем не понимаю принцип этого теста, ведь в методе добавления подзадачи мы достаем эпик из мапы эпиков,
        если такого эпика не будет существовать, то подзадача туда не положится. Единственное, что мне приходит в голову
        - это сравнить чтобы у подзадачи не совпадали поля id  и epicID*/
    }

    @Test
    void checkCompatibilityOfTasks() {
        /* Этот тест рушит всю мою логику работы с таск менеджером. Ведь при добавлении задачи, на вход приходит
        задача без id и менеджер сам его присваивает, чтобы он был уникален. Если есть возможность добавлять задачи
        с уже указанным id, то тогда нужно проверять, что такого id не существует во всех трех мапах? Что делать если
        он будет не уникален? Присвоить задаче новый id или перезаписать? В чем тогда заключается отсутствие
        конфликтности?*/
        Task task1 = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        Task task2 = new Task("Task", "Description", Status.DONE, 2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        final List<Task> tasks = taskManager.getTasks();
        assertEquals(tasks.size(), 2);
    }
}