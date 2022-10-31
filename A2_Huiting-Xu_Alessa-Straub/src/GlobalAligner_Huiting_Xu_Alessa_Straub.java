import A1_Huiting_Xu_Alessa_Straub.FastA_Huiting_Xu_Alessa_Straub;
import A1_Huiting_Xu_Alessa_Straub.FastA_Huiting_Xu_Alessa_Straub.Pair;

import java.io.IOException;

/**
 * GlobalAligner_YOUR_NAME
 * Sequence Bioinformatics, WS 22/23
 */
public class GlobalAligner_Huiting_Xu_Alessa_Straub {
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2)
            throw new IOException("Usage: GlobalAligner_Huiting_Xu_Alessa_Straub infile [quadraticSpace|linearSpace|noDP]");

        var list = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);

        if (list.size() != 2)
            throw new IOException("Wrong number of input sequences: " + list.size());

        var mode = (args.length == 2 ? args[1] : "quadraticSpace");

        switch (mode) {
            case "quadraticSpace" -> runNeedlemanWunschQuadraticSpace(list.get(0), list.get(1));
            case "linearSpace" -> runNeedlemanWunschLinearSpace(list.get(0), list.get(1));
            case "noDP" -> runNeedlemanWunschRecursively(list.get(0), list.get(1));
            default -> throw new IOException("Unknown mode: " + mode);
        }
    }

    /**
     * computes the optimal global alignment score and an alignment, using quadratic space.
     * Prints out the optimal score and a corresponding alignment
     * Also prints out the number of milliseconds required for the computation
     *
     * @param x
     * @param y
     */
    public static String[] runNeedlemanWunschQuadraticSpace(Pair x, Pair y) {
        // todo: implement, Assignment 2.1
        long start = System.currentTimeMillis();
        int n = x.sequence().length();
        int m = y.sequence().length();
        int match = 1, mismatch = -1, gap = 1;

        int[][] scoreMatrix = new int[n + 1][m + 1];
        scoreMatrix[0][0] = 0;

        for (int i = 1; i < n + 1; i++)
            scoreMatrix[i][0] = -gap * i;
        for (int j = 1; j < m + 1; j++)
            scoreMatrix[0][j] = -gap * j;

        for (int i = 1; i < n + 1; i++) {
            for (int j = 1; j < m + 1; j++) {
                int diagScore = scoreMatrix[i - 1][j - 1] + (x.sequence().charAt(i - 1) == y.sequence().charAt(j - 1) ? match : mismatch);
                int leftScore = scoreMatrix[i - 1][j] - gap;
                int aboveScore = scoreMatrix[i][j - 1] - gap;
                int maxScore = Math.max(Math.max(diagScore, leftScore), aboveScore);
                scoreMatrix[i][j] = maxScore;
            }
        }

        // traceback
        StringBuilder alignment1 = new StringBuilder(), alignment2 = new StringBuilder();
        int i = n, j = m;
        while (i != 0 || j != 0)
            if (i > 0 && j > 0 && scoreMatrix[i][j] == (scoreMatrix[i - 1][j - 1] + (x.sequence().charAt(i - 1) == y.sequence().charAt(j - 1) ? match : mismatch))) {
                alignment1.append(x.sequence().charAt(--i));
                alignment2.append(y.sequence().charAt(--j));
            } else if (i > 0 && scoreMatrix[i][j] == scoreMatrix[i - 1][j] - gap) {
                alignment1.append(x.sequence().charAt(--i));
                alignment2.append("-");
            } else if (j > 0 && scoreMatrix[i][j] == scoreMatrix[i][j - 1] - gap) {
                alignment1.append("-");
                alignment2.append(y.sequence().charAt(--j));
            }
        System.out.println("score: " + scoreMatrix[n][m] + "\n" + alignment1.reverse() + "\n" + alignment2.reverse());
        long end = System.currentTimeMillis();
        System.out.println(end - start);

        return new String[]{alignment1.toString(), alignment2.toString()};
    }


    /**
     * computes the optimal global alignment score and an alignment, using linear space
     * Prints out the optimal score and a corresponding alignment
     * Also prints out the number of milliseconds required for the computation
     *
     * @param x
     * @param y
     * @return
     */
    public static void runNeedlemanWunschLinearSpace(Pair x, Pair y) {
        // todo: implement, Assignment 2.2

        System.out.println(linearspace(x, y));

    }

    static String[] linearspace(Pair x, Pair y) {
        long start = System.currentTimeMillis();
        int match = 1, mismatch = -1, gap = 1;
        int n = x.sequence().length();
        int m = y.sequence().length();

        StringBuilder alignment1 = new StringBuilder(), alignment2 = new StringBuilder();
        if (n == 0) {
            for (int i = 0; i < m; i++) {
                alignment1.append("-");
                alignment2.append(y.sequence().charAt(i));
            }
        } else if (m == 0) {
            for (int i = 0; i < n; i++) {
                alignment1.append(x.sequence().charAt(i));
                alignment2.append("-");
            }
        } else if (n <5 || m < 5) {
            String[] nw = runNeedlemanWunschQuadraticSpace(x, y);
            alignment1.append(nw[0]);
            alignment2.append(nw[1]);

        } else {
            int middleY = m / 2, middleX = 0;
            int[][] scoreMatrix = new int[n + 1][2];
            for (int i = 0; i < n + 1; i++)
                scoreMatrix[i][0] = -gap * i;
            for (int j = 1; j < m + 1; j++) {
                int previousIdx = (j + 1) % 2;
                int currentIdx = j % 2;
                scoreMatrix[0][currentIdx] = -gap * j;
                for (int i = 1; i < n + 1; i++) {
                    int diagScore = scoreMatrix[i - 1][previousIdx] + (x.sequence().charAt(i - 1) == y.sequence().charAt(j - 1) ? match : mismatch);
                    int leftScore = scoreMatrix[i - 1][currentIdx] - gap;
                    int aboveScore = scoreMatrix[i][previousIdx] - gap;
                    int maxScore = Math.max(Math.max(diagScore, leftScore), aboveScore);
                    scoreMatrix[i][currentIdx] = maxScore;
                }
                // todo: middle point
                if (j == middleY) {
                    int max = scoreMatrix[n][currentIdx];
                    for (int i = n; i >= 0; i--) {
                        if (max < scoreMatrix[i][currentIdx]) {
                            max = scoreMatrix[i][currentIdx];
                            middleX = i;
                        }
                    }
                }
            }
            Pair fx = new Pair("front_x", x.sequence().substring(0, middleX));
            System.out.println(fx.sequence());
            Pair bx = new Pair("back_x", x.sequence().substring(middleX, n));
            System.out.println(bx.sequence());
            Pair fy = new Pair("front_y", y.sequence().substring(0, middleY));
            Pair by = new Pair("back_y", y.sequence().substring(middleY, m));
            System.out.println("score: " + scoreMatrix[n][m % 2]);

            String[] f = linearspace(fx, fy);
            String[] b = linearspace(bx, by);
            alignment1.append(f[0]);
            alignment1.append(b[0]);
            alignment2.append(f[1]);
            alignment2.append(b[1]);
        }
        System.out.println(alignment1.toString() +"\n"+ alignment2.toString());
        long end = System.currentTimeMillis();
        System.out.println(end - start);
        return new String[]{alignment1.toString(), alignment2.toString()};
    }

    /**
     * computes the optimal global alignment score using a recursion and no table
     * Prints out the optimal score
     * Also prints out the number of milliseconds required for the computation
     *
     * @param x
     * @param y
     */
    public static void runNeedlemanWunschRecursively(Pair x, Pair y) {
        // todo: implement using recursive function computeF, , Assignment 2.3
        long start = System.currentTimeMillis();
        int i = x.sequence().length(), j = y.sequence().length();
        int score = computeF(i, j, x.sequence(), y.sequence());

        System.out.println(score);
        long end = System.currentTimeMillis();
        System.out.println(end - start);
    }

    public static int computeF(int i, int j, String x, String y) {
        // todo: implement
        int match = 1, mismatch = -1, gap = 1;
        if (i == 0 && j == 0)
            return 0;
        else if (i == 0)
            return -j * gap;
        else if (j == 0)
            return -i * gap;
        else {
            return Math.max(computeF(i - 1, j - 1, x, y) + (x.charAt(i - 1) == y.charAt(j - 1) ? match : mismatch),
                    Math.max(computeF(i - 1, j, x, y) - gap, computeF(i, j - 1, x, y) - gap));
        }

    }


}