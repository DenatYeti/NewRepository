import java.util.Scanner;

/**
* RunSimulation program, that makes use of other classes.
* @author Andreas Rosenstjerne,
* Kasper Beider,
* Frederik Dam.
* @version 2.0
*/

public class RunSimulation{
	// We declare some variables and the scanner here, as statics, to allow any part of the program to acces them. We assign default values to avoid errors.
	static Scanner in = new Scanner(System.in); // Declaring the scanner so that we can have user inputs.
	static int input = 0,
			   ticks = 0,
			   steps = 0, 
			   capacity = 0,
			   pheromones = 0;
			   
	static Colony [] colonies = {};
	
	static Graph graph = new Graph(3,3, colonies ,0,0);
	
	static Ant [] ants = {};
	
	static Node n = new Node(); //This node is only used to initialize the base visualizer and nothing more.
	static Visualizer vis = new Visualizer(graph, false, n, ants);

	public static void main (String [] args){
		init();
		runMenu();
	}
	
	
	//A simple method that prints the menu on to the screen 
	public static void menu(){
		System.out.println("\nIt is now possible to either run the simulation with the current values, to check the values and/or change them.\n");
		System.out.println("1) To run the program. If visualization was chosen, you need to close the visualizer window to close.");
		System.out.println("2) To change the variables.");
		System.out.println("0) Close program.");
	}
	
	//The method that both calls the menu, and takes a user input and uses it to either run the simulation, call init to assign new values, or close the program
	public static void runMenu(){
		int i = 0, 
			menuInput = 0; //The menu options.
		do{
			menu();
			System.out.print("Your choice: ");
			menuInput = in.nextInt(); //Takes a input from the user and assigns it to this variable
			switch(menuInput) {
			
			case 1:
				Simulator sim = new Simulator(graph, ants, capacity, pheromones); //Constructs the simulation
				
				switch (input) { //Switch case that uses the user input, 
				case 0: 
					i = 0;
					vis.display(); //Displays the visualizer
					do{ //Updates visualizer and displays it, then runs a tick for both sim and graph thus moving the simulation along
						vis.update(); //Updates the visualizer
						sim.tick(); // Moves the simulation one tick
						i = i+1;
					}while(i<=ticks);
					break;
				case 1: 
					i = 0;
					do{//Takes the visualizers text output and gives it to the user as text along with a numeration of ticks.
						if(i%steps == 0){//This if, takes care of when to give a output, and when not to.
							vis.printStatus();//This function prints the text output of the visualizer.
							System.out.println("Tick " + i + "/" + ticks + "\n"); // Text to tell the user how many steps we have done.
							in.nextLine(); // This allows the user to choose when the next step is played
						}
						sim.tick();
						i = i+1;
					}while(i<=ticks);
					break;
	
				default: 
					System.out.println("There was an error with an input");// A standard just in case of an error happening
					break;
				}
				break;
				
			case 2: 
				init();	// Calls the initializer again to reassign values.
				break;
				
			case 0://Closing option.
				System.out.println("Closing program");
				break;
				
			default:
				System.out.println("Please choose a valid input"); // In case of invalid inputs.
				break;
			}
		}while(menuInput != 0); // Runs until 0 is chosen, but because of the visualizer this gets overridden when the visualizer is viewed.
	}

	// init is a function that initializes the program, and asks the user for inputs for the different values
	public static void init() {
		int i = 0,
			j = 0, 
			k = 0,
			width = 0,
			depth = 0, 
			numberOfAnts = 0, 
			sugarAverage = 0,
			numberOfColonies = 0;
		double sugarProbability = 0;	
		String fil = "";
		boolean isTrue;
		
		System.out.println("Please input the number of colonies. Must be atleast 1");
		System.out.println("If you want to use a file make sure you input the number of colonies specified in the file.");
		System.out.print("Total colonies: ");
		numberOfColonies = in.nextInt(); //Recieves and stores the number of colonies from the user.
	
		colonies = new Colony[numberOfColonies]; //We initialize the array colonies here so that we can use the amount to create the correct amount of spaces.
		
		//Loop that generates colonies and stores them in the array colonies.
		while(i<numberOfColonies){
			colonies [i] = new Colony();
			i = i + 1;
		}
		
		numberOfAnts = 0; // This prevents confusion in case the user wants to change the values. 
		ants = new Ant[0]; // Failsafe to avoid problems when choosing new values
		//This loop, goes through the different colonies, and asks the user for an amount of ants that needs to assign j as their home
		while (j < numberOfColonies){ 
			System.out.println("\nWrite a number of ants for colony " + j);
			System.out.print("Number of ants: ");
			numberOfAnts = numberOfAnts + in.nextInt();
				while (k < numberOfAnts){ //This loop adds an ant, using the addAnt method.
					ants = addAnt(ants, colonies[j]);
					k = k + 1;
				}
			j = j + 1;
		}

		do{	
			System.out.println("\nDo you want to load a file or chose the dimensions for the graph?");
			System.out.println("If width or depth is less than 3 visuals will not be applied with the visualizer");
			System.out.println("input 0 for a file and 1 to chose your own dimensions");
			System.out.print("Your input: ");
			input = in.nextInt(); 

			switch (input) {
			
				case 0: 
					System.out.println("\nPlease input the name of the file you want to use.");
					System.out.println("The file must specify the same number of colonies as chosen."); // reminding the user of the condition.
					System.out.print("File name: ");
					in.nextLine();
					fil = in.nextLine();
					isTrue = false; 
					break;
					
				case 1: 
					System.out.println("\nPlease input the width of the graph");
					System.out.print("Width = ");
					width = in.nextInt();
					System.out.println("\nPlease input the Depth of the graph");
					System.out.print("Depth = ");
					depth = in.nextInt(); 
					isTrue = false;
					break;
				
				default: 
					System.out.println("\nThat is not a valid option");
					isTrue = true;
					break;
			}
		}while (isTrue);
			
		System.out.println("\nChoose a percentage for the spawnrate of sugar");
		System.out.print("spawnrate: ");
		sugarProbability = in.nextDouble(); 

		System.out.println("\nChoose the average amount of sugar to spawn per node");
		System.out.print("Average sugar: ");
		sugarAverage = in.nextInt(); 
		
		System.out.println("\nFor how many ticks do you want the simulation to run?");
		System.out.print("Number of ticks: ");
		ticks = in.nextInt();

		System.out.println("\nHow much sugar do you want the ants to be able to carry? ");
		System.out.print("Your choice: ");
		capacity = in.nextInt();
		
		System.out.println("\nHow many pheromones do you want the ants to leave behind? ");
		System.out.print("Your choice: ");
		pheromones = in.nextInt();
			
		//We seperate the constructors for the graph and visualizer, by some ifs that represent the 3 cases 
		//A file was chosen, the depth and/or width is less than 3, or the depth and width are above 3
		if (input == 0){
			graph = new Graph(fil, colonies, sugarProbability, sugarAverage);
			vis = new Visualizer(graph, false, ants[0].home(), ants); 	
			//This secures that we can initialize the visualizer since we cant confirm if the graph is a grid or not based on the chosen file
		}else if (width < 3 || depth < 3 ) { //This statement checks whether we have atleast a 3 by 3 since if we have less it makes no sence to call it a grid
			graph = new Graph(width, depth, colonies, sugarProbability, sugarAverage);
			vis = new Visualizer(graph, false, ants[0].home(), ants); 
			//Since we can't make a grid when either of these are 1, we again set it to false during this else if.
		}else{
			graph = new Graph(width, depth, colonies, sugarProbability, sugarAverage);
			vis = new Visualizer(graph, true, ants[0].home(), ants); 
			//This is the only case where we can be certain of a grid.
		}

		do{ 
			System.out.println("\nIt is possible to get the simulation visualized or give through text in intervals of the steps\n" +
			"For visualization choose 0.\nFor text in steps, choose 1.");
			System.out.print("Your choice: ");
			input = in.nextInt();

			switch (input)	{
	
			case 0: //If this option is chosen nothing more needs to be done
				isTrue = false; 
				break;
				
			case 1://If this option is chosen the program needs another input from the user.
				System.out.println("\nChoose an amount of steps between the updates");
				System.out.print("Your choice: ");
				steps = in.nextInt();		
				isTrue = false;
				break;
				
			default: 
				System.out.println("\nPlease choose one of the options");
				isTrue = true;
				break;
			}
		}while(isTrue); // We use isTrue here, since it wouldnt make sense to have a case that closes the loop and nothing else. Since we also
						// need the input to be remembered so that we can run either the loop for text or the one for visuals, this is the best option.
	}
	
		
	/** A method, that creates a new array of 1 lenght larger than the given array, and adds the content of the given array to it along with a new ant
	*@param an array of type Ant[], and a conlony that becomes the home of the new ant.
	*@returns an array Ant[] of lenght +1 with an additional ant.
	*/
	public static Ant[] addAnt (Ant[] array, Colony colo){
		
		Ant[] newArray = new Ant[array.length+1]; // Creates new ant array, of lenght +1
		
		for (int i = 0; i < array.length; i++){ // Loops through the given array, and adds its content to the new ant array
			newArray[i]= array[i];
		}
		newArray [array.length] = new Ant(colo); // Adds a new ant to the array.
		return newArray;
	}
	
}