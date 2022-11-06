import java.io.IOException;
import java.util.ArrayList;

/**
 * count number of edges between nucleotides in different sequences and the number of mixed cycles
 * Sequence Bioinformatics, WS22/23
 */
public class CountEdgesSimpleMixedCycles_Huiting_Xu_Alessa_Straub {

    public static void main(String[] args) throws IOException {
        if (args.length != 3)
            throw new IOException("Usage: CountEdgesSimpleMixedCycles_YOUR_Name aLength bLength cLength");

        var length = new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])};

        System.out.println(CountEdgesSimpleMixedCycles_Huiting_Xu_Alessa_Straub.class.getSimpleName());

        System.out.printf("Sequence lengths: %d %d %d%n", length[0], length[1], length[2]);

        // todo: report the number of edges between nucleotides in different sequences
        int count = 0;
        for (int n = 0; n <= 2; n++) {
            for (int m = n + 1; m <= 2; m++) {
                for (int i = 0; i < length[n]; i++) {
                    for (int j = 0; j < length[m]; j++) {
                        count++;
                    }
                }
            }
        }

        var numEdgesBetweenDifferenteSequences = count;

        System.out.printf("Edges between different sequences: %d%n", numEdgesBetweenDifferenteSequences);


        var numSimpleMixedCycles = 0;

        // todo: implement counting of number of simple mixed cycles
        // first compute the number of simple mixed cycles that use two cycle
        numSimpleMixedCycles += cycleBetweenTwoCycle(length[0], length[1]).size();
        numSimpleMixedCycles += cycleBetweenTwoCycle(length[0], length[2]).size();
        numSimpleMixedCycles += cycleBetweenTwoCycle(length[1], length[2]).size();

        // then compute and add the number of simple mixed cycles that use three cycles
        numSimpleMixedCycles += cycleBetweenThreeCycle(length[0], length[1], length[2]).size();
        System.out.printf("Total simple mixed cycles: %d%n", numSimpleMixedCycles);
    }

    static ArrayList<String> cycleBetweenTwoCycle(int length1, int length2) {
        ArrayList<String> constrains = new ArrayList<>();
        for (int i = 0; i < length1; i++) { // start point in first sequence
            for (int j = i; j < length1; j++) { //end point in second sequence
                for (int k = 0; k < length2; k++) { // start point in second sequence
                    for (int l = k; l < length2; l++) { // end point in second sequence
                        if (!(i == j && k == l)) { // when i = j and k = l, it's a line not a cycle
                            constrains.add("X_" + i + j + "+" + "X_" + k + l + "<1;");
                        }
                    }
                }
            }
        }
        return constrains;
    }

    static ArrayList<String> cycleBetweenThreeCycle(int length1, int length2, int length3) {
        ArrayList<String> constrains = new ArrayList<>();
        for (int i = 0; i < length1; i++) { // start point in first sequence
            for (int j = i; j < length1; j++) {  // end point in first sequence
                // after exiting first sequence, enter second sequence first and then third sequence.
                for (int k = 0; k < length2; k++) {  // start point in second sequence
                    for (int l = k; l < length2; l++) {  // end point in second sequence
                        for (int m = 0; m < length3; m++) {  // start point in third sequence
                            for (int n = m; n < length3; n++) { // end point in third sequence
                                if (!(i == j && k == l && n == m)) { //when i = j and k = l and n = m, it's a line not a cycle
                                    constrains.add("X_" + i + j + "+" + "X_" + k + l + "X_" + n + m + "<1;");
                                }
                            }
                        }
                    }
                }
                // after exiting first sequence, enter third sequence first and then second sequence.
                for (int m = 0; m < length3; m++) { // start point in third sequence
                    for (int n = m; n < length3; n++) { // end point in third sequence
                        for (int k = 0; k < length2; k++) { // start point in second sequence
                            for (int l = k; l < length2; l++) { // end point in second sequence
                                if (!(i == j && k == l && n == m)) { //when n = m andi = j and k = l, it's a line not a cycle
                                    constrains.add("X_" + i + j + "+" + "X_" + k + l + "X_" + n + m + "<1;");
                                }
                            }
                        }
                    }
                }
            }
        }
        return constrains;
    }

}
