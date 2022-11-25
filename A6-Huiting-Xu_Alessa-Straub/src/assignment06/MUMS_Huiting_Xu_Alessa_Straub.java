package assignment06;

import assignment01.FastA_Huiting_Xu_Alessa_Straub;

import java.io.IOException;

/**
 * computes all MUMs in a text
 * Sequence Bioinformatics, WS 22/23
 * MUMS_YOUR_NAME, 11.22
 */
public class MUMS_Huiting_Xu_Alessa_Straub {

	public static void main (String[] args) throws IOException {
		System.out.println(MUMS_Huiting_Xu_Alessa_Straub.class.getSimpleName());

		if (args.length != 1)
			throw new IOException("Usage: MUMS_YOUR_NAME fasta-file");

		var textItems = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);
		if (textItems.size() != 2)
			throw new IOException("fasta-file must contain 2 sequence, found: " + textItems.size());

		// todo: please implement

		// 1. setup suffix tree for appropriately concatenated sequences

		// 2. implement algorithm to report all MUMs (of any size)

		// output should be:
		// MUM "GC" at 2 and 2 (1-based)
		// for input AGCT and GGCC
	}
}
