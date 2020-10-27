import java.util.Scanner;

public class runSimulation{

	public static void main (String [] args){

		Scanner in = new Scanner(System.in);
		System.out.println("Please input the number of colonies");
		System.out.print("Total colonies: ");
		int numberOfColonies = in.nextInt();		//modtager antal colonier fra bruger.
		nextline();	// eventuelt \n
		String fil = ("graph1.fil");
		int i = 0,
			j = 0,
			k = 0,
			input, // bruger input som genbruges
			ticks, // input som skal benyttes til at køre x antal gange af simulationen.
			steps, // bruger input. skal bruges i den sidste lykke som køre antal ticks.
			width = 0,
			depth = 0,
			numberOfAnts = 0;
			
		Colony [] colonies = new Colony[numberOfColonies];
		Ant [] ants = {};
		
		//Loop som generer colonies og indsætter dem i colonies arrayed.
		while(i<numberOfColonies){
			colonies [i] = new Colony();
			i = i + 1;
		}
		
		//Loop som laver myre til givet koloni
		while (j < numberOfColonies){
			System.out.println("write a number of ants for colony " + j);
			System.out.print("Number of ants: ");
			numberOfAnts = numberOfAnts + in.nextInt(); // modtager antal myre til en koloni fra brugeren.
			nextline();
				while (k < numberOfAnts){ //loop med følgende for k < numberOfAnts
					ants = addAnt(ants, colonies, j); // laver myrer og indsætter myren i array Ants.
					k = k + 1;
				}
			j = j + 1;
		}

		boolean isTrue = false;
		do{	
			System.out.println("Do you want to load a file or chose the dimensions for the graph?");
			nextline();
			System.out.println("input 0 for a file and 1 to chose your own dimensions");
			System.out.print("Your input: ");
			input = in.nextInt(); 
			nextline();
			switch (input) {
			
				case 0: 
					System.out.println("Please input the name of the file you want to use");
					System.out.println("File name: ");
					in.nextLine();
					fil = in.nextLine();
					isTrue = false; 
					break;
					
				case 1: 
					System.out.println("Please input the width of the graph");
					System.out.print("Width = ");
					width = in.nextInt(); //bruger input
					System.out.println("Please input the Depth of the graph");
					System.out.print("Depth = ");
					depth = in.nextInt(); //bruger input.
					isTrue = false;
					break;
				
				default: 
					System.out.println("That is not a valid option");
					nextline();
					isTrue = true;
			}
			
		}while (isTrue);
			
		System.out.println("Choose a percentage for the spawnrate of sugar");
		System.out.print("spawnrate: ");
		double sugarProbability = in.nextDouble(); // bruger input.
		nextline();
		
		System.out.println("Choose the average amount of sugar to spawn per node");
		System.out.print("Average sugar: ");
		int sugarAverage = in.nextInt(); // bruger input.
		nextline();
		
		if (input == 0){
			Graph graph = new Graph(fil, colonies, sugarProbability, sugarAverage);
		}else{
			Graph graph = new Graph(width, depth, colonies, sugarProbability, sugarAverage);
		}
		

		System.out.println("For how many ticks do you want the simulation to run?");
		System.out.print("Number of ticks: ");
		ticks = in.nextInt(); // bruger input.
		nextline();


		do{ 
			System.out.println("It is possible to get the simulation visualized or give through text in intervals of the steps\n" +
			"For visualization choose 0\nFor text in steps, choose 1");
			System.out.print("Your choice: ");
		input = in.nextInt(); // bruger input
		nextline();
		
		
		//Switch case
		switch (option)	{
	
		case 0: 
			Visualizer vis = new Visualizer(graph, true, ant1.home(), ants); // precondition: det er et grid
			isTrue = false;
			break;
		case 1 : 
			System.out.println("Choose an amount of ticks between you updates");
			System.out.print("Your choice: ");
			int steps; 
			nextline();
			isTrue = false;
			break;
		default: besked til bruger og isTrue = true;
			break;
		}while(isTrue);
		
		System.out.print("How much sugar do you want the ants to be able to carry? ");
		int capacity;
		nextline();
		
		System.out.print("How many pheromones do you want the ants to leave behind? ");
		int pheromones;
		nextline();
		
		// Simulator sim = new Simulator(graph, ants, capacity, pheromones);
		
		/*if (input = 0){
		i = 0;
		do{
			
			vis.update();
			vis.display();
			
			graph.tick();
			sim.tick();
			
			i = i+1;
			
		}while(i<=ticks);
		}
		else {
			//do while, med output, info omkring myre, sukker, colonier og eventuelle andre ting
			// gentages hver gang steps nås.
			//skal stadig køre antal ticks
		}
		*/
			

		
		
		
		
		
		
		/*
		
		Colony colo = new Colony();
		Colony colo2 = new Colony();
		Ant ant1 = new Ant(colo), ant3 = new Ant(colo), ant4 = new Ant(colo), ant5 = new Ant(colo);
		Ant ant2 = new Ant(colo2), ant6 = new Ant(colo2);

		Colony [] c = {colo, colo2};
		Ant [] a = {ant1, ant2, ant3, ant4, ant5, ant6};
		Graph graph = new Graph(6,6, c ,0.1, 100);
		
		Simulator sim = new Simulator(graph, a, 100, 10);
		Visualizer vis = new Visualizer(graph, true, ant1.home() ,a);
		
			
		int i = 0;
		System.out.println( );
		do{
			/*for (int e=0 ; e<a.length; e++){
			System.out.println(a[e].current());
			}
			System.out.println(colo.hasStock());
			
			System.out.println(colo2.hasStock());
			
			vis.update();
			vis.display();
			
			graph.tick();
			sim.tick();
			
			i = i+1;
			
		}while(i<=100);*/
		
	}
	
	public static void nextline(){
		System.out.println("");
	}
	
	public static Ant[] addAnt (Ant[] array, Colony[] colo, int j){
		
		Ant[] newArray = new Ant[array.length+1];
		
		for (int i = 0; i < array.length; i++){
			newArray[i]= array[i];
		}
		newArray [array.length] = new Ant(colo[j]);
		
		
		return newArray;
		
	}
	
	
}