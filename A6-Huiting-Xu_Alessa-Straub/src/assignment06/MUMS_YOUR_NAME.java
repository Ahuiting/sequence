package assignment06;

import java.io.IOException;
import java.util.*;

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
        var text = textItems.get(0).sequence() + "%" + textItems.get(1).sequence();
        var suffixTree = new assignment06.NaiveSuffixTree(text);
        // 2. implement algorithm to report all MUMs (of any size)
        HashMap<String, ArrayList> map = recursive(suffixTree.getRoot(), "");
        ArrayList<String> list = new ArrayList<>(map.keySet());
        for (String k1 : map.keySet()) {
            for (String k2 : map.keySet()) {
                if (k1 != k2) {
                    if (k1.contains(k2)) {
                        list.remove(k2);
                    }
                    if (k2.contains(k1)) {
                        list.remove(k1);
                    }
                }
            }
        }
        for (String key:list) {
            ArrayList<Integer>index=map.get(key);
            Collections.sort(index);
            int start1 = index.get(0) + 1;
            int start2 = index.get(1) - textItems.get(0).sequence().length() - 1 + 1;
            System.out.println("MUM \""+key+"\" at "+start1 + " and " + start2);

        }

        System.out.println("for input " + textItems.get(0).sequence() + " and " + textItems.get(1).sequence());
        // output should be:
        // MUM "GC" at 2 and 2 (1-based)
        // for input AGCT and GGCC
    }

    static HashMap recursive(NaiveSuffixTree.Node node, String s) {
        HashMap<String, ArrayList> map = new HashMap();
        List<NaiveSuffixTree.Node> nodeArrayList = new ArrayList(List.of(node.getChildren().toArray()));
        if (nodeArrayList.size() == 2 && nodeArrayList.get(0).getChildren().isEmpty() && nodeArrayList.get(1).getChildren().isEmpty()) {
            if ((nodeArrayList.get(0).getLetters().contains("%") && (!nodeArrayList.get(1).getLetters().contains("%")))
                    || (!nodeArrayList.get(0).getLetters().contains("%")) && nodeArrayList.get(1).getLetters().contains("%")) {
                map.put(s + node.getLetters(), new ArrayList<>() {{
                    add(nodeArrayList.get(0).getSuffixPos());
                    add(nodeArrayList.get(1).getSuffixPos());
                }});
            }

        } else {
            for (NaiveSuffixTree.Node child : node.getChildren()) {
                String s1 = s + node.getLetters();
                HashMap<String, ArrayList> recursiveResult = recursive(child, s1);
                for (String k : recursiveResult.keySet()) {
                    map.put(k, recursiveResult.get(k));
                }
            }
        }
        return map;
    }
}
