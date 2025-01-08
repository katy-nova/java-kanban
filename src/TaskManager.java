import java.util.ArrayList;
import java.util.HashMap;

public class TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private static int count = 0;

    private int addID() {
        count++;
        return count;
    }

    public void addTask(Task task) {
        if (Task.check(task)) {
            task.setID(addID());
            tasks.put(task.getID(), task);
        }
    }


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


    public void addEpic(Epic epic) {
        if (Epic.check(epic)) {
            epic.setID(addID());
            epics.put(epic.getID(), epic);
        }
    }

    public void updateTask(Task task) { //имея одинаковый ID старая задача заменится новой
        if (tasks.containsKey(task.getID())) {
            tasks.put(task.getID(), task);
        }
    }

    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getID()) && epics.containsKey(subtask.getEpicId())) {
            subtasks.put(subtask.getID(), subtask);//в эпике ничего перезаписывать не надо, тк ID не изменился
            setEpicStatus(epics.get(subtask.getEpicId()));
        }
    }

    public void updateEpic(Epic newEpic) {
        if (epics.containsKey(newEpic.getID())) {
            Epic epic = epics.get(newEpic.getID());
            epic.setName(newEpic.getName());
            epic.setDescription(newEpic.getDescription());
        }
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
                epic.setStatus(Status.NEW);
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

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Task getTask(int ID) {
        return tasks.get(ID);
    }

    public Subtask getSubtask(int ID) {
        return subtasks.get(ID);
    }

    public Epic getEpic(int ID) {
        return epics.get(ID);
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
            setEpicStatus(epic);
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

    private void setEpicStatus(Epic epic) {
        boolean isNew = true;
        boolean isDone = true;
        int epicId = epic.getID();
        ArrayList<Subtask> epicSubtasks = new ArrayList<>(); // создаем пустой список для задач нужного эпика
        for (Subtask subtask: subtasks.values()) {
            if (subtask.getEpicId() == epicId) {
                epicSubtasks.add(subtask); //добвляем все подзадачи по ID из мапы с подзадачами
            }
        }
        for (Subtask epicSubtask: epicSubtasks) {
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
