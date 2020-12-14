import java.util.ArrayList;
import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;


/**
*@supervisor: Andreas Rosenstjerne 
*@author: Andreas Rosenstjerne, Kasper Beider og Frederik Dam.
*@version: 1.4
* A class that takes care of nodes and edges, so that the client classes, simulator and runsimulator can use nodes and edges easily
* both creates the nodes and edges, and keeps track of them.
*/
public class Graph {
	
	private RandomUtils ran = new RandomUtils();
	private Node [][] nodes;
	private ArrayList<Edge> edges = new ArrayList<Edge>();
	
	private double probability;
	
	private int average;
	
	
	
	/**
	*Constructor that takes a file.
	*Constructs a new graph using the information from the given file, using the given array of colonies.
	*Also spawns sugar based on the given probability and average.
	*@Precondition: the number of colonies in the file has to be the same as the number of Colony homes. 
	*/
	public Graph (String file, Colony [] homes, double sugarProbability, int avgSugar){
		this.probability = sugarProbability;
		this.average = avgSugar;
		
		//Reads from the file and uses a private method to add the data to an array.
		String [] array = new String[0];
		//try catch to avoid the program crashing without giving the user any information.
		try {
		File myObj = new File(file); //assigns the file to a variable.
		Scanner myReader = new Scanner(myObj);  //uses the scanner to acces everything from the file.
		//Loops through and adds every individual segment to an array so that we can use the information.
		while (myReader.hasNextLine()) {
			String data = myReader.nextLine();
			array = addLines(array, data);
		}
		myReader.close();
		} catch (FileNotFoundException e) {
			System.out.println("An error occurred when trying to find the file.");
			System.exit(0);
		}
		
		
		int numberOfNodes = Integer.parseInt(array[0]); //calls this here to avoid extra calls of the same unit.
		
		//Creates and places the colonies in the specified places from the file. 
		this.nodes = new Node[Integer.parseInt(array[0])][1];
		String [] colonies = array[1].split(" "); //spliting array index 1, so that each element of the new array is the position of a colony.
		int i = 0;
		while(i < homes.length && i < colonies.length){
			this.nodes [Integer.parseInt(colonies[i])-1][0] = homes[i];
			i = i + 1;
		}
		
		//Creates nodes the places where there arent any colonies. 
		//Uses the method randomUtils.coinFlip() to determine whether there should spawn sugar on each node.
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
			
			//we make two edges since this is needed for our adjacentTo method later.
			edges.add(new Edge(nodes[Integer.parseInt(edgesFromFile[0])-1][0], nodes[Integer.parseInt(edgesFromFile[1])-1][0]));
			edges.add(new Edge(nodes[Integer.parseInt(edgesFromFile[1])-1][0], nodes[Integer.parseInt(edgesFromFile[0])-1][0]));
		}
	}
	
	
	/*
	*Constructor with width and depth.
	*Constructs a new graph with the given width and depth, using the given array of colonies.
	*Also spawns sugar based on the given probability and average.
	*/
	public Graph (int width, int depth, Colony[] homes, double sugarProbability, int avgSugar){
		//declaring interators and other variables that we will need.
		int i = 0,
			x,
			y;
		//Assigning attributes.
		this.probability = sugarProbability;
		this.average = avgSugar;
		//using the width and depth to create a 2d array.
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
		// Creates edges, between the nodes that are adjacent.
		for(int j = 0; j < width; j++){
			for(int k = 0; k < depth; k++){
				//Each if statement allows us to avoid going out of bounds on each of the four sides of a node.
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
	*A method that returns the pheromone level between the to given nodes.
	*@precondition there exits an edge between the two given nodes.
	*/
	public int pheromoneLevel(Node source, Node target){
		int i = 0,
			z = 0;
		boolean isTrue = false;
		
		//A while loop that finds the egde with the given source and target.
		while (!isTrue && i < edges.size()){
			if (source == edges.get(i).source()){
				if (target == edges.get(i).target()){//When the edge is found, we save the value to a variable.
					z = edges.get(i).pheromones(); //Then stop the loop by changing isTrue.
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
	*Precondition: there has to be an edge between node source and node target. 
	*/
	public void raisePheromones(Node source, Node target, int amount){
		int i = 0,
			j = 0;
		boolean isTrue = false;
		
		//Loop from before that finds the correct edge.
		while (!isTrue && i < edges.size()){
			if (source == edges.get(i).source()){
				if (target == edges.get(i).target()){
					edges.get(i).raisePheromones(amount);
					isTrue = true;
				}
			}
			i = i+ 1;
		}
		isTrue = false; // We update isTrue so that we can run the loop again.
		//running the loop to update the other edge that is the reverse.
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
	
		int	i = 0,
			j = 0;
			
		boolean placedSugar = false,
				shouldPlaceSugar = ran.coinFlip(probability);
		
		// loop that decreases the pheromone level of all edges.
		for(int x = 0; x < edges.size() ; x++){
			if(edges.get(x).pheromones() > 0) // makes sure that we don't remove pheromones from edges with no pheromones.
				edges.get(x).decreasePheromones();
		}
		
		
		
		//adds sugar to a random node. 
		while(shouldPlaceSugar && !(placedSugar)){
			i = ran.randomInt(nodes.length);
			j = ran.randomInt(nodes[i].length);
			if (!(nodes[i][j] instanceof Colony)){
				nodes[i][j].setSugar(nodes[i][j].sugar() + ran.randomPoisson(average));
				placedSugar = true;
			}
		}
	}
	
	/**
	*Returns a Node array containing all the nodes adjacent to the node given.
	*@Precondition: Node is a node in the graph. 
	*/
	public Node [] adjacentTo(Node node){
		
		Node [] adjacent = new Node[0];
		int i = 0;
		//loops through and finds all edges that have the correct source node
		while (i < edges.size()){
			if (edges.get(i).source() == node){
				adjacent = addNode(adjacent, edges.get(i).target()); //When an edge is found, we add the target of that edge to the array.
			}
			i = i + 1;
		}
		return adjacent;
	}
	
	//Private method that adjacentTo method uses to add the adjacent nodes to the array. 
	private Node [] addNode (Node [] adjacent, Node node){
		
		Node[] newArray = new Node[adjacent.length+1]; // Creates new node array, of length +1
		for (int i = 0; i < adjacent.length; i++){ // Loops through the given array, and adds its content to the new node array
			newArray[i] = adjacent[i];
		}
		newArray [adjacent.length] = node; // Adds a the new node to the array.
		return newArray;
	}
	
	
	//private method that is used to add each line to an array. 
	private String [] addLines (String [] array, String data){
		
		String[] newArray = new String [array.length+1]; // Creates new node array, of length +1
		for (int i = 0; i < array.length; i++){ // Loops through the given array, and adds its content to the new String array
			newArray[i] = array[i];
		}
		newArray [array.length] = data; // Adds a the new string to the array.
		return newArray;
	}
}