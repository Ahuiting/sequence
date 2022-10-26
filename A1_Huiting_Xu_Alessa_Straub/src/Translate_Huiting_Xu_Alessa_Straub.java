import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Translate_Huiting_Xu_Alessa_Straub
 * Author(s): Huiting Xu and Alessa Straub
 * Sequence Bioinformatics, WS 22/23
 */
public class Translate_Huiting_Xu_Alessa_Straub {
    public static void main(String[] args) throws IOException {
        if (args.length < 1 || args.length > 2)
            throw new IOException("Usage: Translate_YOUR_NAME infile [outFile]");

        // todo: read in FastA pairs
        var list = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);

        // todo: compute translated sequences using translate(sequence) method defined below
        var translated = new ArrayList<FastA_Huiting_Xu_Alessa_Straub.Pair>();

        // todo: write translated sequences
        for (FastA_Huiting_Xu_Alessa_Straub.Pair pair : list) {
            String protein_seq = translate(pair.sequence());
            translated.add(new FastA_Huiting_Xu_Alessa_Straub.Pair(pair.header(), protein_seq));
        }
        String filename = (args.length > 1) ? args[1] : null;
        FastA_Huiting_Xu_Alessa_Straub.write(translated, filename);
    }

    public static String translate(String sequence) {
        var buf = new StringBuilder();

        Map<String, String> dict = new HashMap<String, String>(){{
               put( "TTT", "F");put( "TTC", "F");
                put("TTA", "L");put( "TTG", "L");put("CTT", "L");put( "CTC", "L");put( "CTA", "L");put( "CTG", "L");
                put("ATT", "I");put( "ATC", "I");put( "ATA", "I");put( "ATG", "M");
                put("GTT", "V");put( "GTC", "V");put( "GTA", "V");put( "GTG", "V");
                put("TCT", "S");put( "TCC", "S");put( "TCA", "S");put( "TCG", "S");put( "AGT", "S");put( "AGC", "S");
                put("CCT", "P");put( "CCC", "P");put( "CCA", "P");put( "CCG", "P");
                put("ACT", "T");put( "ACC", "T");put( "ACA", "T");put( "ACG", "T");
                put("GCT", "A");put( "GCC", "A");put( "GCA", "A");put( "GCG", "A");
                put("TAT", "Y");put( "TAC", "Y");
                put("TAA", "*");
                put("TAG", " ");put( "TGA", " ");
                put("CAT","H");put( "CAC","H");
                put("CAA","Q");put( "CAG","Q");
                put("AAT","N");put( "AAC","N");
                put("AAA","K");put( "AAG","K");
                put("GAT","D");put( "GAC","D");
                put("GAA","E");put( "GAG","E");
                put("TGT","C");put( "TGC","C");
                put("TGG","W");
                put("CGT","R");put( "CGC","R");put( "CGA","R");put( "CGG","R");put("AGA","R");put( "AGG","R");
                put("GGT","G");put( "GGC","G");put( "GGA","G");put( "GGG","G");}};
        // todo: implement translation of sequence
        for (int i = 0; i < sequence.length() - 2; i = i + 3) {
            String triplet = sequence.substring(i, i + 3);
            buf.append(dict.get(triplet));
        }
        return buf.toString();
    }
}
