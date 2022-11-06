import java.io.IOException;

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
        for (int n = 0; n <= 2; n++) { // first sequence
            for (int m = n + 1; m <= 2; m++) { // second sequence
                for (int i = 0; i < length[n]; i++) { // start point in first sequence
                    for (int j = i; j < length[n]; j++) { //end point in second sequence
                        for (int k = 0; k < length[m]; k++) { // start point in second sequence
                            for (int l = k; l < length[m]; l++) { // end point in second sequence
                                if (!(i == j && k == l)) { // when i = j and k = l, it's a line not a cycle
                                    numSimpleMixedCycles++;
                                }
                            }
                        }
                    }
                }
            }
        }
        // then compute and add the number of simple mixed cycles that use three cycles
        for (int i = 0; i < length[0]; i++) { // start point in first sequence
            for (int j = i; j < length[0]; j++) {  // end point in first sequence
                // after exiting first sequence, enter second sequence first and then third sequence.
                for (int k = 0; k < length[1]; k++) {  // start point in second sequence
                    for (int l = k; l < length[1]; l++) {  // end point in second sequence
                        for (int m = 0; m < length[2]; m++) {  // start point in third sequence
                            for (int n = m; n < length[2]; n++) { // end point in third sequence
                                if (!(n == m && i == j && k == l)) { //when n = m andi = j and k = l, it's a line not a cycle
                                    numSimpleMixedCycles++;
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
                                if (!(n == m && i == j && k == l)) { //when n = m andi = j and k = l, it's a line not a cycle
                                    numSimpleMixedCycles++;
                                }
                            }
                        }
                    }
                }
            }
        }

        System.out.printf("Total simple mixed cycles: %d%n", numSimpleMixedCycles);
    }
}
