import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;

public class Graph {
	
	RandomUtils ran = new RandomUtils();
	Node [][] nodes;
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	double probability;
	
	int average;
	
	
	//Constructor with a file
	/*
	*Constructor that takes a file 
	*Precondition: the number of homes in the file has to be the same as the number of Colony homes. 
	*/
	public Graph (String file, Colony [] homes, double sugarProbability, int avgSugar){
		this.probability = sugarProbability;
		this.average = avgSugar;
		
		String [] array = new String[0];
		try {
		File myObj = new File(file);
		Scanner myReader = new Scanner(myObj);  
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			array = add(array, data);
		}
		myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred when trying to find the file.");
			System.exit(0);
		}
		
		int numberOfNodes = Integer.parseInt(array[0]);
		
		//Creates and places the colonies in the specified places from the file. 
		this.nodes = new Node[Integer.parseInt(array[0])][1];
		String [] colonies = array[1].split(" ");
		int i = 0;
		while(i < homes.length && i < colonies.length){
			this.nodes [Integer.parseInt(colonies[i])-1][0] = homes[i];
			i = i + 1;
		}
		
		//Creates nodes the places where there arent any colonies. 
		for(int j = 0; j < numberOfNodes; j++){
			if (nodes [j][0] == null){
				if (ran.coinFlip(probability))
					nodes [j][0] = new Node(ran.randomPoisson(average));
				else 
					nodes [j][0] = new Node();
			}
		}
		
		//Loop that adds edges.
		//Takes one line from the file at a time and add an edge between the nodes. 
		for(int k = 2; k < array.length; k++){
			String [] edgesFromFile = array[k].split(" ");
			
			edges.add(new Edge(nodes[Integer.parseInt(edgesFromFile[0])-1][0], nodes[Integer.parseInt(edgesFromFile[1])-1][0]));
			edges.add(new Edge(nodes[Integer.parseInt(edgesFromFile[1])-1][0], nodes[Integer.parseInt(edgesFromFile[0])-1][0]));
		}
		
	}
	
	
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
	
	/*
	*Raises the pheromone level in the edge between the source and target nodes given as parameters.
	*Increases by the amount also given as parameters. 
	*/
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
		isTrue = false; 
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
	
	/*
	*Decreases the pheromone level in all edges with pheromone level > 0.
	*Determines whether there should spawn sugar, and if so where. 
	*/
	public void tick(){
		int amount = 0,
			i = 0,
			j = 0;
			
		boolean placedSugar = false; 
		
		for(int x = 0; x < edges.size() ; x++){
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
	
	/*
	*Returns a Node array containing all the nodes adjacent to the node given as argument.
	*/
	public Node [] adjacentTo(Node node){
		
		Node [] adjacent = new Node[0];
		
		int i = 0;
		
		while (i < edges.size()){
			if (edges.get(i).source() == node){
				adjacent = add(adjacent, edges.get(i).target());
			}
			i = i + 1;
		}
		
		return adjacent;
		
	}
	
	//Private method that adjacentTo method uses. 
	private Node [] add (Node [] adjacent, Node node){
		
		Node[] newArray = new Node[adjacent.length+1]; // Creates new node array, of length +1
		
		for (int i = 0; i < adjacent.length; i++){ // Loops through the given array, and adds its content to the new node array
			newArray[i] = adjacent[i];
		}
		newArray [adjacent.length] = node; // Adds a the new node to the array.
		
		return newArray;
	}
	
	
	//private method that is used to read from a file and add each line to an array. 
	private String [] add (String [] array, String data){
		
		String[] newArray = new String [array.length+1]; // Creates new node array, of length +1
		
		for (int i = 0; i < array.length; i++){ // Loops through the given array, and adds its content to the new node array
			newArray[i] = array[i];
		}
		newArray [array.length] = data; // Adds a the new node to the array.
		
		return newArray;
	}

}