public class Simulator{
	
	private final Graph graph;
	
	private Ant[] ants;
	
	private final int carriedSugar,
					  droppedPheromones;

	/**
	*Constructor for the class, that creates an object simulator with the given values.
	*@precondition every ants home is a node in the graph.
	*/
	public Simulator(Graph graph, Ant[] ants, int carriedSugar, int droppedPheromones){
		
		this.graph = graph;
		this.ants = ants;
		this.carriedSugar = carriedSugar;
		this.droppedPheromones = droppedPheromones;
	}

	
	public void tick(){
		this.graph.tick();
		RandomUtils random = new RandomUtils();
		int i = 0,
			j = 0,
			k = 0;
			
		// variables that calls a method once to avoid multiple calls of the same method.	
		boolean isHome,
				isStocked,
				isCarrying,
				wasHome,
				hasMoved;
		
		Node current,
			 previous,
			 n; 
		Node [] nodes;
	
		
		while (i < ants.length){

			if (ants[i] != null){
				isHome = ants[i].isAtHome();
				isStocked = ants[i].home().hasStock();
				wasHome = ants[i].wasAtHome();
				if (isHome && !(wasHome)){
					if (isHome && isStocked){
						ants[i].home().consume();
						ants[i].move(ants[i].current()); 
					}else if(isHome && !(isStocked)){
						this.ants[i] = null;
					}
				}
			}
			i = i + 1; 	 
		}
		//loop that determines the actions of the ant
		while (j < ants.length){
			if (ants[j] != null){
				isCarrying = ants[j].carrying(); // updating value to avoid multiple calls.
				current = ants[j].current();
				previous = ants[j].previous();
				nodes = graph.adjacentTo(ants[j].current());
				if(!(isCarrying) && current.sugar() > 0){
					ants[j].pickUpSugar(); 
					current.decreaseSugar();
					ants[j].move(ants[j].previous());
				}else if(nodes.length == 1){ //incase of only one adjacent node, move to that node.
					ants[j].move(nodes[0]);
				}else{
					hasMoved = false;
					k = 0;
					while(!(hasMoved) && k < nodes.length){
						n = nodes[k];
						if(n != previous && random.coinFlip(formula(n, current, nodes))){
							ants[j].move(n);
							hasMoved = true;
						}else if(k == nodes.length-1){ // when having reached the final index and not yet satisfied the move reset k and start over.
							k = 0; 
						}else{
							k = k + 1;						
						}
						
						if (hasMoved){ // seperate thing that is only active when an ant moved.
							graph.raisePheromones(current, ants[j].current(), droppedPheromones);
						}
					}
				}
			
			if (ants[j].isAtHome() && ants[j].carrying()){ // checks the precondition. if true drop sugar into the colony.
					ants[j].dropSugar();
					ants[j].home().topUp(carriedSugar);
					}
					}
			j = j + 1;
		}
		
		
	}
	
	private double formula(Node n, Node current, Node [] nodes){
		double numerator = graph.pheromoneLevel(current, n) + 1,
			   denominator = 0,
			   probability = 0;
		int i = 0;
		
		while(i < nodes.length){
			denominator = denominator + graph.pheromoneLevel(current, nodes[i]) + 1;
			i = i + 1;
		}
		probability = numerator / denominator;
		return probability;
	}
}