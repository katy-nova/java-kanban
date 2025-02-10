package service;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    //private final LinkedHashMap<Integer,Task> history = new LinkedHashMap<>();
    private final HashMap<Integer, Node> handMadeLinkedMap = new HashMap<>();

    private Node head;   // первый узел списка
    private Node tail;   // последний узел списка

    @Override
    public Node getNode(Task task) { // вспомогательный метод
        return handMadeLinkedMap.get(task.getID());
    }

    public Node getNode(int ID) { // вспомогательный метод
        return handMadeLinkedMap.get(ID);
    }

    // Метод для добавления элемента в конец списка
    @Override
    public void linkLast(Task task) {
        Node newNode = new Node(null, task.clone(), null); // Создаем новый узел

        if (head == null) {
            head = newNode;   // новый узел становится и головой, и хвостом
            tail = newNode;
        } else {
            tail.next = newNode;  // новый узел становится следующим после текущего хвоста
            newNode.prev = tail;  // предыдущим для нового узла становится текущий хвост
            tail = newNode;       // новый узел становится новым хвостом
        }
        handMadeLinkedMap.put(task.getID(), newNode); // кладем в мапу готовый узел
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node currentNode = head; // голова становится текущим узлом
        while (currentNode != null) {
            tasks.add(currentNode.task); // добавляем текущий узел в список
            currentNode = currentNode.next; // следующий узел от текущего становится текущим
        }
        return tasks;
    }

    @Override
    public void removeNode(Node node) {
        if (node == null) { // если пытаемся удалить задачу, которая не попадалав в историю
            return;
        }
        if (node.prev != null && node.next != null) { // если есть и предыдущий, и следующий
            Node previous = node.prev; // достаем предыдущий
            Node next = node.next; // достаем следующий
            previous.next = next; // связываем
            next.prev = previous;
        } else if (node.prev != null) { // если нет следующего
            node.prev.next = null; // предыдущий становится хвостом
            tail = node.prev;
        } else if (node.next != null) { // если нет предыдущего
            node.next.prev = null; // следующий становится головой
            head = node.next;
        } else { // если этот узел был единственный
            head = null;
            tail = null;
        }
        handMadeLinkedMap.remove(node.task.getID()); // безболезненно удаляем нужный узел
    }


    @Override
    public void remove(int id) {
        removeNode(getNode(id));
    }

//    @Override
//    public void addTask(Task task) {
//        if (Task.check(task)) {
//            history.remove(task.getID());
//            history.put(task.getID(),task.clone());
//        }
//    }

    @Override
    public void addTask(Task task) {
        if (Task.check(task)) {
            if (getNode(task) != null) {
                removeNode(getNode(task));
            }
            linkLast(task);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(getTasks());
    }
}
