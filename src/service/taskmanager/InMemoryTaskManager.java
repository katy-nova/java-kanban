package service.taskmanager;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.Managers;
import service.history.HistoryManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int count = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();
    protected final TreeSet<Task> prioritisedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));

    public boolean hasNoOverlap(Task task1, Task task2) {
        if (task1.getStartTime().isBefore(task2.getStartTime())) { // узнаем какая задача начинается раньше
            return !(task1.getEndTime().isAfter(task2.getEndTime()));
            // используем отрицание потому что время конца предыдущей и начала следующей может быть равно
        } else {
            return !(task2.getEndTime().isBefore(task1.getEndTime()));
        }
    }

    private boolean validate(Task task) {
        if (!Task.check(task)) { // если задача не имеет основных полей - сразу не пропускаем
            return false;
        }
        if (prioritisedTasks.isEmpty() || task.getStartTime() == null && task.getDuration() == null) {
            return true; // если в списке нет задач, или это задача без времени - вернем true
        }
        if (prioritisedTasks.first().getStartTime().isAfter(task.getEndTime()) ||
                prioritisedTasks.last().getEndTime().isBefore(task.getStartTime())) {
            return true;
            // если задача заканчивается раньше самой первой или начинается позже самой последней, то сразу вернем true
        }
        return prioritisedTasks.stream()
                // убираем все задачи, которые заканчиваются раньше начала заданной
                .dropWhile(task1 -> !(task1.getEndTime().isAfter(task.getStartTime())))
                // проверяем, что оставшиеся задачи начинаются позже конца заданной
                .allMatch(task1 -> !(task1.getStartTime().isBefore(task.getEndTime())));
        // не заменяю на nonMatch потому что мне кажется, так легче понимать код
    }

    protected void addTaskToSet(Task task) {
        if (task.getStartTime() != null) {
            prioritisedTasks.add(task);
        }
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritisedTasks);
    }

    public int addID() {
        count++;
        return count;
    }

    public void setCount(int newCount) {
        count = newCount;
    }

    public int getCount() {
        return count;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getTasks();
    }

    @Override
    public void addTask(Task task) throws IllegalStateException {
        if (validate(task)) {
            task.setID(addID());
            tasks.put(task.getID(), task);
            addTaskToSet(task); // сразу при добавлении задачи добавляем ее во временнОе множество
        } else {
            throw new IllegalArgumentException("Задача составлена некорректно");
        }
    }

    @Override
    public void addSubtask(Subtask subtask) throws IllegalStateException {
        if (validate(subtask)) {
            subtask.setID(addID());
            subtasks.put(subtask.getID(), subtask);
            addTaskToSet(subtask);
            if (epics.containsKey(subtask.getEpicId())) {
                Epic epic = epics.get(subtask.getEpicId());
                epic.addSubtask(subtask.getID());
                setEpicStatus(epic);
                setEpicTime(epic);
            } else {
                throw new IllegalArgumentException("Неверно указан epicId");
            }
        } else {
            throw new IllegalArgumentException("Задача составлена некорректно");
        }
    }

    @Override
    public void addEpic(Epic epic) throws IllegalStateException {
        if (Epic.check(epic)) {
            epic.setID(addID());
            epics.put(epic.getID(), epic);
        } else {
            throw new IllegalArgumentException("Задача составлена некорректно");
        }
    }

    @Override
    public void updateTask(Task task) throws IllegalStateException { //имея одинаковый id старая задача заменится новой
        if (tasks.containsKey(task.getID()) && Task.check(task)) {
            tasks.put(task.getID(), task);
            addTaskToSet(task);
        } else {
            throw new IllegalArgumentException("Задача составлена некорректно");
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) throws IllegalStateException {
        if (subtasks.containsKey(subtask.getID()) && epics.containsKey(subtask.getEpicId()) && Task.check(subtask)) {
            subtasks.put(subtask.getID(), subtask);//в эпике ничего перезаписывать не надо, тк id не изменился
            addTaskToSet(subtask);
            setEpicStatus(epics.get(subtask.getEpicId()));
            setEpicTime(epics.get(subtask.getEpicId()));
        } else {
            throw new IllegalArgumentException("Задача составлена некорректно");
        }
    }

    @Override
    public void updateEpic(Epic newEpic) throws IllegalStateException {
        if (epics.containsKey(newEpic.getID())) {
            Epic epic = epics.get(newEpic.getID());
            epic.setName(newEpic.getName());
            epic.setDescription(newEpic.getDescription());
        } else {
            throw new IllegalArgumentException("Задача составлена некорректно");
        }
    }

    @Override
    public void clearTasks() {
        if (!tasks.isEmpty()) {
            for (Integer id : tasks.keySet()) {
                historyManager.remove(id);
            }
            prioritisedTasks.removeAll(tasks.values()); // удаляем все задачи из множества
            tasks.clear();
        }
    }

    @Override
    public void clearSubtasks() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) { // очищаем каждый эпик
                epic.clearEpic();
                epic.setStatus(Status.NEW);
            }
        }
        if (!subtasks.isEmpty()) {
            for (Integer id : subtasks.keySet()) {
                historyManager.remove(id);
            }
            prioritisedTasks.removeAll(subtasks.values());
            subtasks.clear(); // очищаем мапу
        }
    }

    @Override
    public void clearEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear(); // удаляем все подзадачи
            prioritisedTasks.addAll(subtasks.values());
        }
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Optional<Task> getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return Optional.of(tasks.get(id));
    }

    @Override
    public Optional<Subtask> getSubtask(int id) {
        historyManager.addTask(subtasks.get(id));
        return Optional.of(subtasks.get(id));
    }

    @Override
    public Optional<Epic> getEpic(int id) {
        historyManager.addTask(epics.get(id));
        return Optional.of(epics.get(id));
    }

    @Override
    public void deleteTask(int id) {
        prioritisedTasks.remove(tasks.get(id));
        tasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubtask(int id) {
        if (subtasks.containsKey(id)) {
            Subtask subtask = subtasks.get(id); // достаем подзадачу, чтобы получить id эпика
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(id);
            epic.deleteSubtask(id); // удаляем подзадачу из эпика
            setEpicStatus(epic);
            setEpicTime(epic);
            historyManager.remove(id);
            prioritisedTasks.remove(subtask);
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            epics.get(id).getSubtasks().stream()
                    .peek(subtaskId -> prioritisedTasks.remove(subtasks.get(subtaskId)))
                    .peek(historyManager::remove)
                    .forEach(subtasks::remove);

            epics.remove(id);
            historyManager.remove(id);
        }

    }

    public int findMaxID() { // метод, который находит максимальный айди, чтобы потом присвоить счетчику его значение
        Set<Integer> ids = new HashSet<>();
        ids.addAll(tasks.keySet());
        ids.addAll(subtasks.keySet());
        ids.addAll(epics.keySet());
        return Collections.max(ids);
    }

    private void setEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;
        List<Subtask> epicSubtasks = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getID())
                .toList();
        for (Subtask epicSubtask : epicSubtasks) {
            if (epicSubtask.getStatus() == Status.IN_PROGRESS) { // если хотя бы у 1 задачи статус "в процессе"
                epic.setStatus(Status.IN_PROGRESS); // то весь эпик в статусе "в процессе"
                return;
            } else if (epicSubtask.getStatus() == Status.NEW) {
                isDone = false; //если хотя бы у 1 задачи статус новая, то эпик не может быть done
            } else if (epicSubtask.getStatus() == Status.DONE) {
                isNew = false; //если хотя бы у 1 задачи статус done, то эпик не может быть новым
            }
            if (!isNew && !isDone) { // если есть хотя бы по 1 задаче new и done, устанавливаем статус и выходим
                epic.setStatus(Status.IN_PROGRESS); // дальше проверять нет смысла
                return;
            }
        }
        if (isNew) {
            epic.setStatus(Status.NEW);
        } else {
            epic.setStatus(Status.DONE);
        }
    }

    private void setEpicTime(Epic epic) {
        Optional<LocalDateTime> endTime = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getID())
                .map(Task::getEndTime)
                .sorted()
                .reduce((a, b) -> b); // достаем последний элемент
        endTime.ifPresent(epic::setEndTime);

        Optional<LocalDateTime> startTime = subtasks.values().stream()
                .filter(subtask -> subtask.getEpicId() == epic.getID())
                .map(Task::getStartTime)
                .sorted()
                .findFirst();
        startTime.ifPresent(epic::setStartTime);

        if (endTime.isPresent() && startTime.isPresent()) {
            Optional<Long> minutes = subtasks.values().stream()
                    .filter(subtask -> subtask.getEpicId() == epic.getID())
                    .map(subtask -> subtask.getDuration().toMinutes())
                    .reduce(Long::sum);
            minutes.ifPresent(minute -> epic.setDuration(Duration.ofMinutes(minute)));
        }
    }
}
