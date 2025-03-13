package service.taskmanager;

import model.Task;

import java.time.*;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.IntStream;

public class TimeTable {
    private final Map<YearMonth, Map<Integer, Map<LocalTime, Integer>>> timeTable = new HashMap<>(12);
    // храним в мапе ID задачи, которая занимает этот временной отрезок
    private final int year;
    private static final int INTERVALS_IN_DAY = 96;

    public TimeTable(int year) {
        this.year = year;
        for (int i = 1; i <= 12; i++) {
            Month month = Month.of(i);
            int days = month.length(Year.isLeap(year));
            YearMonth yearMonth = YearMonth.of(year, month);
            timeTable.put(yearMonth, createDays(days));
        }
    }

    public Map<YearMonth, Map<Integer, Map<LocalTime, Integer>>> getTimeTable() {
        return new TreeMap<>(timeTable); // копия мапы для тестов
    }

    public void deleteTaskFromTable(Task task) {
        LocalTime startTime = roundDownTo15Minutes(task.getStartTime()).toLocalTime(); // округлили до 15 минут в меньшую сторону
        LocalDate startDay = task.getStartTime().toLocalDate();
        LocalTime endTime = roundUpTo15Minutes(task.getEndTime()).toLocalTime(); // округлили до 15 минут в большую сторону
        YearMonth month = YearMonth.of(year, startDay.getMonth()); // достали месяц
        int day = task.getStartTime().getDayOfMonth(); // достали день
        if (day != task.getEndTime().getDayOfMonth() && !isMidnight(endTime)) { // если начало и конец в разных днях
            Task part1 = task.clone();
            part1.setDuration(Duration.between(startTime, LocalDateTime.of(year, startDay.getMonthValue(),
                    day, 23, 59)));
            deleteTaskFromTable(part1);
            Task part2 = task.clone();
            part2.setStartTime(LocalDateTime.of(year, startDay.plusDays(1).getMonthValue(),
                    startDay.plusDays(1).getDayOfMonth(), 0, 0));
            part2.setDuration(Duration.between(part2.getStartTime(), task.getEndTime()));
            deleteTaskFromTable(part2);
            return;
        }
        Map<LocalTime, Integer> thisDay = timeTable.get(month).get(day);
        if (thisDay.isEmpty()) {
            throw new IllegalArgumentException("Задача не была добавлена в расписание");
        }
        int numberOfIntervals = Math.toIntExact((Duration.between(startTime, endTime).toMinutes() / 15));
        if (numberOfIntervals < 0) {
            numberOfIntervals += INTERVALS_IN_DAY;
        }
        for (int i = 0; i < numberOfIntervals; i++) { // проверка на наличие
            if (thisDay.get(startTime.plusMinutes(i * 15L)) != task.getID()) {
                throw new IllegalArgumentException("Задача не была добавлена в расписание");
            }
        }
        for (int j = 0; j < numberOfIntervals; j++) {
            thisDay.put(startTime.plusMinutes(j * 15L), 0);
        }
    }

    public boolean addTaskToTable(Task task) {
        LocalTime startTime = roundDownTo15Minutes(task.getStartTime()).toLocalTime(); // округлили до 15 минут в меньшую сторону
        LocalDate startDay = task.getStartTime().toLocalDate();
        LocalTime endTime = roundUpTo15Minutes(task.getEndTime()).toLocalTime(); // округлили до 15 минут в большую сторону
        YearMonth month = YearMonth.of(year, startDay.getMonth()); // достали месяц
        int day = task.getStartTime().getDayOfMonth(); // достали день
        if (day != task.getEndTime().getDayOfMonth() && !isMidnight(endTime)) { // если начало и конец в разных днях
            Task part1 = task.clone();
            part1.setDuration(Duration.between(startTime, LocalDateTime.of(year, startDay.getMonthValue(),
                    day, 23, 59)));
            addTaskToTable(part1);
            Task part2 = task.clone();
            part2.setStartTime(LocalDateTime.of(year, startDay.plusDays(1).getMonthValue(),
                    startDay.plusDays(1).getDayOfMonth(), 0, 0));
            part2.setDuration(Duration.between(part2.getStartTime(), task.getEndTime()));
            addTaskToTable(part2);
            return true;
        }
        Map<LocalTime, Integer> thisDay = timeTable.get(month).get(day);
        if (thisDay.isEmpty()) {
            thisDay = createDay(month.getMonthValue(), day);
        }
        // проверяем временные интервалы
        int numberOfIntervals = Math.toIntExact((Duration.between(startTime, endTime).toMinutes() / 15));
        if (numberOfIntervals < 0) {
            numberOfIntervals += INTERVALS_IN_DAY;
        }
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

    public TreeMap<LocalTime, Integer> getDayTasks(int month, int day) {
        return new TreeMap<>(timeTable.get(YearMonth.of(year, month)).get(day));
    }

    public TreeMap<LocalTime, Integer> getNotEmptyDayTasks(int month, int day) {
        TreeMap<LocalTime, Integer> tasks = new TreeMap<>();
        timeTable.get(YearMonth.of(year, month)).get(day).entrySet().stream()
                .filter(entry -> entry.getValue() != 0)
                .forEach(entry -> tasks.put(entry.getKey(), entry.getValue()));
        return tasks;
    }

    private boolean isMidnight(LocalTime time) {
        return time.getMinute() == 0 && time.getHour() == 0;
    }

    Map<Integer, Map<LocalTime, Integer>> getMonth(YearMonth month) {
        return timeTable.get(month);
    }

    private Map<LocalTime, Integer> createDay(Integer month, Integer day) {
        // будем заполнять день временными интервалами только когда в него будет добавляться первая залача
        Map<LocalTime, Integer> dayOfMonth = new HashMap<>();
        LocalTime startTime = LocalTime.of(0, 0);
        for (int i = 0; i < INTERVALS_IN_DAY; i++) {
            dayOfMonth.put(startTime, 0);
            startTime = startTime.plusMinutes(15);
        }
        getMonth(YearMonth.of(year, month)).put(day, dayOfMonth); // добавили день в мапу
        return dayOfMonth;
    }

    private Map<Integer, Map<LocalTime, Integer>> createDays(int days) {
        Map<Integer, Map<LocalTime, Integer>> month = new HashMap<>();
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
