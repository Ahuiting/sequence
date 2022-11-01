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
    public static void runNeedlemanWunschQuadraticSpace(Pair x, Pair y) {
        // todo: implement, Assignment 2.1
        long start = System.currentTimeMillis();
        String[] result = nw(x, y);
        System.out.println("score: " + result[0]);
        printAlignment(result[1], result[2]);
        long end = System.currentTimeMillis();
        System.out.println("runtime: " + (end - start) + "ms");
    }

    static String[] nw(Pair x, Pair y) {
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
                int aboveScore = scoreMatrix[i - 1][j] - gap;
                int leftScore = scoreMatrix[i][j - 1] - gap;
                int maxScore = Math.max(Math.max(diagScore, aboveScore), leftScore);
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
        return new String[]{String.valueOf(scoreMatrix[n][m]), alignment1.reverse().toString(), alignment2.reverse().toString()};
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
        long start = System.currentTimeMillis();
        String[] alignments = linearspace(x, y, true);
        printAlignment(alignments[0], alignments[1]);
        long end = System.currentTimeMillis();
        System.out.println("runtime: " + (end - start) + "ms");
    }

    static String[] linearspace(Pair x, Pair y, boolean first) {
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
        } else if (n == 1 || m == 1) {
            String[] nw = nw(x, y);
            alignment1.append(nw[1]);
            alignment2.append(nw[2]);

        } else {
            boolean saveIDX = false;
            int[][] idxMatrix = new int[n + 1][2];
            int middleY = m / 2, middleX;
            int[][] scoreMatrix = new int[n + 1][2];
            for (int i = 0; i < n + 1; i++)
                scoreMatrix[i][0] = -gap * i;
            for (int j = 1; j < m + 1; j++) {
                if (j == middleY + 1) {
                    saveIDX = true;
                    for (int i = 0; i < n + 1; i++) {
                        idxMatrix[i][0] = i;
                    }
                }
                int previousIdx = (j + 1) % 2;
                int currentIdx = j % 2;
                scoreMatrix[0][currentIdx] = -gap * j;

                for (int i = 1; i < n + 1; i++) {
                    int diagScore = scoreMatrix[i - 1][previousIdx] + (x.sequence().charAt(i - 1) == y.sequence().charAt(j - 1) ? match : mismatch);
                    int aboveScore = scoreMatrix[i - 1][currentIdx] - gap;
                    int leftScore = scoreMatrix[i][previousIdx] - gap;
                    int maxScore = Math.max(Math.max(diagScore, leftScore), aboveScore);
                    scoreMatrix[i][currentIdx] = maxScore;
                    if (saveIDX) {
                        int idx = (j - middleY) % 2;
                        idxMatrix[0][idx] = idxMatrix[0][(idx + 1) % 2];
                        if (maxScore == diagScore)
                            idxMatrix[i][idx] = idxMatrix[i - 1][(idx + 1) % 2];
                        else if (maxScore == aboveScore)
                            idxMatrix[i][idx] = idxMatrix[i - 1][idx];
                        else if (maxScore == leftScore)
                            idxMatrix[i][idx] = idxMatrix[i][(idx + 1) % 2];
                    }
                }

            }
            if (first)
                System.out.println("score: " + scoreMatrix[n][m % 2]);
            middleX = idxMatrix[n][(m - middleY) % 2];
            Pair fx = new Pair("front_x", x.sequence().substring(0, middleX));
            Pair bx = new Pair("back_x", x.sequence().substring(middleX, n));
            Pair fy = new Pair("front_y", y.sequence().substring(0, middleY));
            Pair by = new Pair("back_y", y.sequence().substring(middleY, m));
            String[] f = linearspace(fx, fy, false);
            String[] b = linearspace(bx, by, false);
            alignment1.append(f[0]);
            alignment1.append(b[0]);
            alignment2.append(f[1]);
            alignment2.append(b[1]);
        }
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
        for (int l = 10; l <= 20; l = l + 5) {
            long start = System.currentTimeMillis();
            int score = computeF(l, l, x.sequence().substring(0, l), y.sequence().substring(0, l));
            System.out.println("length: " + l + "\tscore: " + score);
            long end = System.currentTimeMillis();
            System.out.println("runtime: " + (end - start) + "ms");
        }
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

    static void printAlignment(String x, String y) {
        int length = 60, turns;
        turns = x.length() / length + ((x.length() % length == 0) ? 0 : 1);
        for (int i = 0; i < turns; i++) {
            if (x.length() >= (i + 1) * length) {
                System.out.println("sequence1: " + x.substring(i * 60, (i + 1) * 60));
                System.out.println("sequence2: " + y.substring(i * 60, (i + 1) * 60));
                System.out.println();
            } else {
                System.out.println("sequence1: " + x.substring(i * 60));
                System.out.println("sequence2: " + y.substring(i * 60));
                System.out.println();
            }
        }

    }

}