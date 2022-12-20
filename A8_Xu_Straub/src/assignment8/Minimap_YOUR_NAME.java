package assignment8;

import java.io.IOException;
import java.util.*;

/**
 * proof-of-concept implementation for the basic minimap algorithm
 * Sequence Bioinformatics, WS 22/23
 * Minimap_YOUR_NAME, 12.22
 */
public class Minimap_YOUR_NAME {
	/**
	 * run the basic minimap algorithm
	 * @param args commandline arguments
	 * @throws IOException if arguments are incorrect or sequences not found
	 */
	public static void main(String[] args) throws IOException {
		int w;
		int k;
		ArrayList<FastA_Huiting_Xu_Alessa_Straub.Pair> targets;
		ArrayList<FastA_Huiting_Xu_Alessa_Straub.Pair> queries;
		if(args.length==2) {
			w=10;
			k=15;
			targets=FastA_Huiting_Xu_Alessa_Straub.read(args[0]);
			queries=FastA_Huiting_Xu_Alessa_Straub.read(args[1]);
		}
		else if(args.length==4) {
			 w=Integer.parseInt(args[0]);
			 k=Integer.parseInt(args[1]);
			 targets= FastA_Huiting_Xu_Alessa_Straub.read(args[2]);
			 queries=FastA_Huiting_Xu_Alessa_Straub.read(args[3]);
		}
		else
			throw new IOException("Usage: Minimap_YOUR_NAME [w k] targets-file queries-file");


		if(true) { // this tests whether hashing and reverse-complement methods work as expected
			var testSequence= "CACGGTAGA";
			var hash=h(sk(testSequence, 1, 5, 0));
			if (hash != 107)
				throw new RuntimeException("Hashing broken, expected 107, got: "+hash);
			var hashReverseComplement=h(sk(testSequence, 1, 5, 1));
			if (hashReverseComplement != 91)
				throw new RuntimeException("Hashing reverse-complement broken, expected 91, got: "+hashReverseComplement);
		}

		System.err.printf(Minimap_YOUR_NAME.class.getSimpleName() + " w=%d k=%d targets=%d queries=%d%n", w,k,targets.size(),queries.size());

		var targetIndex=computeTargetIndex(targets,w,k);

		for(var record:queries) {
			var query= record.sequence();
			System.err.println("\nQuery: "+record.header());
			var matches=mapQuerySequence(targetIndex,query,w,k,500);
			System.err.println("Matches: "+matches.size());
			for(var match:matches) {
				System.err.println(("Target: %d, query: %d - %d, target: %d - %d, reverse: %d"
						.formatted(match.t()+1,match.qMin()+ 1, match.qMax()+ 1,match.tMin()+ 1, match.tMax()+ 1,match.r())));
				System.err.println(query.substring(match.qMin(), match.qMax()));
				var target=targets.get(match.t()).sequence();
				System.err.println(sk(target,match.tMin(),match.tMax()-match.tMin(), match.r()));
			}
		}
	}

	/**
	 * extracts the k-mer at given position  (as described in script)
	 * @param sequence the DNA sequence
	 * @param pos the position
	 * @param k the k-mer size
	 * @param r 0 for forward and 1 for reverse complement
	 * @return k-mer at given position or its reverse complement
	 */
	public static String sk(String sequence,int pos,int k,int r) {
		if(r==0)
			return sequence.substring(pos,pos+k);
		else {
			var buf=new StringBuilder();
			String dna = sequence.substring(pos,pos+k);
			for (int i = dna.length() - 1; i >= 0; i--) {
				char c = dna.charAt(i);
				if( c=='A') { buf.append('T');}
				else if(c=='T') {  buf.append('A');}
				else if(c =='G') {  buf.append('C');}
				else if(c =='C') {  buf.append('G');}
				else if(c =='-') {  buf.append('-');}
				else if(c =='N') {  buf.append('N');}
				else buf.append("");
			}
			return buf.toString();
		}
	}

	/**
	 * computes the h value for a k-mer (as described in script)
	 * @param s DNA string of length k
	 * @return h value
	 */
	public static int h(String s) {
		var value = 0;
		char[] chars = s.toCharArray();
		for (int i = 0; i < s.length(); i++) {
			switch (chars[i]) {
				case 'A':
					value = value +(0 *(int)Math.pow(4,s.length()-i-1)) ;
					break;
				case 'T':
					value = value + (3 * (int)Math.pow(4,s.length()-i-1));
					break;
				case 'G':
					value = value + (2 *(int)Math.pow(4,s.length()-i-1));
					break;
				case 'C':
					value = value + (1 *(int)Math.pow(4,s.length()-i-1));
					break;
				default:
					break;
			}}
		return value;
	}

	/**
	 * computes a minimizer sketch for a given sequence and parameter k (algorithm 1 in the script)
	 * @param s the DNA sequence
	 * @param w the word size
	 * @param k the k-mer size
	 * @return sorted set of all minimizers
	 */
	public static Set<Minimizer> minimizerSketch(String s, int w, int k) {
		var sketch=new HashSet<Minimizer>();

		// todo: implement computation of minimizer sketch as described in script (algorithm 1)
		if(s.length()>=w+k+1){
			for (int i = 1; i <= s.length()-w-k+1 ; i++) {
				int m = 1000000000;
				for (int j = 0; j < w; j++) {
					String uString = sk(s,i+j,k,0);
					String vString = sk(s,i+j,k,1);
					int u = h(uString);
					int v = h(vString);
					if (u!=v){
						m = Math.min(m,Math.min(u,v));
					}


				}
				for (int j = 0; j < w; j++) {
					String uString = sk(s,i+j,k,0);
					String vString = sk(s,i+j,k,1);
					int u = h(uString);
					int v = h(vString);
					if (u<v && u==m){
						sketch.add(new Minimizer(m,i+j,0));
					} else if (v<u && v==m) {
						sketch.add(new Minimizer(m,i+j,1));

					}


			}
		}
		}
		System.out.println(sketch);
		return sketch;
	}

	/**
	 * Compute a hash map of h-values to minimizer locations in the target sequences (algorithm 3 in the script)
	 * @param targets the target sequences
	 * @param w the word size
	 * @param k the k-mer size
	 * @return the
	 */
	public static HashMap<Integer,Set<Location>> computeTargetIndex(ArrayList<FastA_Huiting_Xu_Alessa_Straub.Pair> targets, int w, int k) {
		var targetIndex= new HashMap<Integer,Set<Location>>();
		for (int i = 0; i < targets.size(); i++) {
			var help = targets.get(i);
			String s = help.sequence();
			var M =new HashSet<Minimizer>(minimizerSketch(s, w,k));
			for ( Minimizer element :M
				 ) { Set<Location> mutableSet = new HashSet<>();
					mutableSet.add(new Location(i,element.pos, element.r));
				targetIndex.put(element.h,mutableSet);

			}
		}
		return targetIndex;
	}

	/**
	 * compute all matches of query to any of the target sequences (algorithm 4 in script)
	 * @param targetIndex the target index computed using #computeTargetIndex()
	 * @param query the query DNA sequences
	 * @param w the word size
	 * @param k the k-mer size
	 * @param epsilon the dialog shift allowed between two chained dialogs
	 */
	public static ArrayList<Match> mapQuerySequence(HashMap<Integer,Set<Location>> targetIndex, String query, int w, int k, int epsilon) {

		// compute array of k-mer hits:
		var A=new ArrayList<KMerHit>();
		var M =new HashSet<Minimizer>(minimizerSketch(query, w,k));
		for ( Minimizer element :M
		) {
			for (Location value : targetIndex.get(element.h)
			) {
				if (element.r == value.r){
					A.add(new KMerHit(value.t,0,element.pos -value.pos,value.pos));
				}
				else{
					A.add(new KMerHit(value.t,1,element.pos +value.pos,value.pos));
				}
			}

			}

		A.sort(KMerHit::compareTo);

		// chain k-mer hits into matches and return the matches
		var result=new ArrayList<Match>();
		var b=0;
		for(var e=0;e<A.size();e++) {
			if (e == A.size()-1 || A.get(e).t !=A.get(e+1).t ||A.get(e).r !=A.get(e+1).r || A.get(e+1).c - A.get(e).c>= epsilon ){
				// todo: part with C and Matches report
				var minPositionTarget = 1000000000;
				var minPositionQuery = 1000000000;
				var maxPositionTarget = -100000;
				var maxPositionQuery = -100000;
				int position = 0;
				for(var f=b;f<=e;f++) {
					if(A.get(f).r ==0){
						position = A.get(f).c + A.get(f).pos;
					} else if (A.get(f).r==1) {
						position = A.get(f).c - A.get(f).pos;

					}
					minPositionTarget= Math.min(A.get(f).pos,minPositionTarget);
					minPositionQuery = Math.min(position,minPositionQuery);
					maxPositionTarget= Math.max(A.get(f).pos,maxPositionTarget);
					maxPositionQuery = Math.max(position,maxPositionQuery);

				}
				minPositionQuery = minPositionQuery+k;
				minPositionTarget = minPositionTarget +k;
				result.add(new Match(A.get(e).t,A.get(e).r,minPositionQuery,maxPositionQuery,minPositionTarget,maxPositionTarget));
				b = e+1;
			}
			// todo: compute matches or ``clusters'' (as described in script, algorithm 4, part;s 2 and 3
		}
		return result;
	}


	// PLEASE DO NOT CHANGE ANYTHING BELOW HERE:

	/**
	 * a minimizer
	 * @param h hash value
	 * @param pos position in sequence
	 * @param r 0 for forward and 1 for reverse strand
	 */
	public static record Minimizer(int h,int pos, int r) {
	}

	/**
	 * a minimizer location
	 * @param t index of target sequence 0...T-1, with T the number of target sequences
	 * @param pos position in sequence t
	 * @param r 0 for forward and 1 for reverse strand
	 */
	public static record Location(int t,int pos,int r) {}

	/**
	 * a k-mer hit
	 * @param t is the number of the target sequence
	 * @param r is the *relative* strand (0 if both query and target minimizers on same strand, otherwise 1)
	 * @param c the dialog number
	 * @param pos the position in the target sequence
	 */
	public static record KMerHit(int t, int r, int c, int pos) implements Comparable<KMerHit>{
		@Override
		public int compareTo(KMerHit other) {
			if(t<other.t)
				return -1;
			else if(t>other.t)
				return 1;
			else if(r<other.r)
				return -1;
			else if(r>other.r)
				return 1;
			else if(c<other.c)
				return -1;
			else if(c>other.c)
				return 1;
			else return Integer.compare(pos, other.pos);
		}
	}

	/**
	 * a query-target sequence match
	 * @param t the target index
	 * @param r is the *relative* strand (0 for matches on same strand, 1 for on different strands)
	 * @param qMin minimum position of match in query
	 * @param qMax maximum position of match in query
	 * @param tMin minimum position of match in target
	 * @param tMax maximum position of match in target

	 */
	public static record Match(int t, int r, int qMin, int qMax, int tMin, int tMax) {
	}

}

