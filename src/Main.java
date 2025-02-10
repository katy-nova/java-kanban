import model.Epic;
import model.Status;
import model.Subtask;
import model.Task;
import service.InMemoryTaskManager;
import service.TaskManager;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new InMemoryTaskManager();
        Epic epic = new Epic("epic");
        taskManager.addEpic(epic);
        Subtask new1 = new Subtask("first", epic.getID()); //2
        Subtask new2 = new Subtask("first", epic.getID()); //3
        Subtask done1 = new Subtask("second", Status.DONE, epic.getID()); //4
        Subtask pr1 = new Subtask("second", Status.IN_PROGRESS, epic.getID()); //5
        Subtask empty = new Subtask("", epic.getID());
        Task task1 = new Task("1"); //6
        Task task2 = new Task("2"); //7
        Task task3 = new Task("3"); //8
        Task task4 = new Task("4"); //9
        taskManager.addSubtask(new1);
        taskManager.addSubtask(new1); // пробуем 2 одинаковые
        taskManager.addSubtask(empty); // пробуем добавить пустую
        taskManager.addSubtask(new2);

        ArrayList<Integer> test = new ArrayList<Integer>(10);
        test.add(0);
        test.add(1);
        test.add(2);
        test.add(3);
        test.add(4);
        setArray(5, test);
        setArray(6, test);
        setArray(7, test);
        setArray(8, test);
        setArray(9, test);
        System.out.println("length is out");
        setArray(10, test);
        setArray(11, test);


        /*System.out.println("NEW" + taskManager.getEpics()); // печатаем эпик и проверяем статус
        System.out.println();
        System.out.println(taskManager.getSubtasks());
        System.out.println();
        taskManager.deleteSubtask(new2.getID()); // удаляем
        System.out.println("удалили одну" + taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());
        System.out.println();
        taskManager.addSubtask(done1); // добавили сделанную
        System.out.println("IN PROGRESS" + taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());
        System.out.println();
        taskManager.deleteSubtask(new2.getID()); // удаляем несуществующую
        taskManager.deleteSubtask(new1.getID()); // удаляем еще одну новую
        System.out.println("DONE" + taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());
        System.out.println();
        taskManager.addSubtask(pr1);
        System.out.println("IN PROGRESS" + taskManager.getEpics());
        System.out.println();
        System.out.println(taskManager.getSubtasks());
        System.out.println();
        model.Subtask copy = new model.Subtask("new name","desc", 5, model.Status.DONE, epic.getID());
        // создаем обновленную подзадачу с таким же id
        taskManager.updateSubtask(copy); // обновляем задачу
        System.out.println("DONE" + taskManager.getEpics());
        System.out.println();
        //taskManager.clearSubtasks();
        model.Epic copyEpic = new model.Epic("update", "desc", model.Status.NEW, 1);
        taskManager.updateEpic(copyEpic);
        //taskManager.clearEpics();
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getEpics()); */

    }

    public static void setArray(Integer num, ArrayList<Integer> array) {
        if (array.size() >= 10) {
            array.remove(0);
        }
        array.add(num);
        System.out.println(array);
    }
}
