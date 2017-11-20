import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;

public class Singles3 {
	private PriorityQueue<Pair> queueOfSingles;//most single groups are first, gives numbers of singles
	private HashMap<Integer, Integer> nOfSingles;// the past nOfSingles
	private static final int MAX_HASH = 10000;//it is n
	//private static HashSet<Integer>[] hash;//this gives a list of men that have almost the same amount of singles
	private long nMax;
	
	
	public Singles3(int[] n) {
		this.nOfSingles = new HashMap<Integer, Integer>();
		for(int i = 0; i < n.length; i++) {
			nOfSingles.put(i, n[i]);
		}
		this.nMax = 0;
    	for(int i = 0; i < nOfSingles.size(); i++)
    		nMax += nOfSingles.get(i);
		
    	
		//this.div = (double)n.length/(k+0.01);
    	//long start = System.nanoTime();
    	
		//System.out.println(System.nanoTime() - start);
		this.queueOfSingles = new PriorityQueue<Pair>(Collections.reverseOrder());
		
		for(int i = 0; i < nOfSingles.size(); i++)
			queueOfSingles.add(new Pair(i,nOfSingles.get(i)));
		
	}
	
	private boolean isValidEl(Pair p) {
		return nOfSingles.get(p.man) == p.nSingles;
	}
	
	public boolean isEmpty() {
		while(!queueOfSingles.isEmpty() && !isValidEl(queueOfSingles.peek())){
			//be careful here
			queueOfSingles.poll();
		}
		return queueOfSingles.peek() == null;
	}//passes to the next allowed value.
	//also create a hash only for not single dudes
	
	public int nextSinglest() {
		return queueOfSingles.poll().man;
	}
	
	public int n(int man) {
		return nOfSingles.get(man);
	}
	/*
	public void display() {
		System.out.println("Singles =");
		for(int i = 0; i < nMax+1; i++){
			System.out.println("hash "+ i + " : " + hash[i]);
		}
		System.out.println("queueOfSingles" + queueOfSingles);
		for(int i = 0; i < nOfSingles.size(); i++){
			System.out.println("nOfSingles "+ i + " : " + nOfSingles.get(i));
		}
		
		System.out.println();
	}*/
	
	public void updatePartial(int man, int delta) {
		if(delta!=0) {
			//hashRemove(man);//remove takes time, but these hashs shouldnt have many elements
			nOfSingles.put(man, nOfSingles.get(man)+delta);
		}
	}
	
	public void updateFinal(int man) {
		queueOfSingles.add(new Pair(man, nOfSingles.get(man)));
	}
	
	
}