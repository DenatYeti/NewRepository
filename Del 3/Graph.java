import java.util.ArrayList;

public class Graph {
	
	RandomUtils ran = new RandomUtils();
	Node [][] nodes;
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	double probability;
	
	int average;
	
	//Constructor with width and depth
	public Graph (int width, int depth, Colony[] homes, double sugarProbability, int avgSugar){
		
		int i = 0,
			x,
			y;
			
		this.probability = sugarProbability;
		this.average = avgSugar;
		this.nodes = new Node [width][depth];
		
		// inputs colonies randomly.
		while (i < homes.length){
			x = ran.randomInt(width);
			y = ran.randomInt(depth);
			if (nodes [x][y] == null){
				nodes [x][y] = homes[i];
				i = i + 1;
			}
		}
		
		//  Creates the remaining nodes.
		for(int j = 0; j < width; j++){
			for(int k = 0; k < depth; k++){
				if (nodes [j][k] == null){
					if (ran.coinFlip(probability))
						nodes [j][k] = new Node(ran.randomPoisson(average));
					else 
						nodes [j][k] = new Node();
				}
			}
		}
		// creating edges
		for(int j = 0; j < width; j++){
			for(int k = 0; k < depth; k++){
				if(j - 1 >= 0)
					edges.add(new Edge(nodes[j][k], nodes[j-1][k]));
				if(j + 1 < width)
					edges.add(new Edge(nodes[j][k], nodes[j+1][k]));
				if(k - 1 >= 0)
					edges.add(new Edge(nodes[j][k], nodes[j][k-1]));
				if(k + 1 < depth)
					edges.add(new Edge(nodes[j][k], nodes[j][k+1]));
				
			}
		}	
		
	}
	
	/**
	*@precondition there exits an edge between the two given nodes
	*/
	public int pheromoneLevel(Node source, Node target){
		int i = 0,
			z = 0;
		boolean isTrue = false;
		
		while (!isTrue && i < edges.size()){
			if (source == edges.get(i).source()){
				if (target == edges.get(i).target()){
					z = edges.get(i).pheromones();
					isTrue = true;
				}
			}
			i = i+ 1;
		}
		return z;
	}
	
	public void raisePheromones(Node source, Node target, int amount){
		int i = 0,
			j = 0;
		boolean isTrue = false;
		
		while (!isTrue && i < edges.size()){
			if (source == edges.get(i).source()){
				if (target == edges.get(i).target()){
					edges.get(i).raisePheromones(amount);
					isTrue = true;
				}
			}
			i = i+ 1;
		}
		while (!isTrue && j < edges.size()){
			if (target == edges.get(j).source()){
				if (source == edges.get(j).target()){
					edges.get(j).raisePheromones(amount);
					isTrue = true;
				}
			}
			j = j + 1;
		}
	}
	
	public void tick(){
		int amount = 0,
			i = 0,
			j = 0;
			
		boolean placedSugar = false; 
		
		for(int x = 0; x < edges.size() ; i++){
			edges.get(x).decreasePheromones();
		}
		
		while(ran.coinFlip(probability) && !(placedSugar)){
			i = ran.randomInt(nodes.length);
			j = ran.randomInt(nodes[i].length);
			if (!(nodes[i][j] instanceof Colony)){
				amount = ran.randomPoisson(average);
				nodes[i][j].setSugar(nodes[i][j].sugar() + amount);
				placedSugar = true;
			}
		}
	}
	
	public Node [] adjacentTo(Node node){
		
		Node [] adjacent = new Node[0];
		
		int i = 0;
		
		while (i < edges.size()){
			if (edges.get(i).source() == node)
				adjacent = add(adjacent,edges.get(i).target());
			i = i + 1;
		}
		
		return adjacent;
		
	}
	
	private Node [] add (Node [] array, Node node){
		
		Node[] newArray = new Node[array.length+1]; // Creates new ant array, of lenght +1
		
		for (int i = 0; i < array.length; i++){ // Loops through the given array, and adds its content to the new ant array
			newArray[i] = array[i];
		}
		newArray [array.length] = node; // Adds a new ant to the array.
		return newArray;
	}


}