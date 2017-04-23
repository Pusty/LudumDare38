package me.game.entity;

public class Hole extends Entity{
	public Hole(int x, int z) {
		super(x, z);
	}
	int tick = 0;
	public String getTextureName() {
		tick = tick + 1;
		if(tick >= 30)tick=0;
		return "hole_"+(tick/10);
	}

}
