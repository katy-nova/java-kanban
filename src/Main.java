import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.taskmanager.FileBackedTaskManager;
import java.io.File;
import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        File file = new File("src" + File.separator + "base" + File.separator + "base.csv");
        Task task = new Task("Test addNewTask", "Test addNewTask description", Status.NEW);
        System.out.println(task.toFileString());
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
        Task task1 = new Task("1,TASK,Test addNewTask,IN_PROGRESS,Test addNewTask description");
        Epic epic = new Epic("epic", "desc", Status.NEW, 2);
        Subtask subtask = new Subtask("3,SUBTASK,Test addNewTask,DONE,Test addNewTask description,2");
        manager.addTask(task1);
        manager.addEpic(epic);
        manager.addSubtask(subtask);
    }
}
