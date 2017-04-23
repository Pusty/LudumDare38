package me.game.entity;



public class Planet extends Ball {

	int number;
	
	public Planet(int x, int y, int id) {
		super(x, y);
		number = id;
//		health = 1;
	}
	
	
	public int getID() {
		return number;
	}
	public String getTextureName() {
		if(number >= 0)
			return "planet_"+number;
		return "sun";
	}
	
	
	public boolean hasDirections() { return true; }

	
}


