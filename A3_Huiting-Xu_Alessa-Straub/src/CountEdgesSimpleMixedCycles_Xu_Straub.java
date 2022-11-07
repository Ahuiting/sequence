import java.io.IOException;
import java.util.ArrayList;

/**
 * count number of edges between nucleotides in different sequences and the number of mixed cycles
 * Sequence Bioinformatics, WS22/23
 */
public class CountEdgesSimpleMixedCycles_Xu_Straub {

    public static void main(String[] args) throws IOException {
        if (args.length != 3)
            throw new IOException("Usage: CountEdgesSimpleMixedCycles_YOUR_Name aLength bLength cLength");

        var length = new int[]{Integer.parseInt(args[0]), Integer.parseInt(args[1]), Integer.parseInt(args[2])};

        System.out.println(CountEdgesSimpleMixedCycles_Xu_Straub.class.getSimpleName());

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
        numSimpleMixedCycles += cycleBetweenTwoCycle(length).size();

        // then compute and add the number of simple mixed cycles that use three cycles
        numSimpleMixedCycles += cycleBetweenThreeCycle(length).size();
        System.out.printf("Total simple mixed cycles: %d%n", numSimpleMixedCycles);
    }

    static ArrayList<String> cycleBetweenTwoCycle(int[] length) {
        ArrayList<String> constraints = new ArrayList<>();
        for (int first = 0; first < length.length; first++) {
            for (int second = first + 1; second < length.length; second++) {
                for (int i = 0; i < length[first]; i++) { // start point in first sequence
                    for (int j = i; j < length[first]; j++) { //end point in second sequence
                        for (int k = 0; k < length[second]; k++) { // start point in second sequence
                            for (int l = k; l < length[second]; l++) { // end point in second sequence
                                if (!(i == j && k == l)) { // when i = j and k = l, it's a line not a cycle
                                    constraints.add("X" + first + i + "_" + second + l + "+" + "X" + first + j + "_" + second + k + "<1;");
                                }
                            }
                        }
                    }
                }
            }
        }
        return constraints;
    }

    static ArrayList<String> cycleBetweenThreeCycle(int[] length) {
        ArrayList<String> constraints = new ArrayList<>();
        for (int i = 0; i < length[0]; i++) { // start point in first sequence
            for (int j = i; j < length[0]; j++) {  // end point in first sequence
                // after exiting first sequence, enter second sequence first and then third sequence.
                for (int k = 0; k < length[1]; k++) {  // start point in second sequence
                    for (int l = k; l < length[1]; l++) {  // end point in second sequence
                        for (int m = 0; m < length[2]; m++) {  // start point in third sequence
                            for (int n = m; n < length[2]; n++) { // end point in third sequence
                                if (!(i == j && k == l && n == m)) { //when i = j and k = l and n = m, it's a line not a cycle
                                    constraints.add("X0" + j + "_1" + k + "+X1" + l + "_2" + m + "+" + "X0" + i + "_2" + n + "<2;");
                                }
                            }
                        }
                    }
                }
                // after exiting first sequence, enter third sequence first and then second sequence.
                for (int m = 0; m < length[2]; m++) { // start point in third sequence
                    for (int n = m; n < length[2]; n++) { // end point in third sequence
                        for (int k = 0; k < length[1]; k++) { // start point in second sequence
                            for (int l = k; l < length[1]; l++) { // end point in second sequence
                                if (!(i == j && k == l && n == m)) { //when n = m andi = j and k = l, it's a line not a cycle
                                    constraints.add("X0" + j + "_2" + m + "+X1" + k + "_2" + n + "+" + "X0" + i + "_1" + l + "<2;");
                                }
                            }
                        }
                    }
                }
            }
        }
        return constraints;
    }

}
