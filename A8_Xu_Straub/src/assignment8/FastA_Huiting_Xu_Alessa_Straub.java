package assignment8;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;

/**
 * FastA_Huiting_Xu_Alessa_Straub
 * Author(s): Huiting Xu and Alessa Straub
 * Sequence Bioinformatics, WS 22/23
 */
public class FastA_Huiting_Xu_Alessa_Straub {

    public static void write(Collection<Pair> list, String fileName) throws IOException {
        try (var w = (fileName != null ? new FileWriter(fileName) : new OutputStreamWriter(System.out))) {
            // todo: write out pairs in FastA format here
            for (Pair pair : list) {
                w.write(pair.header + '\n' + pair.sequence + '\n');
            }
        }
    }

    public static ArrayList<Pair> read(String fileName) throws IOException {
        var list = new ArrayList<Pair>();
        try (var r = new BufferedReader(new FileReader(fileName))) {
            String header = "", seq = "";
            String line;
            while ((line = r.readLine()) != null) {
                if (line.startsWith(">")) {
                    if (!seq.equals("")) {
                        list.add(new Pair(header, seq));
                    }
                    header = line;
                    seq = "";
                } else {
                    seq += line.replace(" ", "");
                }
            }
            if (!header.equals("")) {
                list.add(new Pair(header, seq));
            }
        }
        return list;

    }

    /**
     * a FastA record consisting of a pair of header and sequence
     */
    public static record Pair(String header, String sequence) {
    }

}
