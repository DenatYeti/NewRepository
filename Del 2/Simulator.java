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
		int i = 0,
			j = 0;
		// variables that calls a method once to avoid multiple calls of the same method.	
		boolean isHome,
				isStocked,
				isCarrying;
		
		Node current; 
		
		//alle myrer dør
		//måske lave en counter som springer dette over på første tick.
		while (i < ants.length){
			isHome = ants[i].isAtHome();
			isStocked = ants[i].home().hasStock();
			if (ants[i] != null){
				if (isHome && isStocked){
					this.ants[i].home().consume();
				}else if(isHome && !(isStocked)){
					this.ants[i] = null;
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
				if(!(isCarrying) && current.sugar() > 0){
					ants[j].pickUpSugar(); 
					current.decreaseSugar();
					ants[j].move(ants[j].previous());
					if (ants[j].isAtHome() && ants[j].carrying()){ // checks the precondition. if true drop sugar into the colony.
						ants[j].dropSugar();
					}
				}else{
					//move
					
				}
			}
		}
		
		
	}
	
}