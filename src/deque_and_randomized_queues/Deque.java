package deque_and_randomized_queues;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private int size;
    private Node head;
    private Node tail;

    private class Node {
        private Item item;
        private Node next;
        private Node prev;

        Node(Item item) {
            this.item = item;
        }

        Node(Item item, Node next) {
            this.item = item;
            this.next = next;
        }

        public Node(Item item, Node next, Node prev) {
            this.item = item;
            this.next = next;
            this.prev = prev;
        }
    }

    public Deque() {

    }

    public boolean isEmpty() {
        return head == null;
    }

    public int size() {
        return size;
    }

    public void addFirst(Item item) {
        validateRequest(item);
        if (isEmpty()) {
            head = new Node(item);
            tail = head;
        } else {
            Node oldHead = head;
            head = new Node(item, oldHead);
            oldHead.prev = head;
        }
        size++;
    }

    public void addLast(Item item) {
        validateRequest(item);
        if (isEmpty()) {
            tail = new Node(item);
            head = tail;
        } else {
            Node oldTail = tail;
            tail = new Node(item, null, oldTail);
            oldTail.next = tail;
        }
        size++;
    }

    private void validateRequest(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You can not add item as null");
        }
    }

    public Item removeFirst() {
        validateRequest();
        Item item = head.item;
        head = head.next;
        if (!isEmpty()) {
            head.prev = null;
        }
        size--;
        return item;
    }

    public Item removeLast() {
        validateRequest();
        Item item = tail.item;
        tail = tail.prev;
        if (tail != null) {
            tail.next = null;
        } else {
            head = null;
        }
        size--;
        return item;
    }

    private void validateRequest() {
        if (isEmpty()) {
            throw new NoSuchElementException("Deque is empty");
        }
    }

    public Iterator<Item> iterator() {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {

        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to return");
            }
            Item item = current.item;
            current = current.next;
            return item;
        }

        public void remove() {
            throw new UnsupportedOperationException("This method is not supported");
        }
    }
}
