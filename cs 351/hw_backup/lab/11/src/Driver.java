import edu.uwm.cs351.BSTExample;


public class Driver {

	public static void main(String[] args) { 
		BSTExample e = new BSTExample();
		if(e.printInOrderTraversal()) {
			System.out.println("In-order traversal complete!");
		} else {
			System.out.println("In-order traversal incomplete");
		}
	}
}
