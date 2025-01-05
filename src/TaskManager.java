import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    //??? зачем нужна отдельная мапа с подзадачами, если все подзадачи хранятся в листах в эпике????
    private final HashMap<Integer, Epic> epics = new HashMap<>();

    public void addTask(Task task) {
        if (Task.check(task)) {
            tasks.put(task.getID(), task);
        }
    }

    public void addSubtask(Subtask subtask) {
        if (Subtask.check(subtask)) {
            subtasks.put(subtask.getID(), subtask);
            Epic epic = epics.get(subtask.getEpicId());
            //??? по идее подзадачу невозможно создать отдельно, значит эпик уже обязан существовать в мапе эпиков???
            epic.addSubtask(subtask);
        }
    }

    public void addEpic(Epic epic) { //??? эпик по умолчанию добавляется пустой????
        if (Epic.check(epic)) {
            epics.put(epic.getID(), epic);
        }
    }

    public void renovateTask(Task task) { //имея одинаковый ID старая задача заменится новой
        addTask(task);
    }

    public void renovateSubtask(Subtask subtask) {
        Epic epic = epics.get(subtask.getEpicId());
        epic.deleteSubtask(subtask.getID()); // удаляем старую задачу из эпика, чтобы добавить обновленную
        addSubtask(subtask);
    }

    public void renovateEpic(Epic epic) {
        addEpic(epic);
    }

    public void clearTasks() {
        if (!tasks.isEmpty()) {
            tasks.clear();
        }
    }

    public void clearSubtasks() {
        if (!epics.isEmpty()) {
            for (Epic epic : epics.values()) { // очищаем каждый эпик
                epic.clearEpic();
            }
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear(); // очищаем мапу
        }
    }

    public void clearEpics() {
        if (!epics.isEmpty()) {
            epics.clear();
        }
        if (!subtasks.isEmpty()) {
            subtasks.clear(); // удаляем все подзадачи
        }
    }

    public String printAllTasks() {
        return tasks.toString();
    }

    public String printAllSubtasks() {
        return subtasks.toString();
    }

    public String printAllEpics() {
        return epics.toString();
    }

    public Task getTask(int ID) {
        return tasks.getOrDefault(ID, null);
    }

    public Subtask getSubtask(int ID) {
        return subtasks.getOrDefault(ID, null);
    }

    public Epic getEpic(int ID) {
        return epics.getOrDefault(ID, null);
    }

    public void deleteTask(int ID) {
        tasks.remove(ID);
    }

    public void deleteSubtask(int ID) {
        if (subtasks.containsKey(ID)) {
            Subtask subtask = subtasks.get(ID); // достаем подзадачу, чтобы получить ID эпика
            Epic epic = epics.get(subtask.getEpicId());
            subtasks.remove(ID);
            epic.deleteSubtask(ID); // удаляем подзадачу из эпика
        }
    }

    public void deleteEpic(int ID) {
        if (epics.containsKey(ID)) {
            for (Subtask subtask: subtasks.values()) {
                if (subtask.getEpicId() == ID) {
                    subtasks.remove(subtask.getID());
                }
            }
            epics.remove(ID);
        }

    }


}
