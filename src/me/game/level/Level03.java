package me.game.level;

import me.game.entity.Hole;
import me.game.entity.Planet;
import me.game.entity.Player;
import me.game.entity.WallX;
import me.game.main.Engine;
import me.game.world.World;

public class Level03 extends Level{
	public Level03() {}
	
	public int getIndex() {
		return 1;
	}
	public boolean done(Engine e) {
		if(e.getWorld().countEntityArray()==2) return true;
		return false;
	}
	public void load(Engine e) {
		strokes=0;
		e.setPlayer(new Player(8,-26));
		World world = new World();
		world.addEntity(new Planet(16,-8, 2));
		world.addEntity(new WallX(32,16, 16));
		world.addEntity(new Hole(64,-8-16));
		e.setWorld(world);
	}

	@Override
	public int minStrokes() {
		return 1;
	}
}
