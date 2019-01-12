/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.BinaryStdIn;
import edu.princeton.cs.algs4.BinaryStdOut;

import java.util.ArrayList;

public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    private static final int R = 256;

    public static void encode() {
        int[] ord = new int[R]; // get char pos in ord depend on char's ord
        int[] array = new int[R];  // the array of characters
        for (int i = 0; i < R; i++) {
            array[i] = i;
            ord[i] = i;
        }
        char tmp; // the char that got from the stdin
        String s = BinaryStdIn.readString();
        char[] input = s.toCharArray();
        for (int i = 0; i < input.length; i++) {
            tmp = input[i];
            BinaryStdOut.write(ord[tmp], 8);  // output the index of the char
            for (int j = ord[tmp]; j > 0; j--) {
                ord[array[j - 1]] += 1;  // update char's index in ord
                array[j] = array[j - 1]; // move the all char before i backword by 1
            }
            ord[tmp] = 0;
            array[0] = tmp;
        }
        BinaryStdOut.close();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode() {

        ArrayList<Character> array = new ArrayList<>();
        for (int i = 0; i < R; i++) {
            array.add((char) (255 - i));
        }

        char tmp; // the char corresponding to the index
        while (!BinaryStdIn.isEmpty()) {
            tmp = array.remove(255 - BinaryStdIn.readChar());
            BinaryStdOut.write(tmp, 8);  // output the index of the char
            array.add(tmp);
        }
        BinaryStdOut.close();
    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args) {
        if (args[0].equals("-"))
            encode();
        else if (args[0].equals("+"))
            decode();
    }
}
