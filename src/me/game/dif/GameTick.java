package me.game.dif;

import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;

import me.game.entity.Entity;
import me.game.entity.Ball;
import me.game.entity.Hole;
import me.game.entity.Player;
import me.game.entity.Sun;
import me.game.entity.WallX;
import me.game.entity.WallZ;
import me.game.level.LevelHandler;
import me.game.main.Engine;
import me.game.util.LocationFloat;
import me.game.util.RenderUtil;

public class GameTick extends Tick{

	float cursor_angle = 0;

	public GameTick(Engine engine) {
		super(engine);

	}
	
	@Override
	public boolean keyEvent(final Engine e,int type,int keycode) {
		Player player = e.getPlayer();
		if(e.getDialog()!=null) {
			if(keycode==Keys.SPACE || keycode==Keys.ENTER) {
				if(type==0)
					e.getDialog().nextLine(e);
			}else if(keycode==Keys.W || keycode==Keys.UP) {
				if(type==0)
					e.getDialog().up(e);
			}else if(keycode==Keys.S || keycode==Keys.DOWN) {
				if(type==0)
					e.getDialog().down(e);
			}
		} else {
		switch(keycode) {
			case Keys.ENTER:
				if(type==0) {
					if(player.getLength()<=0 && e.getLevelHandler().getStatus()==LevelHandler.STATUS_RUNNING) {
						player.impulse(cursor_angle, 50, 5);
						e.getLevelHandler().getLevel().addStrokes(1);
						e.getSound().playClip("select");
					}else if(e.getLevelHandler().getStatus()==LevelHandler.STATUS_WINNING){
						e.getLevelHandler().init(e, e.getLevelHandler().getIndex()+1);
						e.getLevelHandler().setStatus(LevelHandler.STATUS_RUNNING);
						e.getSound().playClip("col");
					}else if(e.getLevelHandler().getStatus()==LevelHandler.STATUS_LOST){
						e.getLevelHandler().init(e, e.getLevelHandler().getIndex());
						e.getLevelHandler().setStatus(LevelHandler.STATUS_RUNNING);
						e.getSound().playClip("col");
					}
				}
				return true;
			case Keys.R:
				if(type == 0) {
					e.getLevelHandler().init(e, e.getLevelHandler().getIndex());
					e.getLevelHandler().setStatus(LevelHandler.STATUS_RUNNING);
					e.getSound().playClip("des");
				}
//			case Keys.N: if(type==0) player.setAngle(player.getAngle()+10f); break;
//			case Keys.M: if(type==0) player.setAngle(player.getAngle()-10f); break;
			}
		}
		return false;
	}
	
	
	@Override
	public void tick(Engine e,float delta) {
		if(ticks>0)
			ticks=ticks-(1*delta*30*e.getTickSpeed());
		if(e.getLevelHandler().getStatus()!=LevelHandler.STATUS_RUNNING) {
			if(e.getLevelHandler().getIndex()==5 && e.getLevelHandler().getStatus()==LevelHandler.STATUS_WINNING) {
					e.setGameStatus(2);
			}
			return;
		}
		
		e.getLevelHandler().checkDone(e);
		Player player = e.getPlayer();
		if(player.getHealth()<=0) {
			e.getLevelHandler().setStatus(LevelHandler.STATUS_LOST);
			e.getSound().playClip("des");
			return;
		}
		player.tickTraveled(e,delta);
		checkCollision(e, player);
		
		
		for(int entityIndex=0;entityIndex<e.getWorld().getEntityArray().length;entityIndex++) {
			Entity entity = e.getWorld().getEntityArray()[entityIndex];
			if(entity == null) continue;
			if(!(entity instanceof Ball)) continue;
			if(((Ball) entity).getHealth() <=0) {
				e.getWorld().removeEntity(entity);
				e.getSound().playClip("des");
			}
			((Ball)entity).tickTraveled(e, delta);
			checkCollision(e, ((Ball)entity));
		}
		
		
		
		if(Tick.esc && ticks<=0) {
			e.setGameStatus(0);
			ticks=10;
		}
	}
	
	public static void checkCollision(Engine e, Ball c) {
		if(c.getLength()<=0)return;
		if(c.getCollision()>0)return;
		for(int entityIndex=0;entityIndex<e.getWorld().getEntityArray().length;entityIndex++) {
			Entity entity = e.getWorld().getEntityArray()[entityIndex];
			if(entity == null) continue;
			if(entity.hashCode()==c.hashCode()) continue;
			if(checkEntityCollision(e, c, entity)) {
				innerCollision(e, c, entity);
			}
		}
		if(c.hashCode() != e.getPlayer().hashCode())
			if(checkEntityCollision(e, c, e.getPlayer())) {
				innerCollision(e, c, e.getPlayer());
			}
		
		float distance = (float) LocationFloat.getDistance(c.getLocation(), new LocationFloat(0,0));
		if(distance >= 128) {
			c.setAngle(new LocationFloat(0-c.getX(), 0-c.getZ()).angle(new LocationFloat(1,0)));
		}
	}
	private static void innerCollision(Engine e, Ball c, Entity entity) {
		if(entity instanceof Ball) {
			LocationFloat anglePos = new LocationFloat(entity.getX()-c.getX(), entity.getZ()-c.getZ());
			float angle = anglePos.angle(new LocationFloat(1, 0));
			((Ball)entity).impulse(angle, c.getLength(), c.getSpeed());
			c.stop();
//			c.setHealth(c.getHealth()-1);
//			((EntityLiving)entity).setHealth(((EntityLiving)entity).getHealth()-1);
			e.getSound().playClip("col");
		}else if(entity instanceof WallX) {
			c.setAngle(180-(c.getAngle()+180));
			e.getSound().playClip("col");
		}else if(entity instanceof WallZ) {
			c.setAngle(180-c.getAngle());
			e.getSound().playClip("col");
		}else if(entity instanceof Sun) {
			c.setHealth(0);
			c.stop();
		}else if(entity instanceof Hole) {
			if(LocationFloat.getDistance(entity.getLocation(), c.getLocation()) < 16) {
				c.setHealth(0);
				c.stop();
			}else {
				c.impulse(entity.getLocation().sub(c.getLocation()).angle(new LocationFloat(1,0)), 5, 1f);
			}
		}
	}
	private static boolean checkEntityCollision(Engine e, Ball c, Entity a ) {
		final int radius = 8;
		LocationFloat cPoint = c.getLocation();
		//RADIUS radius
		if(a instanceof Ball
				) {
			LocationFloat aPoint = a.getLocation();
			float distance = (float) LocationFloat.getDistance(aPoint, cPoint);
			if(distance <= radius+radius) return true;
		}else if(a instanceof Sun) {
			LocationFloat aPoint = a.getLocation();
			float distance = (float) LocationFloat.getDistance(aPoint, cPoint);
			if(distance <= radius+radius) return true;
		}else if(a instanceof Hole) {
			LocationFloat aPoint = a.getLocation();
			float distance = (float) LocationFloat.getDistance(aPoint, cPoint);
			if(distance <= radius*4) return true;
		}else if(a instanceof WallX) {
			float wi=((WallX)a).getWidth()/2f;
			float hi = 1f;
			Vector2 inter = new Vector2();
			Vector2 cPointNew = new Vector2(c.getX()+(float)Math.cos(c.getAngle())*c.getSpeed()*c.getLength(),
											c.getZ()+(float)Math.sin(c.getAngle())*c.getSpeed()*c.getLength());
			boolean intersect = Intersector.intersectLines(new Vector2(c.getX(), c.getZ()),
										cPointNew,
					 				   new Vector2(a.getX()-wi,a.getZ()-hi),
					 				   new Vector2(a.getX()+wi,a.getZ()+hi), inter);
			if(!intersect) return false;
			float distance = Intersector.distanceLinePoint(a.getX()-wi,a.getZ()-hi, a.getX()+wi,a.getZ()+hi, c.getX(), c.getZ());
			if(distance > c.getSpeed()*radius) return false;
			Rectangle rec = new Rectangle(a.getX()-wi,a.getZ()-hi, wi*2, hi*2);
			if(Intersector.overlaps(new Circle(c.getX(), c.getZ(), radius), rec)) return true;
		}else if(a instanceof WallZ) {
			float wi = 1f;
			float hi = ((WallZ)a).getHeight()/2f;
			Vector2 inter = new Vector2();
			Vector2 cPointNew = new Vector2(c.getX()+(float)Math.cos(c.getAngle())*c.getSpeed()*c.getLength(),
											c.getZ()+(float)Math.sin(c.getAngle())*c.getSpeed()*c.getLength());
			boolean intersect = Intersector.intersectLines(new Vector2(c.getX(), c.getZ()),
										cPointNew,
					 				   new Vector2(a.getX()-wi,a.getZ()-hi),
					 				   new Vector2(a.getX()+wi,a.getZ()+hi), inter);
			if(!intersect) return false;
			float distance = Intersector.distanceLinePoint(a.getX()-wi,a.getZ()-hi, a.getX()+wi,a.getZ()+hi, c.getX(), c.getZ());
			if(distance > c.getSpeed()*radius) return false;
			Rectangle rec = new Rectangle(a.getX()-wi,a.getZ()-hi, wi*2, hi*2);
			if(Intersector.overlaps(new Circle(c.getX(), c.getZ(), radius), rec)) return true;
		}
		return false;
	}
	
	
	public boolean intersectRect(int x1,int y1,int w1,int h1,int x2,int y2,int w2,int h2) {
		  return !(x2 > x1+w1 || 
		           x2+w2 < x1 || 
		           y2+h2 > y1 ||
		           y2 < y1+h1);
	}

	@Override
	public void genericMouse(Engine e,int type,int screenX, int screenY, int pointer, int button){
		LocationFloat mouseLocation = new LocationFloat(screenX-engine.getCamera().viewportWidth/2,screenY-engine.getCamera().viewportHeight/2);
    	if(type==5) {
//    		LocationFloat norm = LocationFloat.getNorm(mouseLocation);
    		if(e.getLevelHandler().getStatus()==LevelHandler.STATUS_RUNNING) {
    		cursor_angle = mouseLocation.angle(new LocationFloat(1, 0));
    		}
    	}
	}

	@Override
	public void mouse(Engine e,int screenX, int screenY, int pointer, int button) {
		if(e.getDialog()!=null) {
					e.getDialog().nextLine(e);	
		}
	}
	
	@Override
	public void render(Engine e, float delta) {
		e.getBatch().setColor(new Color(0f,0f,0f,1f));
		//May get removed later (just for debug purpose)
		e.getBatch().draw(e.getImageHandler().getImage("empty"), 0, 0,e.getCamera().viewportWidth,e.getCamera().viewportHeight);
		e.getBatch().setColor(new Color(1f,1f,1f,1f));
		
		e.getBatch().draw(e.getImageHandler().getImage("circle_border"), -20-e.getPlayerLocation().getX(), -64-e.getPlayerLocation().getZ());
		
		drawEntitys(e);
		RenderUtil.renderEntity(e, e.getPlayer());
		
//		cursor_angle
		float size = 8;
		float distance = 32+e.getPlayer().getLength()/4;
		LocationFloat distance_ = new LocationFloat(distance*(float)Math.cos(Math.toRadians(cursor_angle+180)), distance*(float)Math.sin(Math.toRadians(cursor_angle+180)));
		e.getBatch().draw(e.getImageHandler().getImage("cursor"), e.getCamera().viewportWidth/2-size/2+distance_.getX(), e.getCamera().viewportHeight/2-size/2+distance_.getZ(),4,4,size,size,1f,1f,(cursor_angle-270));
		//HUD HERE 
			if(e.getDialog()!=null){
				RenderUtil.renderDialog(e);
			}
		RenderUtil.renderText(e,e.getBatch(),new LocationFloat(0,e.getCamera().viewportHeight-16),"Strokes: "+e.getLevelHandler().getLevel().getStrokes());
		String contentString = "Minimum Strokes: "+e.getLevelHandler().getLevel().minStrokes();
		RenderUtil.renderText(e,e.getBatch(),new LocationFloat(e.getCamera().viewportWidth-6*contentString.length(),e.getCamera().viewportHeight-16),contentString);
		
		
		if(e.getLevelHandler().getStatus()!=LevelHandler.STATUS_RUNNING) {
			if(e.getLevelHandler().getStatus()==LevelHandler.STATUS_WINNING) {
				if(e.getLevelHandler().getLevel().getStrokes()<=e.getLevelHandler().getLevel().minStrokes())
					RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,24), "Minimum Strokes Reached! :D");	
				else
					RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,24), "Completed!");
				RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,0), "It took you "+e.getLevelHandler().getLevel().getStrokes()+" Stroke(s)");
				RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,-24), "Press Enter to continue");
			}else if(e.getLevelHandler().getStatus()==LevelHandler.STATUS_LOST) {
				RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,24), ":(");
				RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,0), "Please try again");
				RenderUtil.renderCentured(e, e.getBatch(), new LocationFloat(0,-24), "Press Enter to continue");				
			}
		}
	}

	@Override
	public void show() {
	}
		
	private void drawEntitys(Engine e) {
		for(int entityIndex=0;entityIndex<e.getWorld().getEntityArray().length;entityIndex++) {
			Entity entity = e.getWorld().getEntityArray()[entityIndex];
			if(entity != null)
				RenderUtil.renderEntity(e, entity);
		}
	}

	

}
