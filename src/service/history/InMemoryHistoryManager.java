package service.history;

import model.Task;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {

    private final HashMap<Integer, Node> handMadeLinkedMap = new HashMap<>();

    private Node head;   // первый узел списка
    private Node tail;   // последний узел списка


    private Node getNode(Task task) { // вспомогательный метод
        return handMadeLinkedMap.get(task.getID());
    }

    private Node getNode(int id) { // вспомогательный метод
        return handMadeLinkedMap.get(id);
    }

    // Метод для добавления элемента в конец списка
    private void linkLast(Task task) {
        Node newNode = new Node(null, task.clone(), null); // Создаем новый узел

        if (head == null) {
            head = newNode;   // новый узел становится и головой, и хвостом
            tail = newNode;
        } else {
            tail.setNext(newNode);  // новый узел становится следующим после текущего хвоста
            newNode.setPrev(tail); // предыдущим для нового узла становится текущий хвост
            tail = newNode;       // новый узел становится новым хвостом
        }
        handMadeLinkedMap.put(task.getID(), newNode); // кладем в мапу готовый узел
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasks = new ArrayList<>();
        Node currentNode = head; // голова становится текущим узлом
        while (currentNode != null) {
            tasks.add(currentNode.getTask()); // добавляем текущий узел в список
            currentNode = currentNode.getNext(); // следующий узел от текущего становится текущим
        }
        return tasks;
    }

    private void removeNode(Node node) {
        if (node == null) { // если пытаемся удалить задачу, которая не попадалав в историю
            return;
        }
        if (node.getPrev() != null && node.getNext() != null) { // если есть и предыдущий, и следующий
            Node previous = node.getPrev(); // достаем предыдущий
            Node next = node.getNext(); // достаем следующий
            previous.setNext(next); // связываем
            next.setPrev(previous);
        } else if (node.getPrev() != null) { // если нет следующего
            node.getPrev().setNext(null); // предыдущий становится хвостом
            tail = node.getPrev();
        } else if (node.getNext() != null) { // если нет предыдущего
            node.getPrev().setNext(null); // следующий становится головой
            head = node.getNext();
        } else { // если этот узел был единственный
            head = null;
            tail = null;
        }
        handMadeLinkedMap.remove(node.getTask().getID()); // безболезненно удаляем нужный узел
    }


    @Override
    public void remove(int id) {
        removeNode(getNode(id));
    }

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
