import java.util.ArrayList;

public class Graph {

	Node [][] nodes;
	ArrayList<Edge> edges = new ArrayList<Edge>();
	
	double probability;
	
	int average;
	
	//Constructor with width and depth
	public Graph (int width, int depth, Colony[] homes, double sugarProbability, int avgSugar){
		RandomUtils ran = new RandomUtils();
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
				if(j-1 >= 0)
					edges.add(new Edge(nodes[j][k],nodes[j-1][k]));
				if(j + 1 <= width)
					edges.add(new Edge(nodes[j][k], nodes[j+1][k]));
				if(k-1 >= 0)
					edges.add(new Edge(nodes[j][k], nodes[j][k-1]));
				if(k+1 <= depth)
					edges.add(new Edge(nodes[j][k], nodes[j][k+1]));
				
				
			}
		}
		
		
		
	}

}