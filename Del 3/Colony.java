/**
*@supervisor Andreas Rosenstjerne
*@author Andreas Rosenstjerne, Kasper Beider og Frederik Dam.
*@version 1.0
*/

public class Colony extends Node{

	int stock;
	
	public Colony() {
		this.stock = 0;
	}
	
	public boolean hasStock(){
		return (stock>0);
	}
	
	public void consume(){
		this.stock = this.stock - 1;
	}
	
	public void topUp(int sugar){
		this.stock += sugar;
	}
}