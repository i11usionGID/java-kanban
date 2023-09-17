package finaltask.manager;

import finaltask.task.Task;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager{

    private Node first;
    private Node last;
    private Map<Integer, Node> map = new HashMap<>();

    @Override
    public List<Task> getHistory() {
        List <Task> list = new ArrayList<>();
        Node current = first;
        while(current != null){
            list.add(current.getValue());
            current = current.next;
        }
        return list;
    }

    @Override
    public void add(Task task) {
        if(map.containsKey(task.getID())){
            removeNode(map.get(task.getID()));
        }
        Node node = new Node(task);
        if (first != null) {
            last.next = node;
            node.prev = last;
            last = node;
            map.put(task.getID(), node);
        } else {
            first = node;
            last = node;
            map.put(task.getID(), node);
        }
    }
    @Override
    public void remove(int id){
        removeNode(map.get(id));
        map.remove(id);

    }
    private void removeNode(Node node){
        Node current = node;
        if(first == last){
            first = null;
            last = null;
        }else if(node == first){
            first = current.next;
            current.next.prev = null;
            current.next = null;
        } else if(node == last){
            last = current.prev;
            current.prev.next = null;
            current.prev = null;
        } else {
            current.next.prev = current.prev;
            current.prev.next = current.next;
            current.prev = null;
            current.next = null;
        }

    }

    private static class Node{

        Task value;
        Node next;
        Node prev;
        public Node(Task value){
            this.value=value;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Task getValue() {
            return value;
        }

        public Node getNext() {
            return next;
        }

        public Node getPrev() {
            return prev;
        }
    }
}
