/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import java.util.Arrays;

public class CircularSuffixArray {
    private final CircularString[] array;

    private class CircularString implements Comparable<CircularString> {
        private final char[] s;
        private final int i;

        public CircularString(char[] s, int i) {
            this.s = s;
            this.i = i;
        }

        @Override
        public int compareTo(CircularString that) {
            int d = 0;
            while (i + d < 2 * s.length && that.i + d < 2 * s.length) {
                if (s[(i + d) % s.length] < s[(that.i + d) % s.length]) return -1;
                if (s[(i + d) % s.length] > s[(that.i + d) % s.length]) return 1;
                d++;
            }
            return 0;
        }
    }

    public CircularSuffixArray(String s)    // circular suffix array of s
    {
        if (s == null) throw new IllegalArgumentException();
        char[] text = s.toCharArray();
        array = new CircularString[text.length];
        for (int i = 0; i < text.length; i++) {
            array[i] = new CircularString(text, i);
        }
        Arrays.sort(array);
    }

    public int length()                     // length of s
    {
        return array.length;
    }

    public int index(int i)                 // returns index of ith sorted suffix
    {
        if (i < 0 || i >= array.length) throw new IllegalArgumentException();
        return array[i].i;
    }

    public static void main(String[] args)  // unit testing (required)
    {
        CircularSuffixArray csa = new CircularSuffixArray("ABRACADABRA!");
        System.out.println(csa.length());
        for (int i = 0; i < 12; i++)
            System.out.println(csa.index(i));
    }
}
