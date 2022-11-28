package assignment06;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * computes all MUMs in a text
 * Sequence Bioinformatics, WS 22/23
 * MUMS_YOUR_NAME, 11.22
 */
public class MUMS_YOUR_NAME {

    public static void main(String[] args) throws IOException {
        System.out.println(MUMS_YOUR_NAME.class.getSimpleName());

        if (args.length != 1)
            throw new IOException("Usage: MUMS_YOUR_NAME fasta-file");

        var textItems = assignment01.FastA_Xu_Straub.read(args[0]);
        if (textItems.size() != 2)
            throw new IOException("fasta-file must contain 2 sequence, found: " + textItems.size());

        // todo: please implement

        // 1. setup suffix tree for appropriately concatenated sequences
        var text = textItems.get(0).sequence().substring(0, textItems.get(0).sequence().length() - 1) + "%"
                + textItems.get(1).sequence();
        var suffixTree = new assignment06.NaiveSuffixTree(text);
        // 2. implement algorithm to report all MUMs (of any size)

        System.out.println(recursive(suffixTree.getRoot()));
        // output should be:
        // MUM "GC" at 2 and 2 (1-based)
        // for input AGCT and GGCC
    }

    static ArrayList recursive(NaiveSuffixTree.Node node) {
        ArrayList arrayList = new ArrayList();
        List<NaiveSuffixTree.Node> nodeArrayList = new ArrayList(List.of(node.getChildren().toArray()));
        if (nodeArrayList.size() == 2 && nodeArrayList.get(0).getChildren().size() == 0 && nodeArrayList.get(1).getChildren().size() == 0) {
            if (nodeArrayList.get(0).getLetters().contains("%") && nodeArrayList.get(0).getLetters().contains("%")) {
                return new ArrayList();
            } else {
                arrayList.add(nodeArrayList.get(0).getSuffixPos());
                arrayList.add(nodeArrayList.get(1).getSuffixPos());
            }

        } else {
            for (NaiveSuffixTree.Node child : node.getChildren()) {
                arrayList.addAll(recursive(child));
            }
        }
        return arrayList;
    }
}
