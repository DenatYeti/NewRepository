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
			
		double probability;
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
		
		//alle myrer dør
		//måske lave en counter som springer dette over på første tick.
		
		while (i < ants.length){
			isHome = ants[i].isAtHome();
			isStocked = ants[i].home().hasStock();
			wasHome = ants[i].wasAtHome();
			if (ants[i] != null){
				if (isHome && !(wasHome)){
					if (isHome && isStocked){
						this.ants[i].home().consume();
					}else if(isHome && !(isStocked)){
						this.ants[i] = null;
					}
				}
			}
			i = i + 1; 	 
		}
		//loop that determines the actions of the ant
		while (j<ants.length){
			if (ants[j] == null){
				j = j + 1;
			}else{
				isCarrying = ants[j].carrying(); // updating value to avoid multiple calls.
				current = ants[j].current();
				previous = ants[j].previous();
				nodes = graph.adjacentTo(ants[j].current());
				if(!(isCarrying) && current.sugar() > 0){
					ants[j].pickUpSugar(); 
					current.decreaseSugar();
					ants[j].move(ants[j].previous());
					if (ants[j].isAtHome() && ants[j].carrying()){ // checks the precondition. if true drop sugar into the colony.
						ants[j].dropSugar();
					}
				}else if(nodes.length == 1){ //incase of only one adjacent node, move to that node.
					ants[j].move(nodes[0]);
				}else{
					hasMoved = false;
					while(k < nodes.length && !(hasMoved)){
						n = nodes[k];
						probability = probability + formula(n,previous);
						if(random.coinFlip(probability) && n != previous){
							ants[j].move(n);
							hasMoved = true;
						}else if(k = (nodes.length-1) && n != previous){
								ants[j].move(n);
						}else if(k = (nodes.length-1) && n == previous){
								ants[j].move(nodes[k-1]);
						}else{
							k = k + 1;						
						}
					}
					
				}
			}
		}
		
		
	}
	
	private double formula(Node n);
	

}