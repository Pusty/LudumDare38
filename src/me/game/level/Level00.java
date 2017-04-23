package me.game.level;

import me.game.dialog.Dialog;
import me.game.entity.Planet;
import me.game.entity.Player;
import me.game.entity.Sun;
import me.game.main.Engine;
import me.game.world.World;

public class Level00 extends Level{
	public Level00() {}
	
	public int getIndex() {
		return 0;
	}
	public boolean done(Engine e) {
		if(e.getWorld().countEntityArray()==1) return true;
		return false;
	}
	public void load(Engine e) {
		strokes=0;
		Dialog dialog = new Dialog("empty", new String[] {"Try to destroy the other worlds","Hint: Press Enter to hit your planet", "Press R to restart"}, null);
		e.setDialog(dialog);
		e.setPlayer(new Player(-16,-8));
		World world = new World();
		world.addEntity(new Planet(16,-8, 1));
		world.addEntity(new Sun(48,-8));
		e.setWorld(world);
	}

	@Override
	public int minStrokes() {
		return 1;
	}
}
