import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
public class Singles2 {
	private PriorityQueue<Integer> queueOfSingles;//most single groups are first, gives numbers of singles
	private int[] nOfSingles;// the past nOfSingles
	private double div;
	private static final int MAX_HASH = 10000;//it is n
	private static LinkedList<Integer>[] hash;//this gives a list of men that have almost the same amount of singles
	private long nMax;
	static {
		hash = new LinkedList[MAX_HASH];
		for(int i = 0; i < MAX_HASH; i++)
			hash[i] = new LinkedList<Integer>();
	}
	
	
	public Singles2(int[] n) {
		this.nOfSingles = n;
		this.nMax = 0;
    	for(int i = 0; i < nOfSingles.length; i++)
    		nMax += nOfSingles[i];
		
    	
		//this.div = (double)n.length/(k+0.01);
    	//long start = System.nanoTime();
    	
		for(int i = 0; i < nMax+1; i++)
			hash[i].clear();
		
		for(int i = 0; i < n.length; i++)
			this.hashAddLast(i);
		//System.out.println(System.nanoTime() - start);
		this.queueOfSingles = new PriorityQueue<Integer>(Collections.reverseOrder());
		
		for(int i = 0; i < nOfSingles.length; i++)
			queueOfSingles.add(nOfSingles[i]);
		
	}
	
	public void display() {
		System.out.println("Singles =");
		for(int i = 0; i < nMax+1; i++){
			System.out.println("hash "+ i + " : " + hash[i]);
		}
		System.out.println("queueOfSingles" + queueOfSingles);
		for(int i = 0; i < nOfSingles.length; i++){
			System.out.println("nOfSingles "+ i + " : " + nOfSingles[i]);
		}
		
		System.out.println();
	}
	
	public boolean isEmpty() {
		while(queueOfSingles.peek() != null && (hash(queueOfSingles.peek()).isEmpty() || !hashContains(queueOfSingles.peek()))){
			//System.out.print("isEmpty");
			//display();System.out.println(System.nanoTime() - start);
			queueOfSingles.poll();
		}
		return queueOfSingles.peek() == null;
	}//passes to the next allowed value.
	//also create a hash only for not single dudes
	public boolean hashContains(int n) {
		for(Integer man: hash(n)) {
			if(nOfSingles[man] == n)
				return true;
		}
		return false;
	}
	public int hashFind(int n) {
		for(Integer man: hash(n)) {
			if(nOfSingles[man] == n)
				return man;
		}
		return -1;
	}
	public int n(int man) {
		return nOfSingles[man];
	}
	
	public void qRemove(int man) {
		queueOfSingles.remove(nOfSingles[man]);
	}
	
	public int nextSinglest() {
		return hashFind(queueOfSingles.poll());
	}
	
	private LinkedList<Integer> hash(int n) {
		if(hashC(n)<=nMax)
			return hash[hashC(n)];
		else 
			throw new ArrayIndexOutOfBoundsException("in hash()");
	}
	
	private LinkedList<Integer> hashGet(int man){
		
		return hash(nOfSingles[man]);
	}
	
	private int hashC(int n) {
		return n;
	}
	
	private boolean hashCodeChanges(int man, int delta) {
		return (hashC(nOfSingles[man]+delta) - hashC(nOfSingles[man]) > 0);
	}
	
	private boolean hashRemove(int man) {
		return hashGet(man).remove((Integer)man);
	}
	
	private void hashAddLast(int man) {
		
		hashGet(man).addLast(man);
		//display();
		//System.out.println(man + "," + hashGet(man));
	}
	
	public void updatePartial(int man, int delta) {
		if(delta!=0) {
			//hashRemove(man);//remove takes time, but these hashs shouldnt have many elements
			nOfSingles[man] += delta;			
			hashAddLast(man);
		}
	}
	
	public void updateFinal(int man) {
		queueOfSingles.add(nOfSingles[man]);
	}
}