package service.taskmanager;

import model.*;

import java.io.*;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private final File file;

    public FileBackedTaskManager(File file) throws ManagerSaveException {
        this.file = file;
        load(file); // при создании объекта автоматически выполняем загрузку данных
    }

    private Task fromString(String taskString) throws ManagerSaveException {
        String[] data = taskString.split(",");
        try {
            TaskType taskType = TaskType.valueOf(data[1]);
            switch (taskType) {
                case TASK:
                    Task task = new Task(taskString);
                    tasks.put(task.getID(), task); // сразу записываем в менеджер
                    return task;
                case SUBTASK:
                    Subtask subtask = new Subtask(taskString);
                    // не знаю, насколько необходима эта проверка, ведь условия считаются идеальными
                    // но для профилактики исключений добавила
                    if (epics.get(subtask.getEpicId()) != null) {
                        subtasks.put(subtask.getID(), subtask);
                        Epic epicForSubtask = epics.get(subtask.getEpicId());
                        epicForSubtask.addSubtask(subtask.getID());
                    }
                    return subtask;
                case EPIC:
                    Epic epic = new Epic(taskString);
                    epics.put(epic.getID(), epic);
                    return epic;
                default:
                    return null;
            }
        } catch (IllegalArgumentException exception) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    private void load(File file) throws ManagerSaveException {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            if (br.ready()) {
                String line = br.readLine(); // читаем первую строку в которой нет задач
                if (line.isEmpty()) {
                    return; // если в файле одна пустая строка
                }
            } else {
                return; // если файл пустой, прерываем метод
            }
            while (br.ready()) {
                fromString(br.readLine());
            }
            setCount(findMaxID()); // устанавливаем счетчик на значение максимального айди
        } catch (FileNotFoundException e) {
            throw new ManagerSaveException("Файл не найден");
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }

    private void save() throws ManagerSaveException {
        try (Writer fileWriter = new FileWriter(file)) {
            fileWriter.write("id,type,name,status,description,epicId\n");
            for (Task task : getTasks()) {
                fileWriter.write(task.toFileString());
            }
            for (Epic epic : getEpics()) {
                fileWriter.write(epic.toFileString());
            }
            for (Subtask subtask : getSubtasks()) {
                fileWriter.write(subtask.toFileString());
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        return new FileBackedTaskManager(file);
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubtask(Subtask subtask) {
        super.addSubtask(subtask);
        save();
    }

    @Override
    public void addEpic(Epic epic) {
        super.addEpic(epic);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void updateEpic(Epic newEpic) {
        super.updateEpic(newEpic);
        save();
    }

    @Override
    public void clearTasks() {
        super.clearTasks();
        save();
    }

    @Override
    public void clearSubtasks() {
        super.clearSubtasks();
        save();
    }

    @Override
    public void clearEpics() {
        super.clearEpics();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }
}

