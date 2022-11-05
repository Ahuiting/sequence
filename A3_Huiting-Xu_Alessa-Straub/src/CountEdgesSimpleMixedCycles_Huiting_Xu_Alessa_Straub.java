import java.io.IOException;

/**
 * count number of edges between nucleotides in different sequences and the number of mixed cycles
 * Sequence Bioinformatics, WS22/23
 */
public class CountEdgesSimpleMixedCycles_Huiting_Xu_Alessa_Straub {
	private static int m;

	public static void main(String[] args) throws IOException {
		if(args.length!=3)
			throw new IOException("Usage: CountEdgesSimpleMixedCycles_YOUR_Name aLength bLength cLength");

		var length=new int[]{Integer.parseInt(args[0]),Integer.parseInt(args[1]),Integer.parseInt(args[2])};

		System.out.println(CountEdgesSimpleMixedCycles_Huiting_Xu_Alessa_Straub.class.getSimpleName());

		System.out.printf("Sequence lengths: %d %d %d%n", length[0],length[1],length[2]);

		// todo: report the number of edges between nucleotides in different sequences
		int count = 0;
		for (int  n = 0;  n <= 2;  n++) {
			for (int m = n+1; m <=2 ; m++) {
				for (int i = 0; i < length[n]; i++) {
					for (int j = 0; j < length[m]; j++) {
						count ++;

					}
				}

			}
			
		}
		

		var numEdgesBetweenDifferenteSequences= count;

		System.out.printf("Edges between different sequences: %d%n", numEdgesBetweenDifferenteSequences);


		var numSimpleMixedCycles=0;

		// todo: implement counting of number of simple mixed cycles

		// first compute the number of simple mixed cycles that use two cycles
		for (int  n = 0;  n <= 2;  n++) {
			for (int m = 0; m <=2 ; m++) {
				if (m!=n){
				for (int i = 0; i < length[n]; i++) {     //iterate over the number of directed edges in first sequence
					for (int j = i + 1; j < length[n]; j++) {
						for (int k = 0; k < length[m]; k++) { //iterate over the number of nodes in second sequence
							numSimpleMixedCycles++;
						}
						for (int w = 0; w < length[m]; w++) {     //iterate over the number of directed edges in first sequence
							for (int e = w+ 1; e < length[m]; e++) {
								numSimpleMixedCycles++;
							}

						}}
					}
				}
			}}


		// then compute and add the number of simple mixed cycles that use three cycles


		System.out.printf("Total simple mixed cycles: %d%n", numSimpleMixedCycles);
	}
}
