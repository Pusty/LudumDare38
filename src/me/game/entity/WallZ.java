package me.game.entity;

public class WallZ extends Entity{
	int height;
	public WallZ(int x, int z,int h) {
		super(x, z);
		height = h;
	}
	public int getHeight() {
		return height;
	}
	
	public String getTextureName() {
		return "empty";
	}

}
