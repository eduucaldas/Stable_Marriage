import java.util.List;

public class Priority  {
	//implements a queue
	private int head;
	private int[] el;
	

	public Priority(int capacity) {
		this.head = 0;
		this.el = new int[capacity];
	}
	//implemented using an array
	public void clear() {
		this.head = 0;
		//done in O(1)
	}
	public int poll() {
		int polled = el[head];
		head++;
		return polled;
	}
	
	public void addAll(int n[]) {
		head = 0;
		el = n;
	}
	
	
	
}
