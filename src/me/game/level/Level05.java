package me.game.level;

import me.game.entity.Hole;
import me.game.entity.Planet;
import me.game.entity.Player;
import me.game.entity.WallZ;
import me.game.main.Engine;
import me.game.world.World;

public class Level05 extends Level{
	public Level05() {}
	
	public int getIndex() {
		return 5;
	}
	public boolean done(Engine e) {
		if(e.getWorld()==null)return false;
		if(e.getWorld().countEntityArray()==3) return true;
		return false;
	}
	public void load(Engine e) {
		strokes=0;
		e.setPlayer(new Player(8,0));
		World world = new World();
		world.addEntity(new WallZ(-8,32, 16));
		world.addEntity(new WallZ(24,32, 16));
		world.addEntity(new Planet(-8-20,32, 4));
		world.addEntity(new Planet(24+20,32, 5));
		world.addEntity(new Hole(8,64));
		e.setWorld(world);
	}

	@Override
	public int minStrokes() {
		return 5;
	}
}
