import model.Status;
import model.Task;
import org.junit.jupiter.api.Test;
import service.taskmanager.TimeTable;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.YearMonth;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

class TimeTableTest {
    TimeTable timeTable = new TimeTable(2025);

    @Test
    void shouldAddDayInTimeTable() {
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 13, 30));
        timeTable.addTaskToTable(task);
        Map<YearMonth, Map<Integer, Map<LocalTime, Integer>>> map = timeTable.getTimeTable();
        assertEquals(map.get(YearMonth.of(2025, 3)).get(31)
                .get(LocalTime.of(13, 30)), task.getID());
    }

    @Test
    void addTaskToTable() {
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 13, 30));
        timeTable.addTaskToTable(task);
        assertEquals(4, timeTable.getNotEmptyDayTasks(3, 31).size());
        Task task1 = new Task("Tes1", "Test addNewTask description", Status.NEW, 2,
                Duration.ofMinutes(130), LocalDateTime.of(2025, 3, 31, 17, 40));
        timeTable.addTaskToTable(task1);
        assertEquals(14, timeTable.getNotEmptyDayTasks(3, 31).size());
        Task incorrectTask1 = new Task("Tes1", "Test addNewTask description", Status.NEW, 3,
                Duration.ofMinutes(30), LocalDateTime.of(2025, 3, 31, 18, 0));
        Task incorrectTask2 = new Task("Tes1", "Test addNewTask description", Status.NEW, 4,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 16, 50));
        assertFalse(timeTable.addTaskToTable(incorrectTask1));
        assertFalse(timeTable.addTaskToTable(incorrectTask2));
        assertEquals(14, timeTable.getNotEmptyDayTasks(3, 31).size());
    }

    @Test
    void addDifficultTask() {
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 23, 45));
        timeTable.addTaskToTable(task);
        assertEquals(1, timeTable.getNotEmptyDayTasks(3, 31).size());
        assertEquals(3, timeTable.getNotEmptyDayTasks(4, 1).size());
    }

    @Test
    void deleteTaskFromTable() {
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 13, 30));
        timeTable.addTaskToTable(task);
        Task task1 = new Task("Tes1", "Test addNewTask description", Status.NEW, 2,
                Duration.ofMinutes(130), LocalDateTime.of(2025, 3, 31, 17, 40));
        timeTable.addTaskToTable(task1);
        assertEquals(14, timeTable.getNotEmptyDayTasks(3, 31).size());
        timeTable.deleteTaskFromTable(task1);
        assertEquals(4, timeTable.getNotEmptyDayTasks(3, 31).size());
        timeTable.deleteTaskFromTable(task);
        assertTrue(timeTable.getNotEmptyDayTasks(3, 31).isEmpty());
    }

    @Test
    void deleteDifficultTask() {
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 23, 45));
        timeTable.addTaskToTable(task);
        assertEquals(1, timeTable.getNotEmptyDayTasks(3, 31).size());
        assertEquals(3, timeTable.getNotEmptyDayTasks(4, 1).size());
        timeTable.deleteTaskFromTable(task);
        assertTrue(timeTable.getNotEmptyDayTasks(3, 31).isEmpty());
        assertTrue(timeTable.getNotEmptyDayTasks(4, 1).isEmpty());
    }

    @Test
    void shouldThrowException() {
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 13, 30));
        timeTable.addTaskToTable(task);
        Task notAddedTask = new Task("Tes1", "Test addNewTask description", Status.NEW, 2,
                Duration.ofMinutes(130), LocalDateTime.of(2025, 3, 21, 17, 40));
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> timeTable.deleteTaskFromTable(notAddedTask));
        assertEquals("Задача не была добавлена в расписание", exception.getMessage());
    }

    @Test
    void shouldReturnEmptyDay() {
        // пока в этот день не завели задачу - он будет пустой, чтобы не занимать лишнюю память
        assertTrue(timeTable.getDayTasks(3, 31).isEmpty());
        Task task = new Task("Tes1", "Test addNewTask description", Status.NEW, 1,
                Duration.ofMinutes(60), LocalDateTime.of(2025, 3, 31, 23, 45));
        timeTable.addTaskToTable(task);
        assertFalse(timeTable.getDayTasks(3, 31).isEmpty());
    }

}