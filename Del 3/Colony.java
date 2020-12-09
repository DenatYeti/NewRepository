/**
*@supervisor Andreas Rosenstjerne
*@author Andreas Rosenstjerne, Kasper Beider og Frederik Dam.
*@version 1.0
* This class extends the superclass node, in order to add som functions and make a node that the ants can use as home
* it stores sugar in its stock, and consumes sugar when an ant is eating.
*/
public class Colony extends Node{
	//Declaring the attribute that is needed
	int stock;
	
	//Constructor for colony, that creates a new colony with no sugar in stock
	public Colony() {
		this.stock = 0;
	}
	
	//A method that checks whether or not there is sugar in the stock of this colony
	public boolean hasStock(){
		return (stock>0);
	}
	
	
	//Removes a sugar from stock
	//Precondition there is sugar in the stock of this colony
	public void consume(){
		this.stock = this.stock - 1;
	}
	
	//adds sugar to the stock of this colony
	public void topUp(int sugar){
		this.stock = this.stock + sugar;
	}
}