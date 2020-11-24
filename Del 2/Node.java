/**
*@Supervisor Kasper Beider
*@author Andreas Rosenstjerne, Kasper Beider og Frederik Dam
* Provider class for multiple other classes
* Represents a node.
* @version 1.2
*/

public class Node{
	// declares class attributes.
	private int sugar;
	
	// Constructor with no arguments that sets a default value of sugar 
	public Node() {
		sugar = 0;
	}
	
	// Constructor with one argument that creates a node with n sugar on it.
	public Node(int n){
		sugar = n;
	}
	
	// Getter that returns the amount of sugar on the node
	public int sugar() {
		return sugar;
	}
	
	public void decreaseSugar() {
		if (this.sugar() > 0 ){
			setSugar(this.sugar()-1);
		}
	}
	
	
	// Setter that sets the amount on sugar to n
	public void setSugar(int n){
		sugar = n;
	}
}