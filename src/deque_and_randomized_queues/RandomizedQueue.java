package deque_and_randomized_queues;

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {

    private Item[] rq;
    private int size = 0;
    private int head = 0;
    private int tail = 1;

    public RandomizedQueue() {
        rq = (Item[]) new Object[2];
    }

    public boolean isEmpty() {
        return size() == 0;
    }

    public int size() {
        return size;
    }

    public void enqueue(Item item) {
        validateRequest(item);
        addItem(item);
    }

    private void validateRequest(Item item) {
        if (item == null) {
            throw new IllegalArgumentException("You can not add item as null");
        }
    }

    private void addItem(Item item) {
        if (head == -1) {
            expandContainer();
        }
        rq[head--] = item;
        size++;
    }

    private void expandContainer() {
        int length = rq.length;
        int firstIndex = length;
        Item[] copy = (Item[]) new Object[2 * length];
        for (int i = head + 1; i < tail; i++) {
            copy[firstIndex++] = rq[i];
        }
        head = length - 1;
        tail = length + size;
        rq = copy;
    }

    public Item dequeue() {
        validateRequest();
        if (size > 0 && size == rq.length/4) {
            collapseContainer(rq.length/2);
        }
        changeLastItem();
        Item item = rq[tail];
        rq[tail] = null;
        size--;
        return item;
    }

    private void changeLastItem() {
        int i = StdRandom.uniform(head +  1, tail);
        int j = --tail;
        Item temp = rq[i];
        rq[i] = rq[j];
        rq[j] = temp;
    }

    private void collapseContainer(int capacity) {
        Item[] copy = (Item[]) new Object[capacity];
        int firstIndex = 0;
        for (int i = head + 1; i < tail; i++) {
            copy[firstIndex++] = rq[i];
        }

        head = -1;
        tail = size;
        rq = copy;
    }

    public Item sample() {
        validateRequest();
        return rq[StdRandom.uniform(head + 1, tail)];
    }

    private void validateRequest() {
        if (isEmpty()) {
            throw new NoSuchElementException("RandomizedQueue is empty");
        }
    }

    public Iterator<Item> iterator() {
        return new RandomizedQueueIterator();
    }

    private class RandomizedQueueIterator implements Iterator<Item> {
        int[] random;
        int next = 0;

        public RandomizedQueueIterator() {
            random = new int[size];
            int firstIndex = 0;

            for (int i = head + 1; i < tail; i++) {
                random[firstIndex++] = i;
            }
            StdRandom.shuffle(random);
        }

        public boolean hasNext() {
            return next != random.length;
        }

        public Item next() {
            if (!hasNext()) {
                throw new NoSuchElementException("No more items to return");
            }

            return rq[random[next++]];
        }

        public void remove() {
            throw new UnsupportedOperationException("This method is not supported");
        }
    }
}
