package assignment06;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * find occurrences of queries in a text
 * Sequence Bioinformatics, WS 22/23
 * FindQueries_YOUR_NAME 11.22
 */
public class FindQueries_YOUR_NAME {
    public static void main(String[] args) throws IOException {
        System.out.println(FindQueries_YOUR_NAME.class.getSimpleName());

        if (args.length != 2)
            throw new IOException("Usage: FindQueries_YOUR_NAME text queries");

        var textItems = assignment01.FastA_Xu_Straub.read(args[0]);
        if (textItems.size() != 1)
            throw new IOException("text must contain 1 sequence, found: " + textItems.size());

        System.out.println("Text: " + textItems.get(0).sequence());

        var suffixTree = new assignment06.NaiveSuffixTree(textItems.get(0).sequence());

        var queryItems = assignment01.FastA_Xu_Straub.read(args[1]);

        for (var item : queryItems) {
            System.out.println("Query " + item.sequence());
            System.out.println("Contained: " + contains(suffixTree, item.sequence()));
            System.out.print("Occurrences:");
            for (var pos : find(suffixTree, item.sequence())) {
                System.out.print(" " + pos);
            }
            System.out.println();
        }
    }

    /**
     * determines whether text contains query
     *
     * @param suffixTree the suffix tree representing the text
     * @param query      the query
     * @return true, if query in text
     */
    public static boolean contains(NaiveSuffixTree suffixTree, String query) {
        // todo: please implement this
        ArrayList<NaiveSuffixTree.Node> rootChildren = suffixTree.getChildren(suffixTree.getRoot());
        return checkQueryRecursive(rootChildren, query);
    }

    static boolean checkQueryRecursive(Collection<NaiveSuffixTree.Node> children, String query) {
        boolean same = false;
        for (NaiveSuffixTree.Node node : children) {
            String edgeLabel = node.getLetters();
            if (query.length() <= edgeLabel.length()) {
                if (edgeLabel.startsWith(query)) {
                    return true;
                }
            } else {
                if (query.startsWith(edgeLabel)) {
                    same = checkQueryRecursive(node.getChildren(), query.substring(edgeLabel.length()));
                    if (same) {
                        break;
                    }
                }
            }
        }
        return same;
    }

    /**
     * find and return all occurrences of query in text
     *
     * @param suffixTree the suffix tree representing the text
     * @param query      the query
     * @return all positions in text at which query occurs
     */
    public static Collection<Integer> find(NaiveSuffixTree suffixTree, String query) {
        // todo: please implement this
        ArrayList<NaiveSuffixTree.Node> rootChildren = suffixTree.getChildren(suffixTree.getRoot());
        return indexQueryRecursive(rootChildren, query);
    }

    static Collection<Integer> indexQueryRecursive(Collection<NaiveSuffixTree.Node> children, String query) {
        ArrayList<Integer> indices = new ArrayList<>();
        for (NaiveSuffixTree.Node node : children) {
            String edgeLabel = node.getLetters();
            if (query.length() <= edgeLabel.length()) {
                if (edgeLabel.startsWith(query)) {
                    indices.addAll(getChildrenIndices(node));
                }
            } else {
                if (query.startsWith(edgeLabel)) {
                    indices.addAll(indexQueryRecursive(node.getChildren(), query.substring(edgeLabel.length())));
                }
            }
        }
        Collections.sort(indices);
        return indices;
    }

    static Collection<Integer> getChildrenIndices(NaiveSuffixTree.Node node) {
        ArrayList<Integer> indices = new ArrayList<>();
        if (node.getChildren().isEmpty()) {
            indices.add(node.getSuffixPos());
        } else {
            for (NaiveSuffixTree.Node child : node.getChildren()) {
                indices.addAll(getChildrenIndices(child));
            }
        }
        return indices;
    }
}
