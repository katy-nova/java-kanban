package service.taskmanager;

import model.Task;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class TimeTable {
    public final Map<YearMonth, Map<Integer, Map<LocalDateTime, Integer>>> timeTable = new HashMap<>(12);
    // храним в мапе ID задачи, которая занимает этот временной отрезок
    private final int year;
    private static final int INTERVALS_IN_DAY = 96;
    // не добавляла это в основную реализацию менеджера, потому что мне кажется, что хоть искать пересечения таким
    // способом это все равно не совсем О1 (цикл по интервалам) да и вообще гораздо сложнее и требует много памяти.
    // Поскольку в 1 дне 96 интервалов, то продумала логику, что день заполняется интервалами только в тот момент,
    // когда в него кладут первую задачу, до это момента мапы дня пустые
    // но это все равно больше похоже на гусеницу Франкенштейна(
    // P.S. хотя бы работает...

    public TimeTable(int year) {
        this.year = year;
        for (int i = 1; i <= 12; i++) {
            Month month = Month.of(i);
            int days = month.length(Year.isLeap(year));
            YearMonth yearMonth = YearMonth.of(year, month);
            timeTable.put(yearMonth, createDays(days));
        }
    }

    public void deleteTaskFromTable(Task task) {
        LocalDateTime startTime = roundDownTo15Minutes(task.getStartTime()); // округлили до 15 минут в меньшую сторону
        LocalDateTime endTime = roundUpTo15Minutes(task.getEndTime()); // округлили до 15 минут в большую сторону
        YearMonth month = YearMonth.of(year, startTime.getMonth()); // достали месяц
        int day = startTime.getDayOfMonth(); // достали день
        if (day != endTime.getDayOfMonth() && !isMidnight(endTime)) { // если начало и конец в разных днях
            Task part1 = task.clone();
            part1.setDuration(Duration.between(startTime, LocalDateTime.of(year, startTime.getMonthValue(),
                    day, 23, 59)));
            deleteTaskFromTable(part1);
            Task part2 = task.clone();
            part2.setStartTime(LocalDateTime.of(year, startTime.plusDays(1).getMonthValue(),
                    startTime.plusDays(1).getDayOfMonth(), 0, 0));
            part2.setDuration(Duration.between(part2.getStartTime(), task.getEndTime()));
            deleteTaskFromTable(part2);
        }
        Map<LocalDateTime, Integer> thisDay = timeTable.get(month)
                .get(day);
        if (thisDay.isEmpty()) {
            throw new IllegalArgumentException("Задача не была добавлена в расписание");
        }
        int numberOfIntervals = Math.toIntExact((Duration.between(startTime, endTime).toMinutes() / 15));
        for (int j = 0; j < numberOfIntervals; j++) {
            thisDay.put(startTime.plusMinutes(j * 15L), 0);
        }
    }

    public boolean addTaskToTable(Task task) {
        LocalDateTime startTime = roundDownTo15Minutes(task.getStartTime()); // округлили до 15 минут в меньшую сторону
        LocalDateTime endTime = roundUpTo15Minutes(task.getEndTime()); // округлили до 15 минут в большую сторону
        YearMonth month = YearMonth.of(year, startTime.getMonth()); // достали месяц
        int day = startTime.getDayOfMonth(); // достали день
        if (day != endTime.getDayOfMonth() && !isMidnight(endTime)) { // если начало и конец в разных днях
            Task part1 = task.clone();
            part1.setDuration(Duration.between(startTime, LocalDateTime.of(year, startTime.getMonthValue(),
                    day, 23, 59)));
            addTaskToTable(part1);
            Task part2 = task.clone();
            part2.setStartTime(LocalDateTime.of(year, startTime.plusDays(1).getMonthValue(),
                    startTime.plusDays(1).getDayOfMonth(), 0, 0));
            part2.setDuration(Duration.between(part2.getStartTime(), task.getEndTime()));
            addTaskToTable(part2);
        }
        Map<LocalDateTime, Integer> thisDay = timeTable.get(month)
                .get(day);
        if (thisDay.isEmpty()) {
            thisDay = createDay(month.getMonthValue(), day);
        }
        // проверяем временные интервалы
        int numberOfIntervals = Math.toIntExact((Duration.between(startTime, endTime).toMinutes() / 15));
        for (int i = 0; i < numberOfIntervals; i++) {
            if (thisDay.get(startTime.plusMinutes(i * 15L)) != 0) {
                return false; // если хотя бы 1 занят - вернется false
            }
        }
        for (int j = 0; j < numberOfIntervals; j++) {
            thisDay.put(startTime.plusMinutes(j * 15L), task.getID());
        }
        return true;
    }

    public TreeMap<LocalDateTime, Integer> getDayTasks(int month, int day) {
        return new TreeMap<>(timeTable.get(YearMonth.of(year, month)).get(day));
    }

    public TreeMap<LocalDateTime, Integer> getNotEmptyDayTasks(int month, int day) {
        TreeMap<LocalDateTime, Integer> tasks = new TreeMap<>();
        timeTable.get(YearMonth.of(year, month)).get(day).entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .forEach(entry -> tasks.put(entry.getKey(), entry.getValue()));
        return tasks;
    }

    private boolean isMidnight(LocalDateTime time) {
        return time.getMinute() == 0 && time.getHour() == 0;
    }

    Map<Integer, Map<LocalDateTime, Integer>> getMonth(YearMonth month) {
        return timeTable.get(month);
    }

    private Map<LocalDateTime, Integer> createDay(Integer month, Integer day) {
        // будем заполнять день временными интервалами только когда в него будет добавляться первая залача
        Map<LocalDateTime, Integer> dayOfMonth = new HashMap<>();
        LocalDateTime startTime = LocalDateTime.of(year, month, day, 0, 0);
        for (int i = 0; i < INTERVALS_IN_DAY; i++) {
            dayOfMonth.put(startTime, 0);
            startTime = startTime.plusMinutes(15);
        }
        getMonth(YearMonth.of(year, month)).put(day, dayOfMonth); // добавили день в мапу
        return dayOfMonth;
    }

    private Map<Integer, Map<LocalDateTime, Integer>> createDays(int days) {
        Map<Integer, Map<LocalDateTime, Integer>> month = new HashMap<>();
        IntStream.rangeClosed(1, days)
                .forEach(day -> month.put(day, new HashMap<>()));
        return month;
        // заполняем каждый день пустыми мапами без временных интервалов
    }

    private static LocalDateTime roundDownTo15Minutes(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        long minutesToSubtract = minutes % 15;
        return dateTime.minusMinutes(minutesToSubtract);
    }

    private static LocalDateTime roundUpTo15Minutes(LocalDateTime dateTime) {
        int minutes = dateTime.getMinute();
        long minutesToAdd = (15 - (minutes % 15)) % 15;
        return dateTime.plusMinutes(minutesToAdd);
    }
}