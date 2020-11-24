/**
*@Supervisor Kasper Beider
*@author Andreas Rosenstjerne, Kasper Beider og Frederik Dam
*Program that creates paths/edges between nodes.
*Serves as a provider class for other classes.
*@version 1.1
*/
public class Edge {
	// declaring the class attributes
	private int pheromones;
	
	private Node source,
				 target;
	
	// Constructer with two arguments to create an edge between two nodes
	public Edge(Node source, Node target){
		
		this.source = source;
		this.target = target;
		pheromones = 0;
		
	}
	// decreases the amount of pheromones on this edge
	public void decreasePheromones(){
		if (this.pheromones > 0){
			this.pheromones = this.pheromones - 1; 
		}
	}
	
	// increases the amount of phermones on this edge
	public void raisePheromones(int amount){
		this.pheromones = this.pheromones + amount; 
	}
	
	// the three functions below are the three getters for the class attributes.
	public int pheromones(){
		return this.pheromones;
	}
	
	public Node source(){
		return this.source;
	}
	
	public Node target(){
		return this.target;
	}
	
}