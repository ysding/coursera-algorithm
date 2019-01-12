/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;
import edu.princeton.cs.algs4.Queue;

import java.util.HashMap;
import java.util.Map;

public class BurrowsWheeler {
    // apply Burrows-Wheeler transform, reading from standard input and writing to standard output
    public static void transform() {
        String input = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(input);

        int first = 0;
        for (int i = 0; i < input.length(); i++) {
            if (csa.index(i) == 0)
                first = i;
        }
        BinaryStdOut.write(first);
        for (int i = 0; i < input.length(); i++) {
            if (csa.index(i) == 0) {
                BinaryStdOut.write(input.charAt(input.length() - 1), 8);
            }
            else
                BinaryStdOut.write(input.charAt(csa.index(i) - 1), 8);
        }
        BinaryStdOut.close();
    }

    // apply Burrows-Wheeler inverse transform, reading from standard input and writing to standard output
    public static void inverseTransform() {
        int first = BinaryStdIn.readInt();
        String t = BinaryStdIn.readString();
        int[] next = new int[t.length()];
        Map<Character, Queue<Integer>> indexes = new HashMap<Character, Queue<Integer>>();
        char key;
        int[] count = new int[256];
        for (int i = 0; i < t.length(); i++) { // record all indexes for each character
            key = t.charAt(i);
            count[key] += 1;
            if (!indexes.containsKey(key))
                indexes.put(key, new Queue<>());
            indexes.get(key).enqueue(i);
        }
        // sort the chars.
        char[] sortedChars = new char[t.length()];
        int tmp = 0;
        for (int i = 0; i < 256; i++) {
            while (count[i] > 0) {
                sortedChars[tmp++] = (char) i;
                count[i]--;
            }
        }

        for (int i = 0; i < t.length(); i++) {  // create next list.
            next[i] = indexes.get(sortedChars[i]).dequeue();
        }
        for (int i = 0; i < t.length(); i++) {  // create origin string
            BinaryStdOut.write(sortedChars[first]);
            first = next[first];
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply Burrows-Wheeler transform
    // if args[0] is '+', apply Burrows-Wheeler inverse transform
    public static void main(String[] args) {
        if (args[0].equals("-"))
            transform();
        else if (args[0].equals("+"))
            inverseTransform();
    }
}
