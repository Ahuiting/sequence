import assignment01.FastA_Huiting_Xu_Alessa_Straub;

import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.ArrayList;

/**
 * setup ILP to solve multiple sequence alignment for three sequences
 * Sequence Bioinformatics, WS22/23
 */
public class AlignmentILP_Huiting_Xu_Alessa_Straub {
    public static void main(String[] args) throws IOException {
        if (args.length != 1 && args.length != 2) {
            throw new IOException("Usage: AlignmentILP_Huiting_Xu_Alessa_Strau input [output]");
        }

        var list = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);

        if (list.size() != 3) {
            throw new IOException("Input file must contain 3 sequences, found: " + list.size());
        }

        // todo: setup and write out ILP based on extended alignment graph, with match score =  4 and mismatch score = 1

        try (var w = (args.length == 2 ? new FileWriter(args[1]) : new OutputStreamWriter(System.out))) {
            w.write("max: ");
            // 1. write the objective function: loop over all pairs of sequences and all pairs of letters
            StringBuilder sb = new StringBuilder();
            ArrayList<String> constaintsBinary = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                for (int j = i + 1; j < list.size(); j++) {
                    for (int n = 0; n < list.get(i).sequence().length(); n++) {
                        for (int m = 0; m < list.get(j).sequence().length(); m++) {
                            if (list.get(i).sequence().charAt(n) == list.get(j).sequence().charAt(m)) {
                                sb.append("4*X" + i + n + "_" + j + m + "+");
                            } else sb.append("1*X" + i + n + "_" + j + m + "+");
                            constaintsBinary.add("X" + i + n + "_" + j + m);
                        }
                    }
                }
            }
            w.write(sb.substring(0, sb.length() - 1) + ";");


            // 2. write out all the simple mixed cycle constraints between any two sequences
            ArrayList<String> constaintsWithTwoSequence = new ArrayList();
            int[] length = new int[]{list.get(0).sequence().length(), list.get(1).sequence().length(), list.get(2).sequence().length()};
            constaintsWithTwoSequence.addAll(CountEdgesSimpleMixedCycles_Huiting_Xu_Alessa_Straub.cycleBetweenTwoCycle(length));
            for (String string : constaintsWithTwoSequence) {
                w.write("\n" + string);
            }

            // 3. write out all the simple mixed cycle constraints between any three sequences
            ArrayList<String> constaintsWithThreeSequence = CountEdgesSimpleMixedCycles_Huiting_Xu_Alessa_Straub.cycleBetweenThreeCycle(length);
            for (String string : constaintsWithThreeSequence) {
                w.write("\n" + string);
            }
            // 4. write out the binary variable constraints
            for (String string : constaintsBinary) {
                w.write("\n" + string + "<1;");
            }

            // 5. specify all variables as integers
            w.write("\nint ");
            for (int i = 0; i < constaintsBinary.size() - 1; i++) {
                w.write(constaintsBinary.get(i) + ",");
            }
            w.write(constaintsBinary.get(constaintsBinary.size() - 1));
            w.write(";");
        }
    }

}
