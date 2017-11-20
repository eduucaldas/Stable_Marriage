import java.util.PriorityQueue;
import java.util.stream.Collectors;
import java.util.ArrayDeque;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;

public class StableMatching implements StableMatchingInterface {

    //private int[] nOfSingleMen;
    private int[] nOfSingleGirls;
    //private PriorityQueue<Pair> queueOfSingleMen;//gonna be used to pick the singlest group. This will be difficult to implement
    private ArrayDeque<Integer>[] priorityMen;// only update is popping when we analyse a new man
    private int[][] priorityMen4Women;//list inversion of women Prefs, will stay unchanged. gives for G, M, the priority of M in G`s view, 0 being the best
    private int[][] mar;
    private PriorityQueue<Integer>[] pHusbands;//gives the priority of the husbands G is linked to. bigger priorities first
    static final private int MAX_CAPACITY = 10000;
    private Singles5 singlesMen;
    
    public StableMatching(){
    	//this.nOfSingleMen = new int[MAX_CAPACITY];
    	this.nOfSingleGirls = new int[MAX_CAPACITY];
    	//this.queueOfSingleMen = new PriorityQueue<Pair>(Collections.reverseOrder());//we want many single guys first
    	this.priorityMen = new ArrayDeque[MAX_CAPACITY];
    	for(int i = 0; i < MAX_CAPACITY; i++) 
        	priorityMen[i] = new ArrayDeque<Integer>();
    	this.priorityMen4Women = new int[MAX_CAPACITY][MAX_CAPACITY];
    	this.mar = new int[MAX_CAPACITY][MAX_CAPACITY];
    	this.pHusbands = new PriorityQueue[MAX_CAPACITY];
    	for(int i = 0; i < MAX_CAPACITY; i++)
        	pHusbands[i] = new PriorityQueue<Integer>(Collections.reverseOrder());//we put bigger numbers first
    	//this.singlesMen = new Singles3();
    }

    private void askSingleGirls(int M, int G) {
    	mar[M][G] = Math.min(singlesMen.n(M), nOfSingleGirls[G]);
    	singlesMen.updatePartial(M, -mar[M][G]);
    	nOfSingleGirls[G] -= mar[M][G];
    }
    
    private void cheat(int M, int MCheated, int G, int n) {
    	mar[M][G] += n;//these guys get married
    	singlesMen.updatePartial(M, -n);
    	mar[MCheated][G] -= n;//these guys get single
    	singlesMen.updatePartial(MCheated, n);
    	singlesMen.updateFinal(MCheated);
    }
    
    public void display(String name, int[][] matrix) {
    	System.out.println(name + " = ");
    	for (int i = 0; i < matrix.length; i++) {
    	    for (int j = 0; j < matrix[i].length; j++) {
    	        System.out.print(matrix[i][j] + " ");
    	    }
    	    System.out.println();
    	}
    }
    
    
    
    public int[][] constructStableMatching (int[] menGroupCount,
								    	    int[] womenGroupCount,
								    	    int[][] menPrefs,
								    	    int[][] womenPrefs){
    	//long start = System.nanoTime();
    	int n = 0;
    	for(int i = 0; i < menGroupCount.length; i++)
    		n += menGroupCount[i];
    	//System.out.println((System.nanoTime() - start)/1000000);
    	stableMatchingInit(menGroupCount, womenGroupCount, menPrefs, womenPrefs);
        ///display("priorityMen4Women", priorityMen4Women);
        ///display("womenPrefs",womenPrefs);
    	//im gonna put a pq of number of single men in the group, so I always get the one with biggest number
    	
    	int MCheated, M, G;
    	
    	while(!singlesMen.isEmpty()) {
    		M = singlesMen.nextSinglest(); //target group of men
    		G = priorityMen[M].poll();//null if M asked every G, what shouldnt occur
    		///System.out.println("M, G: " + M + ", " + G);
    		//lets make them ask first girls that are single, less trouble. and then the girls with guys in descending order
    		if(nOfSingleGirls[G]>0) askSingleGirls(M,G);//should update mar, nOfSingleMen, nOfSingleGirls,
    		///singlesMen.display();
    		
    		
    		
    		//else men will try to create some divorces
    		//here we pass through the divorces in the order of the most difficult
    		//p is the priority from which the girls are gonna change their husbands for M
    		int pM = priorityMen4Women[G][M];
    		if(singlesMen.n(M) == 0) {
    			if(mar[M][G] > 0)//if M and G created any links add M
        			pHusbands[G].add(pM);
    			continue;
    		}
    		///System.out.println(pM + "," + pHusbands[G].peek());
    		while(pHusbands[G].peek()!=null && pHusbands[G].peek() > pM){
    			MCheated = womenPrefs[G][pHusbands[G].peek()];
    			///System.out.println("MCheated" + MCheated);
    			if(singlesMen.n(M) < mar[MCheated][G]) {
					cheat(M, MCheated, G, singlesMen.n(M));
					break;
				}
				else {
					cheat(M, MCheated, G, mar[MCheated][G]);
					pHusbands[G].poll();//beacause in this case Mcheated has no more links with G
				}
    		}
    		if(singlesMen.n(M) > 0)
    			singlesMen.updateFinal(M);
    		if(mar[M][G] > 0)//if M and G created any links add M
    			pHusbands[G].add(pM);
    		///singlesMen.display();
    	}
    	///singlesMen.display();
    	///display("mar", mar);
    	return mar;
    }

	private void stableMatchingInit(int[] menGroupCount, int[] womenGroupCount, int[][] menPrefs, int[][] womenPrefs) {
    	long start = System.nanoTime();

		int m = menGroupCount.length;
    	int w = womenGroupCount.length;
    	//System.out.println((System.nanoTime() - start)/1000000);
        this.nOfSingleGirls = womenGroupCount;
        this.mar = new int[m][w];

        //System.out.println((System.nanoTime() - start)/1000000);
        for(int i = 0; i < m; i++) {
        	priorityMen[i].clear();
        	for(int j = 0; j < w; j++) {
        		priorityMen[i].add(menPrefs[i][j]);
        	}
        }
        ///System.out.println((System.nanoTime() - start)/1000000);
        for(int i = 0; i < w; i++)
        	pHusbands[i].clear();
        //System.out.println((System.nanoTime() - start)/1000000);
        this.priorityMen4Women = new int[w][m];//list inversion of women Prefs, will stay unchanged. gives for G, M, the priority of M in G`s view, 0 being the best
        for(int i = 0; i < m; i++) {
        	for(int j = 0; j < w; j++)
        		priorityMen4Women[j][womenPrefs[j][i]] = i;
        }
    	//System.out.println((System.nanoTime() - start)/1000000);

        this.singlesMen = new Singles5(menGroupCount);
        //System.out.println((System.nanoTime() - start)/1000000);

        
	}
}
