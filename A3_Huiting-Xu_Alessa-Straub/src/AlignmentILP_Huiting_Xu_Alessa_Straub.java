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
		if (args.length != 1 && args.length!=2) {
			throw new IOException("Usage: AlignmentILP_YOUR_NAME input [output]");
		}

		var list = FastA_Huiting_Xu_Alessa_Straub.read(args[0]);

		if (list.size() != 3) {
			throw new IOException("Input file must contain 3 sequences, found: " + list.size());
		}

		// todo: setup and write out ILP based on extended alignment graph, with match score =  4 and mismatch score = 1

		try(var w=(args.length ==2?new FileWriter(args[1]):new OutputStreamWriter(System.out))) {
			w.write("max: ");
			// 1. write the objective function: loop over all pairs of sequences and all pairs of letters
			ArrayList<String> help = new ArrayList();
			for (int i = 0; i < list.size(); i++) {
				for (int j = i+1; j < list.size(); j++) {
					for (int n = 0; n < list.get(i).sequence().length(); n++) {

					for (int m= 0; m < list.get(j).sequence().length(); m++) {
						help.add((i)+String.valueOf(n)+"_"+String.valueOf(j)+String.valueOf(m));
					}}
				}

			}


			// 2. write out all the simple mixed cycle constraints between any two sequences
			int count = 0;
			for (int i = 0; i < help.size(); i++) {
				for (int j = i+1; j < help.size(); j++) {
					System.out.print(help.get(i)+" ");
					count++;
					System.out.println(help.get(j));
				}

			}


			// 3. write out all the simple mixed cycle constraints between any three sequences

			// 4. write out the binary variable constraints

			// 5. specify all variables as integers
		}
	}

}
