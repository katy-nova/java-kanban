package service;

import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int count = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistoryManager();

    public int addID() {
        count++;
        return count;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getTasks();
    }

    @Override
    public void addTask(Task task) {
        if (Task.check(task)) {
            task.setID(addID());
            tasks.put(task.getID(), task);
        }
    }

    @Override
    public void addSubtask(Subtask subtask) {
        if (Subtask.check(subtask)) {
            subtask.setID(addID());
            subtasks.put(subtask.getID(), subtask);
            if (epics.containsKey(subtask.getEpicId())) {
                Epic epic = epics.get(subtask.getEpicId());
                epic.addSubtask(subtask.getID());
                setEpicStatus(epic);
            }
        }
    }


    @Override
    public void addEpic(Epic epic) {
        if (Epic.check(epic)) {
            epic.setID(addID());
            epics.put(epic.getID(), epic);
        }
    }

    @Override
    public void updateTask(Task task) { //имея одинаковый id старая задача заменится новой
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getID(), subtask);//в эпике ничего перезаписывать не надо, тк id не изменился
            setEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    @Override
    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getID())) {
            Epic epic = epics.get(newEpic.getID());
            epic.setName(newEpic.getName());
            epic.setDescription(newEpic.getDescription());
        }
    }

    @Override
    public void clearTasks() {
        if (!tasks.isEmpty()) {
            for (Integer id : tasks.keySet()) {
                historyManager.remove(id); // не слишком ли усложняется метод с добавлением цикла?
            }
            tasks.clear();
        }
    }

    @Override
    public void clearSubtasks() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) { // очищаем каждый эпик
                historyManager.remove(epic.getID());
                epic.clearEpic();
                epic.setStatus(Status.NEW);
            }
        }
        if (!subtasks.isEmpty()) {
            for (Integer id : subtasks.keySet()) {
                historyManager.remove(id);
            }
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
    public Task getTask(int id) {
        historyManager.addTask(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Subtask getSubtask(int id) {
        historyManager.addTask(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Epic getEpic(int id) {
        historyManager.addTask(epics.get(id));
        return epics.get(id);
    }

    @Override
    public void deleteTask(int id) {
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
            historyManager.remove(id);
        }
    }

    @Override
    public void deleteEpic(int id) {
        if (epics.containsKey(id)) {
            ArrayList<Integer> subtasksIDs = epics.get(id).getSubtasks();
            for (Integer subtaskId : subtasksIDs) {
                subtasks.remove(subtaskId);
                historyManager.remove(subtaskId);
            }
            epics.remove(id);
            historyManager.remove(id);
        }

    }

    public void setEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;
        int epicId = epic.getID();
        ArrayList<Subtask> epicSubtasks = new ArrayList<>(); // создаем пустой список для задач нужного эпика
        for (Subtask subtask : subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                epicSubtasks.add(subtask); //добвляем все подзадачи по id из мапы с подзадачами
            }
        }
        for (Subtask epicSubtask : epicSubtasks) {
            if (epicSubtask.getStatus() == Status.IN_PROGRESS) { // если хотя бы у 1 задачи статус "в процессе"
                epic.setStatus(Status.IN_PROGRESS); // то весь эпик в статусе "в процессе"
                return;
            } else if (epicSubtask.getStatus() == Status.NEW) {
                isDone = false; //если хотя бы у 1 задачи статус новая, то эпик не может быть done
            } else if (epicSubtask.getStatus() == Status.DONE) {
                isNew = false; //если хотя бы у 1 задачи статус done, то эпик не может быть новым
            }
            if (!isNew && !isDone) {// если есть хотя бы по 1 задаче new и done, устанавливаем статус и выходим
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
}
