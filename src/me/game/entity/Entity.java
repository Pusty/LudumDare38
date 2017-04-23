package me.game.entity;

import me.game.main.Engine;
import me.game.render.RawAnimation;
import me.game.util.LocationFloat;


public class Entity{
	
	LocationFloat location;
	RawAnimation animation=null;
	float sizex;
	float sizez;
	boolean init=false;

	public Entity(int x,int z){
		location=new LocationFloat(x,z);
	}
	
	public void playAnimation() {
		
	}


	public LocationFloat getLocation(){
		return location;
	}
	public void setLocation(LocationFloat l) {
		location.set(l);
	}
	public float getX(){
		return location.getX();
	}
	public float getZ(){
		return location.getZ();
	}

	
	String img=null;
	
	
	public String getTextureName() {
		return "empty";
	}
	
	public String getImage() {
		if(img!=null)
			return img;
		return getTextureName();
	}
	public void setAnimation(RawAnimation a){
		if(a!=null)
			animation=a.getWorkCopy();
		else
			animation=null;
	}
	public boolean isAnimationNull(){
		return animation==null;
	}
	public void initTick(Engine engine,int ind) {
		if(!init) {
			//INIT HERE
			init = true;
		}
	}
	public void renderTick(Engine engine,int ind){
		if(animation!=null) {
			String img = animation.getFrame();
			if(img!=null)
			setImage(img);
			else
			{setAnimation(null);setDefault();}
		}else if(img!=null)
			setDefault();
	}
	
	public void setDefault(){
		img=null;
	}
	
	public void setImage(String i) {
		img = i;
	}
	
	
}
