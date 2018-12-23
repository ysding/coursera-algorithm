/* *****************************************************************************
 *  Name: Yusheng Ding
 *  Date: 12/20/2018
 *  Description: RandomizedQueue int resized array.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdRandom;

import java.util.Iterator;
import java.util.NoSuchElementException;

public class RandomizedQueue<Item> implements Iterable<Item> {
    private Item[] array;
    private int n;

    public RandomizedQueue()                 // construct an empty randomized queue
    {
        array = (Item[]) new Object[2];
        n = 0;
    }

    public boolean isEmpty()                 // is the randomized queue empty?
    {
        return n == 0;
    }

    public int size()                        // return the number of items on the randomized queue
    {
        return n;
    }

    public void enqueue(Item item)           // add the item
    {
        if (item == null) throw new java.lang.IllegalArgumentException();
        if (n == array.length) resize(2 * array.length);
        int i = StdRandom.uniform(n + 1);
        array[n] = array[i];
        array[i] = item;
        n += 1;
    }

    public Item dequeue()                    // remove and return a random item
    {
        if (isEmpty()) throw new NoSuchElementException();
        Item item = array[n - 1];
        array[n - 1] = null;
        n--;
        if (n > 0 && n == array.length / 4) resize(array.length / 2);
        return item;
    }

    public Item sample()                     // return a random item (but do not remove it)
    {
        if (isEmpty()) throw new NoSuchElementException();
        int i = StdRandom.uniform(n);
        return array[i];
    }

    public Iterator<Item> iterator()         // return an independent iterator over items in random order
    {
        return new ArrayIterator();
    }

    private void resize(int capacity) {
        assert capacity >= n;
        Item[] temp = (Item[]) new Object[capacity];
        for (int i = 0; i < n; i++) {
            temp[i] = array[i];
        }
        array = temp;
    }

    private class ArrayIterator implements Iterator<Item> {
        private int i;
        private Item[] tmp;

        public ArrayIterator() {
            tmp = array.clone();
            i = n;
        }

        public boolean hasNext() {
            return i > 0;
        }

        public void remove() {
            throw new UnsupportedOperationException();
        }

        public Item next() {
            if (!hasNext()) throw new NoSuchElementException();
            int t = StdRandom.uniform(i);
            Item item = tmp[t];
            tmp[t] = tmp[i - 1];
            tmp[i - 1] = null;
            i -= 1;
            return item;
        }
    }

    public static void main(String[] args)   // unit testing (optional)
    {

    }
}
