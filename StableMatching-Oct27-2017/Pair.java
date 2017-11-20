
public class Pair implements Comparable{
	public Integer man;
	public Integer nSingles;
	
	public Pair(int group, int singles) {
	    this.man = group;
	    this.nSingles = singles;
	}
	@Override
	public int compareTo(Object o) {
	    Pair a2 = (Pair)o;
	    if(this.nSingles-a2.nSingles != 0)
	    	return this.nSingles-a2.nSingles;
	    else
	    	return this.man - a2.man;
	}
}
