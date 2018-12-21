/* *****************************************************************************
 *  Name: Yusheng Ding
 *  Date: 12/20/2018
 *  Description: Deque in linked list.
 **************************************************************************** */

import java.util.Iterator;
import java.util.NoSuchElementException;

public class Deque<Item> implements Iterable<Item> {

    private class Node {
        private Item item;
        private Node next;
        private Node pre;
    }

    private Node head;
    private Node tail;
    private int length;

    public Deque()                           // construct an empty deque
    {
        head = null;
        tail = null;
        length = 0;
    }

    public boolean isEmpty()                 // is the deque empty?
    {
        return length == 0;
    }

    public int size()                        // return the number of items on the deque
    {
        return length;
    }

    public void addFirst(Item item)          // add the item to the front
    {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (head == null) {
            head = node;
            tail = node;
        }
        else {
            head.pre = node;
            head = node;
        }
        length += 1;
    }

    public void addLast(Item item)           // add the item to the end
    {
        if (item == null) {
            throw new java.lang.IllegalArgumentException();
        }
        Node node = new Node();
        node.item = item;
        if (tail == null) {
            head = node;
            tail = node;
        }
        else {
            tail.next = node;
            tail = node;
        }
        length += 1;
    }

    public Item removeFirst()                // remove and return the item from the front
    {
        if (length == 0) {
            throw new java.util.NoSuchElementException();
        }
        Item item = head.item;
        head = head.next;
        if (head == null) {
            tail = null;
        }
        length -= 1;
        return item;
    }

    public Item removeLast()                 // remove and return the item from the end
    {
        if (length == 0) {
            throw new java.util.NoSuchElementException();
        }
        Item item = tail.item;
        tail = tail.pre;
        if (tail == null) {
            head = null;
        }
        length -= 1;
        return item;
    }

    public Iterator<Item> iterator()         // return an iterator over items in order from front to end
    {
        return new DequeIterator();
    }

    private class DequeIterator implements Iterator<Item> {
        private Node current = head;

        public boolean hasNext() {
            return current != null;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            Item item = current.item;
            current = current.next;
            return item;
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {

    }
}
