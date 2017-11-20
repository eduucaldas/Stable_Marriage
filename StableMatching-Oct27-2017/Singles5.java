import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.HashMap;

public class Singles5 {
	private PriorityQueue<Pair> queueOfSingles;//most single groups are first, gives numbers of singles
	private int[] nOfSingles;// the past nOfSingles
	private static final int MAX_HASH = 10000;//it is n
	private long nMax;
	
	
	public Singles5(int[] n) {
		this.nOfSingles = n;
		this.nMax = 0;
    	for(int i = 0; i < nOfSingles.length; i++)
    		nMax += nOfSingles[i];
		
    	
		this.queueOfSingles = new PriorityQueue<Pair>(Collections.reverseOrder());
		
		for(int i = 0; i < nOfSingles.length; i++)
			queueOfSingles.add(new Pair(i,nOfSingles[i]));
		
	}
	
	private boolean isValidEl(Pair p) {
		return nOfSingles[p.man] == p.nSingles;
	}
	
	public boolean isEmpty() {
		while(!queueOfSingles.isEmpty() && !isValidEl(queueOfSingles.peek())){
			//be careful here
			queueOfSingles.poll();
		}
		return queueOfSingles.peek() == null;
	}//passes to the next allowed value.
	
	public int nextSinglest() {
		return queueOfSingles.poll().man;
	}
	
	public int n(int man) {
		return nOfSingles[man];
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
			nOfSingles[man] += delta;
		}
	}
	
	public void updateFinal(int man) {
		queueOfSingles.add(new Pair(man, nOfSingles[man]));
	}
	
	
}