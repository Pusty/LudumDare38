package me.game.entity;



public class Player extends Ball {

	public Player(int x, int y) {
		super(x, y);
	}
	
	
	public String getTextureName() {
		return "planet_0";
	}
	
	
	public boolean hasDirections() { return true; }

	
}


