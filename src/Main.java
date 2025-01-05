/*
   Никита, здравствуйте! С наступившим новым годом вас! Плохо поняла смысл задания, совершенно не понимаю смысла
   создания отдельной мапы с подзадачами, ведь они и так хранятся в эпике. Также не очень понимаю количество необходимых
   проверок при добавлении подзадач,считается ли, что эпик обязательно будет существовать или это надо как-то
    дополнительно проверять. Комментарии, где я не уверена в реалтзации отметила с двух строн знаками вопроса. Код в
    мейне не удаляю, чтобы потом при исправлении быыло удобнее тестировать.
    С уважением, Екатерина
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();
        Epic epic = new Epic("epic");
        Subtask new1 = new Subtask("first", epic.getID());
        Subtask new2 = new Subtask("first", epic.getID());
        Subtask done1 = new Subtask("second", Status.DONE, epic.getID());
        Subtask pr1 = new Subtask("second", Status.IN_PROGRESS, epic.getID());
        Subtask empty = new Subtask("", epic.getID());
        taskManager.addEpic(epic);
        taskManager.addSubtask(new1);
        taskManager.addSubtask(new1); // пробуем 2 одинаковые
        taskManager.addSubtask(empty); // пробуем добавить пустую
        taskManager.addSubtask(new2);
        System.out.println("все задачи новые" + taskManager.printAllEpics()); // печатаем эпик и проверяем статус
        System.out.println();
        System.out.println(taskManager.printAllSubtasks());
        System.out.println();
        taskManager.deleteSubtask(new2.getID()); // удаляем
        System.out.println("удалили одну" + taskManager.printAllEpics());
        System.out.println();
        System.out.println(taskManager.printAllSubtasks());
        System.out.println();
        taskManager.addSubtask(done1); // добавили сделанную
        System.out.println("проверяем статус" + taskManager.printAllEpics());
        System.out.println();
        System.out.println(taskManager.printAllSubtasks());
        System.out.println();
        taskManager.deleteSubtask(new2.getID()); // удаляем несуществующую
        taskManager.deleteSubtask(new1.getID()); // удаляем еще одну новую
        System.out.println("проверяем статус" + taskManager.printAllEpics());
        System.out.println();
        System.out.println(taskManager.printAllSubtasks());
        System.out.println();
        taskManager.addSubtask(pr1);
        System.out.println("проверяем статус" + taskManager.printAllEpics());
        System.out.println();
        System.out.println(taskManager.printAllSubtasks());
        System.out.println();
        Subtask copy = new Subtask("new name","desc", 7, Status.DONE, epic.getID());
        // создаем обновленную подзадачу с таким же ID
        taskManager.renovateSubtask(copy); // обновляем задачу
        System.out.println("проверяем статус" + taskManager.printAllEpics());
        System.out.println();
        //taskManager.clearSubtasks();
        taskManager.clearEpics();
        System.out.println(taskManager.printAllSubtasks());
        System.out.println(taskManager.printAllEpics());

    }
}
