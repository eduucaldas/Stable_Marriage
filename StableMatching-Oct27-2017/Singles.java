import java.util.PriorityQueue;
import java.util.LinkedList;
import java.util.Collection;
import java.util.Collections;
public class Singles {
	private LinkedList<Integer>[] hash;//this gives a list of men that have almost the same amount of singles
	private PriorityQueue<Integer> queueOfSingles;//most single groups are first, gives numbers of singles
	private int[] nOfSingles;// the past nOfSingles
	private double div;
	static final int MAX_HASH = 1000000000;//it is n
	
	public Singles(int[] n) {
		this.nOfSingles = n;
		int k = 0;
    	for(int i = 0; i < nOfSingles.length; i++)
    		k += nOfSingles[i];
		
    	this.hash = new LinkedList[n.length];
		this.div = (double)n.length/(k+0.01);
		
		for(int i = 0; i < n.length; i++)
			hash[i] = new LinkedList<Integer>();

		for(int i = 0; i < n.length; i++)
			this.hashAddLast(i);
		
		this.queueOfSingles = new PriorityQueue<Integer>(Collections.reverseOrder());
		
		for(int i = 0; i < nOfSingles.length; i++)
			queueOfSingles.add(nOfSingles[i]);
	}
	
	public void display() {
		System.out.println("Singles =");
		for(int i = 0; i < hash.length; i++){
			System.out.println("hash "+ Math.ceil(i*div) +"-"+ Math.ceil((i+1)*div) + " : " + hash[i]);
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
			//display();
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
		if(hashC(n)<hash.length)
			return hash[hashC(n)];
		else 
			throw new ArrayIndexOutOfBoundsException("in hash()");
	}
	
	private LinkedList<Integer> hashGet(int man){
		return hash(nOfSingles[man]);
	}
	
	private int hashC(int n) {
		return (int)(n*div);
	}
	
	private boolean hashCodeChanges(int man, int delta) {
		return (hashC(nOfSingles[man]+delta) - hashC(nOfSingles[man]) > 0);
	}
	
	private int hashRemove(int man) {
		return hashGet(man).remove(man);
	}
	
	private void hashAddLast(int man) {
		hashGet(man).addLast(man);
		//display();
		//System.out.println(man + "," + hashGet(man));
	}

	
	public void updatePartial(int man, int delta) {
		if(hashCodeChanges(man, delta)) {
			hashRemove(man);//remove takes time, but these hashs shouldnt have many elements
			nOfSingles[man] += delta;
			hashAddLast(man);
		}
		else
			nOfSingles[man] += delta;
	}
	
	public void updateFinal(int man) {
		queueOfSingles.add(nOfSingles[man]);
	}
}
