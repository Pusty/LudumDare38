package me.game.entity;

import me.game.main.Engine;
import me.game.util.LocationFloat;

public class Ball extends Entity {

	public Ball(int x, int z) {
		super(x, z);
		health = 3;
	}
	@Override
	public void setLocation(LocationFloat l) {
		super.setLocation(l);
	}
	
	
	
	public boolean hasDirections() { return false; }
	
	public String getImage() {
		if(img!=null)
			return img;
//		float percent = ((float)getSpeed()-getTraveled())/getSpeed();
//		int frame = Math.round(percent * 1) + walkAnimationRunned ; // frame = process * framecount
//		return getTextureName()+"_"+frame;
//		System.out.println(getTextureName());
		return getTextureName();
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
	
	public LocationFloat getAddLocation(float angle, float delta, Engine e) {
		float mul = delta*30*e.getTickSpeed() * speed;
		return new LocationFloat((float)Math.cos(Math.toRadians(angle))*mul, (float)Math.sin(Math.toRadians(angle))*mul);
	}
	
	//Get Speed 30 = 1 sec
	int health = 0;
	public void tickTraveled(Engine e,float delta) {
		if(collision>0)collision--;
		if(length>0) {
			speed = speed*0.95f;
			location = location.add(getAddLocation(angle,delta, e));
			length--;
		}else {
			speed = 0;
//			angle = 0;
		}
		if(stopThis>0)stopThis--;
		if(stopThis==0) {
			length = 0;
			speed = 0;
			stopThis=-1;
		}
		
		
	}
	public int getHealth() {
		return health;
	}
	public void setHealth(int h) {
		health = h;
	}
	public float getAngle() { return angle; }
	public void setAngle(float a) { if(a>360) a = a-360; if(a<-360)a=a+360; angle = a; }
	public float getSpeed() { return speed; }
	public int getLength() { return length; }
	int stopThis=0;
	public void stop() {
		stopThis=1;
	}
	public int getCollision() { return collision; }
	public void setCollision(int c) { collision = c; }
	float angle = 0;
	float speed = 0;
	int length = 0;
	int collision = 0;
	public void impulse(float a, int l, float s) {
		LocationFloat vecA = new LocationFloat(
				((float)Math.cos(Math.toRadians(angle))) * speed, 
				((float)Math.sin(Math.toRadians(angle))) * speed);
		LocationFloat vecB = new LocationFloat(
				((float)Math.cos(Math.toRadians(a))) * s, 
				((float)Math.sin(Math.toRadians(a))) * s );
		LocationFloat vecC = vecA.add(vecB);
//		vecC = new LocationFloat(vecC.getX()/2, vecC.getZ()/2);
		LocationFloat norm = LocationFloat.getNorm(vecC);
		if(norm.getX() != 0 && norm.getZ() != 0)
			speed = ((vecC.getX()/norm.getX())+(vecC.getZ()/norm.getZ()))/2;
		else if(norm.getX() != 0)
			speed = (vecC.getX()/norm.getX());
		else if(norm.getZ() != 0)
			speed = (vecC.getZ()/norm.getZ());
		else
			speed = 0;
//		System.out.println(speed+",( "+vecC+") , ("+norm+")");
		angle = norm.angle(new LocationFloat(1,0));
//		angle = (angle%360 + a%360)/2;
//		System.out.println(angle);
//		angle = ((angle%360f)+(a%360f))/2;
		length = l;
//		speed = speed;
	}
}
