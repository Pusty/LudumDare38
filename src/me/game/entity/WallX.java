package me.game.entity;

public class WallX extends Entity{
	int width;
	public WallX(int x, int z,int w) {
		super(x, z);
		width = w;
	}
	public int getWidth() {
		return width;
	}
	
	public String getTextureName() {
		return "empty";
	}

}
