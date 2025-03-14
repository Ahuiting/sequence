package A1_Huiting_Xu_Alessa_Straub;

import A1_Huiting_Xu_Alessa_Straub.FastA_Huiting_Xu_Alessa_Straub;

import java.io.IOException;
import java.util.ArrayList;

/**
 * A1_Huiting_Xu_Alessa_Straub.EchoFastA_Huiting_Xu_Alessa_Straub
 * Author(s): Huiting Xu and Alessa Straub
 * Sequence Bioinformatics, WS 22/23
 */
public class EchoFastA_Huiting_Xu_Alessa_Straub {
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2)
            throw new IOException("Usage: EchoFastA_Huiting_Xu_Alessa_Straub infile [outFile]");
        // todo: read FastA records from infile and echo to outfile or stdout (console)
        ArrayList<FastA_Huiting_Xu_Alessa_Straub.Pair> lst = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);
        String filename = (args.length > 1) ? args[1] : null;
        FastA_Huiting_Xu_Alessa_Straub.write(lst, filename);
    }
}
