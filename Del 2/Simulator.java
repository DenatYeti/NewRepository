/**
*@Supervisor Kasper Beider
*@author Andreas Rosenstjerne, Kasper Beider og Frederik Dam
* Provider class for run simulation that simulates ant colonies, gathering sugar, spreading pheromones, eating and dying.
* It also serves as the client class for, some of the other classes like Graph and ant.
* @version 2.4
*/

public class Simulator{
	// Declaring class attributes, some of which are final since they are not to be changed after being assigned.
	private final Graph graph;
	
	private Ant[] ants; //The only non final class, as we need to update the ants inside of the array. 
	
	private final int carriedSugar,
					  droppedPheromones;

	/**
	*Constructor for the class, that creates an object simulator with the given values.
	*@precondition Every ants home is a node in the graph.
	*/
	public Simulator(Graph graph, Ant[] ants, int carriedSugar, int droppedPheromones){
		
		this.graph = graph;
		this.ants = ants;
		this.carriedSugar = carriedSugar;
		this.droppedPheromones = droppedPheromones;
	}

	/*
	* Moves the simulation along one unit of time, updating the pheromones, as well as the status of the ants.
	* This includes moving ants.
	*/
	public void tick(){
		this.graph.tick(); //Calls the method from graph, that lowers the pheromone level on all edges that have pheromones.
		eat(); 
		moveAnt();	
	}
	
	/*
	* A Method that checks if the ants need to eat, and also kills them if they arent able.
	*/
	private void eat () {
		// Declaring some variables that we assigning later to avoid repeated calls of variables that havent changed.
		boolean isHome,
				isStocked;
		
		int i = 0;
		while (i < ants.length){ //Going through each individual ant.
			if (ants[i] != null){ //This checks if the ant is dead and skips it if it is.
				isStocked = ants[i].home().hasStock();
				if (ants[i].isAtHome() && !(ants[i].wasAtHome())){ // If ant is home and wasnt home 
					if (isStocked){ // If there is sugar in stock, the ant eats and its value is changed to make sure its wont eat again.
						ants[i].home().consume(); 
						ants[i].move(ants[i].current()); 
					}else{ // If there isnt sugar in stock we kill the ant.
						this.ants[i] = null;
					}	
				}
			}
			i = i + 1;
		}
	}
	
	
	/*
	* A method that goes through the individual ants to determine their moves.
	*/
	private void moveAnt() {
		RandomUtils random = new RandomUtils(); // Declaring an instance of randomutils for later use.
		int i = 0,
			j = 0;
		
		boolean isCarrying,
				hasMoved;
		
		Node current,
			 previous,
			 n; 
		Node [] nodes;
	
		

		//Loop that determines the actions of the ant
		while (i < ants.length){
			if (ants[i] != null){
				isCarrying = ants[i].carrying(); // Updating value to avoid multiple calls.
				current = ants[i].current();
				previous = ants[i].previous();
				nodes = graph.adjacentTo(ants[i].current());
				if(!(isCarrying) && current.sugar() > 0){ // If the ant isnt carrying but is on a node with sugar its picks it up
					ants[i].pickUpSugar(); 
					current.decreaseSugar();
					ants[i].move(ants[i].previous()); // Do a predetermined move.
					hasMoved = true;
				}else if(nodes.length == 1){ //Incase of only one adjacent node, move to that node.
					ants[i].move(nodes[0]);
					hasMoved = true;
				}else if(nodes.length == 0){ //Incase of no possible moves at all, we update its status anyways.
					ants[i].move(current);
				}else{ //This is the part that takes care of deciding which random node the ant moves to if the other specific moves cant be applied.
					hasMoved = false; //A value to stop the loop.
					j = 0;
					while(!(hasMoved) && j < nodes.length){
						n = nodes[j]; //We take the specific node we are currently checking 
						//We do the coinFlip method with the probability from the formula, but are still skipping the previous node the ant was on.
						if(n != previous && random.coinFlip(formula(n, current, nodes, previous, j))){ 
							ants[i].move(n);
							hasMoved = true; //This stops the loop early
						}else{
							j = j + 1;						
						}
						
					}
				}
				
			if (hasMoved){ //Seperate thing that is only active when an ant moved.
							graph.raisePheromones(current, ants[i].current(), droppedPheromones); //Raises the pheromone level on the edge it moved over.
						}
			if (ants[i].isAtHome() && ants[i].carrying()){ //Checks the precondition. if true drop sugar into the colony.
					ants[i].dropSugar();
					ants[i].home().topUp(carriedSugar); //Add sugar to the colony, amount is equal to the the value given when constructing simulator.
			}
			}
			i = i + 1;
		}
		
		
	}
	
		
	/*
	* A method that takes care of the calculations for the probability of the ants moving to a specific node.
	* It's parameters are numerous, as to both know the node we wish to check the move for, the current node we are on and its adjacent nodes,
	* As well as its previous node.
	*/
	private double formula(Node n, Node current, Node [] nodes, Node previous, int j){
		double numerator = graph.pheromoneLevel(current, n) + 1, // Using the graph that we already know, from the constructor, to get the pheromones.
			   denominator = 0,
			   probability = 0;
		int i = j; // Assigning a starting value.
		
		// A Loop that gathers the number needed for the denominator of the equation.
		while(i < nodes.length){
			if (nodes[i] != previous){ // We use this if to skip over the ants previous node as this node is not a possible move unless specified.
			// Starting from the node we are looking at in the array, and skipping the ants previous, means we ensure a move.
			denominator = denominator + graph.pheromoneLevel(current, nodes[i]) + 1;
			}
			i = i + 1;
		}
		probability = numerator / denominator; // Uses the two numbers to get the probablity. 
		return probability;
	}
}