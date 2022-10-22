import java.io.FileWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

/**
 * EditDistance_Huiting_Xu_Alessa_Straub
 * Author(s): Huiting Xu and Alessa Straub
 * Sequence Bioinformatics, WS 22/23
 */
public class EditDistance_Huiting_Xu_Alessa_Straub {

    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2)
            throw new IOException("Usage: EditDistance_YOUR_NAME infile [outFile]");

        // todo: implement input of FastA records
        ArrayList<FastA_Huiting_Xu_Alessa_Straub.Pair> input = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);

        // todo: check that all input sequences have the same length, otherwise throw a new IOException("Different lengths");
        for (int i = 0; i < input.size() - 1; i++) {
            if (input.get(i).toString().length() != input.get(i + 1).toString().length()) {
                throw new IOException("Different lengths");
            }
        }

        try (Writer w = (args.length == 2 ? new FileWriter(args[1]) : new OutputStreamWriter(System.out))) {
            // todo: compute distance between any two sequences, using method computeEditDistance(x,y) defined below
            // todo: write distance matrix
            for (int i = 0; i < input.size(); i++) {
                var line = new StringBuilder();
                line.append(input.get(i).header());
                for (int j = 0; j < input.size(); j++) {
                    line.append("\t" + computeEditDistance(input.get(i).sequence(), input.get(j).sequence()));
                }
                w.write(line + "\n");
            }

        }
        // example of format:
        // a 0 1 2 3
        // b 1 0 4 5
        // c 2 4 0 6
        // d 3 5 6 0
    }

    private static int computeEditDistance(String x, String y) {
        // todo: implement computation of edit distance
        int count = 0;
        for (int i = 0; i < x.length(); i++) {
            if (x.charAt(i) != y.charAt(i)) {
                count++;
            }
        }
        return count;
    }
}
