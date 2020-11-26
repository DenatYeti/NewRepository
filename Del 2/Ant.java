/**
*@Supervisor Kasper Beider
*@author Andreas Rosenstjerne, Kasper Beider og Frederik Dam
*A program that simulates an ant that has a home colony, along with its positions.
*@version 1.3
*/
public class Ant{
	private int sugar;
	private Colony home;
	
	private Node current,
				 previous;
	
	public Ant (Colony home){
		sugar = 0;
		this.home = home;
		current = home;
		previous = home;
	}
	
	public boolean carrying(){
		return (sugar >= 1);
	}
	
	public boolean isAtHome(){
		return (home==current);
	}
	public boolean wasAtHome(){
		return (home == previous);
	}
	
	public void dropSugar(){
			this.sugar = 0;
	}
	// precondition: There exists an edge between the two nodes
	public void move(Node location){
		previous = current;
		current = location; 
	}
	
	// picks up suger if the the current node has sugar and the ant isnt carrying sugar.
	public void pickUpSugar(){
		if((current.sugar()>0) || !(carrying())){
			this.sugar = this.sugar + 1;
		}
	}
	
	// Getters for the attributes.
	public Colony home(){
		return home; 
	}
	
	public Node current(){
		return current;
	}
	
	public Node previous(){
		return previous;
	}
}