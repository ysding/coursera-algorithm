/* *****************************************************************************
 *  Name: Yusheng Ding
 *  Date: 12/20/2018
 *  Description: Permutation.
 **************************************************************************** */

import edu.princeton.cs.algs4.StdIn;

public class Permutation {
    public static void main(String[] args) {
        int k = Integer.parseInt(args[0]);
        RandomizedQueue<String> array = new RandomizedQueue<>();
        while (!StdIn.isEmpty()) {
            array.enqueue(StdIn.readString());
        }
        while (k > 0) {
            k -= 1;
            System.out.println(array.dequeue());
        }
    }
}
