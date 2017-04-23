package me.game.level;

import me.game.entity.Hole;
import me.game.entity.Planet;
import me.game.entity.Player;
import me.game.main.Engine;
import me.game.world.World;

public class Level04 extends Level{
	public Level04() {}
	
	public int getIndex() {
		return 4;
	}
	public boolean done(Engine e) {
		if(e.getWorld().countEntityArray()==2) return true;
		return false;
	}
	public void load(Engine e) {
		strokes=0;
		e.setPlayer(new Player(8,0));
		World world = new World();
		world.addEntity(new Planet(-16,24, 2));
		world.addEntity(new Hole(-32,8));
		world.addEntity(new Planet(16,56, 2));
		world.addEntity(new Hole(-32,64));
		e.setWorld(world);
	}

	@Override
	public int minStrokes() {
		return 3;
	}
}
