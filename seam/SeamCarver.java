/* *****************************************************************************
 *  Name:
 *  Date:
 *  Description:
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {
    // create a seam carver object based on the given picture
    private Picture picture;

    public SeamCarver(Picture picture) {
        if (picture == null) throw new IllegalArgumentException();
        this.picture = new Picture(picture);
    }

    public Picture picture()                          // current picture
    {
        return new Picture(this.picture);
    }

    public int width()                            // width of current picture
    {
        return picture.width();
    }

    public int height()                           // height of current picture
    {
        return picture.height();
    }

    private double calEnergy(int x0, int y0, int x1, int y1) {
        int c0 = picture.getRGB(x0, y0);
        int c1 = picture.getRGB(x1, y1);
        int sum = 0;
        for (int i = 0; i < 3; i++) {
            sum += Math.pow((c0 >> (8 * i) & 0xFF) - (c1 >> (8 * i) & 0xFF), 2);
        }
        return sum;
    }

    public double energy(int x, int y)               // energy of pixel at column x and row y
    {

        if (y < 0 || y > picture.height() - 1 || x < 0 || x > picture.width() - 1)
            throw new IllegalArgumentException();

        if (x == 0 || y == picture.height() - 1 || y == 0 || x == picture.width() - 1)
            return 1000;
        double sum = 0;
        sum += calEnergy(x + 1, y, x - 1, y);
        sum += calEnergy(x, y + 1, x, y - 1);
        sum = Math.sqrt(sum);
        return sum;
    }

    public int[] findHorizontalSeam()               // sequence of indices for horizontal seam
    {
        // from top to down to calculate the minimum energy cost of path to i j;
        double[][] energyCost = new double[picture.height()][picture.width()];
        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                if (i == 0)
                    energyCost[j][i] = energy(i, j);
                else {
                    double topMin = energyCost[j][i - 1];
                    if (j > 0) topMin = Math.min(topMin, energyCost[j - 1][i - 1]);
                    if (j < picture.height() - 1)
                        topMin = Math.min(topMin, energyCost[j + 1][i - 1]);
                    energyCost[j][i] = energy(i, j) + topMin;
                }
            }
        }
        int[] path = new int[picture.width()];
        double tmpCost = energyCost[0][picture.width() - 1];
        for (int j = 0; j < picture.height(); j++) {
            if (energyCost[j][picture.width() - 1] <= tmpCost) {
                path[picture.width() - 1] = j;
                tmpCost = energyCost[j][picture.width() - 1];
            }
        }

        for (int i = picture.width() - 1; i > 0; i--) {
            path[i - 1] = path[i];
            if (path[i] > 0 && energyCost[path[i - 1]][i - 1] > energyCost[path[i] - 1][i - 1]) {
                path[i - 1] = path[i] - 1;
            }
            if (path[i] < picture.height() - 1 &&
                    energyCost[path[i - 1]][i - 1] > energyCost[path[i] + 1][i - 1]) {
                path[i - 1] = path[i] + 1;
            }
        }
        return path;
    }

    public int[] findVerticalSeam()                 // sequence of indices for vertical seam
    {
        // from top to down to calculate the minimum energy cost of path to i j;
        double[][] energyCost = new double[picture.height()][picture.width()];
        for (int i = 0; i < picture.height(); i++) {
            for (int j = 0; j < picture.width(); j++) {
                if (i == 0)
                    energyCost[i][j] = energy(j, i);
                else {
                    double topMin = energyCost[i - 1][j];
                    if (j > 0) topMin = Math.min(topMin, energyCost[i - 1][j - 1]);
                    if (j < picture.width() - 2)
                        topMin = Math.min(topMin, energyCost[i - 1][j + 1]);
                    energyCost[i][j] = energy(j, i) + topMin;
                }
            }
        }
        int[] path = new int[picture.height()];
        double tmpCost = energyCost[picture.height() - 1][0];
        for (int j = 0; j < picture.width(); j++) {
            if (energyCost[picture.height() - 1][j] <= tmpCost) {
                path[picture.height() - 1] = j;
                tmpCost = energyCost[picture.height() - 1][j];
            }
        }

        for (int i = picture.height() - 1; i > 0; i--) {
            path[i - 1] = path[i];
            if (path[i] > 0 && energyCost[i - 1][path[i - 1]] > energyCost[i - 1][path[i] - 1]) {
                path[i - 1] = path[i] - 1;
            }
            if (path[i] < picture.width() - 1 &&
                    energyCost[i - 1][path[i - 1]] > energyCost[i - 1][path[i] + 1]) {
                path[i - 1] = path[i] + 1;
            }
        }
        return path;
    }

    private boolean valid(int[] seam, int length, int bound) {
        if (seam == null || seam.length != length) return false;
        int last = seam[0];
        for (int now : seam) {
            if (now < 0 || now >= bound || Math.abs(now - last) > 1) {
                return false;
            }
            last = now;
        }
        return true;
    }

    public void removeHorizontalSeam(int[] seam)   // remove horizontal seam from current picture
    {
        if (!valid(seam, picture.width(), picture.height()) || picture.height() <= 1)
            throw new IllegalArgumentException();
        Picture anotherPicture = new Picture(picture.width(), picture.height() - 1);
        for (int i = 0; i < picture.width(); i++)
            for (int j = 0; j < picture.height() - 1; j++) {
                if (j < seam[i]) {
                    anotherPicture.set(i, j, picture.get(i, j));
                }
                else {
                    anotherPicture.set(i, j, picture.get(i, j + 1));
                }
            }
        this.picture = anotherPicture;
    }

    public void removeVerticalSeam(int[] seam)     // remove vertical seam from current picture
    {
        if (!valid(seam, picture.height(), picture.width()) || picture.width() <= 1)
            throw new IllegalArgumentException();
        Picture anotherPicture = new Picture(picture.width() - 1, picture.height());
        for (int i = 0; i < picture.height(); i++)
            for (int j = 0; j < picture.width() - 1; j++) {
                if (j < seam[i]) {
                    anotherPicture.set(j, i, picture.get(j, i));
                }
                else {
                    anotherPicture.set(j, i, picture.get(j + 1, i));
                }
            }
        this.picture = anotherPicture;
    }
}
