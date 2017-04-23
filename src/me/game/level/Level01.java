package me.game.level;

import me.game.entity.Planet;
import me.game.entity.Player;
import me.game.entity.Sun;
import me.game.entity.WallZ;
import me.game.main.Engine;
import me.game.world.World;

public class Level01 extends Level{
	public Level01() {}
	
	public int getIndex() {
		return 1;
	}
	public boolean done(Engine e) {
		if(e.getWorld().countEntityArray()==2) return true;
		return false;
	}
	public void load(Engine e) {
		strokes=0;
		e.setPlayer(new Player(16,-26));
		World world = new World();
		world.addEntity(new Planet(16,-8, 2));
		world.addEntity(new WallZ(32,-8, 16));
		world.addEntity(new Sun(48,-8));
		e.setWorld(world);
	}

	@Override
	public int minStrokes() {
		return 4;
	}
}
