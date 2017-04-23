package me.game.level;

import me.game.entity.Planet;
import me.game.entity.Player;
import me.game.entity.Sun;
import me.game.main.Engine;
import me.game.world.World;

public class Level02 extends Level{
	public Level02() {}
	
	public int getIndex() {
		return 2;
	}
	public boolean done(Engine e) {
		if(e.getWorld().countEntityArray()==3) return true;
		return false;
	}
	public void load(Engine e) {
		strokes=0;
		e.setPlayer(new Player(-8,-8));
		World world = new World();
		world.addEntity(new Planet(16,-8, 3));
		world.addEntity(new Planet(34,-18, 2));
		world.addEntity(new Planet(34,2, 2));
		world.addEntity(new Sun(48+16,-8-16-8));
		world.addEntity(new Sun(48,-8));
		world.addEntity(new Sun(48+16,-8+16+8));
		e.setWorld(world);
	}

	@Override
	public int minStrokes() {
		return 2;
	}
}
