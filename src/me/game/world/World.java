package me.game.world;

import me.game.entity.Entity;

public class World {
	Entity[] entities;
	public World() {
		entities = new Entity[25];
	}
	public void addEntity(Entity e) {
		for(int i=0;i<entities.length;i++)
			if(entities[i]==null) {
				entities[i] = e;
				return;
			}
	}
	public void removeEntity(Entity e) {
		for(int i=0;i<entities.length;i++)
			if(entities[i]!=null&&entities[i].hashCode()==e.hashCode()) {
				entities[i] = null;
				return;
			}
	}
	public int countEntityArray() {
		int index = 0;
		for(int i=0;i<entities.length;i++)
			if(entities[i]!=null)
				index++;
		return index;
	}
	public Entity[] getEntityArray() {
		return entities;
	}
	
	

}
