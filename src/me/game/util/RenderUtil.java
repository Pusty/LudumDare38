package me.game.util;

import me.game.entity.Entity;
import me.game.entity.Ball;
import me.game.entity.WallX;
import me.game.entity.WallZ;
import me.game.main.Engine;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;

public class RenderUtil {
	public static void renderEntity(Engine e,Entity entity) {
		renderEntity(e,entity,entity.getX(),entity.getZ());
	}
	public static void renderEntity(Engine e,Entity entity,float x,float z) {
		TextureRegion reg = e.getImageHandler().getImage(entity.getImage());
		if(reg==null)
			reg = e.getImageHandler().getImage("empty");
		
		final int radius = 16;
		int xSize = reg.getRegionWidth();
		int zSize = reg.getRegionHeight();
		
		if(entity instanceof Ball) {
			xSize = radius;
			zSize = radius;
		}else if(entity instanceof WallX) {
			xSize = ((WallX)entity).getWidth();
			zSize = 2;
		}else if(entity instanceof WallZ) {
			xSize = 2;
			zSize = ((WallZ)entity).getHeight();
		}
		
		int movementX = 0;
		int movementZ = 0;
		float offsetX =  (x  - e.getPlayerLocation().getX());
		float offsetZ = (z  - e.getPlayerLocation().getZ());
		
		float angle = 0f;
		if(entity instanceof Ball) {
			Ball living = (Ball)entity;
			if(living.hasDirections())
				angle = living.getAngle();
		}
		float PosX = e.getCL().getX() - xSize/2 -movementX + offsetX;
		float PosY = e.getCL().getZ() - zSize/2 -movementZ + offsetZ;
		float originX = xSize/2;
		float originY = zSize/2;
		e.getBatch().draw(reg, PosX, PosY, originX, originY, xSize, zSize,1f, 1f, angle);
		if(entity instanceof Ball) {
			int health = ((Ball) entity).getHealth();
			health = health>=4?3:health;
			health = health<0?0:health;
			health = 3-health;
			e.getBatch().draw(e.getImageHandler().getImage("health_"+health), PosX, PosY, originX, originY, xSize, zSize,1f, 1f, angle);
		}
	}
	
	
	public static void renderDialog(Engine e) {
		String[] text = e.getDialog().getText();
		e.getBatch().setColor(new Color(1f,1f,1f,1f));
		if(e.getDialog().getOwner()!=null && !e.getDialog().getOwner().equalsIgnoreCase("null"))
			e.getBatch().draw(e.getImageHandler().getImage(e.getDialog().getOwner()), 5, 20);
		e.getBatch().draw(e.getImageHandler().getImage("dialog"), 0, 0,e.getCamera().viewportWidth,40);
		e.getBatch().setColor(new Color(0f,0f,0f,1f));
		
		for(int index=0;index<Math.min(3,text.length-e.getDialog().getLine());index++) 
			RenderUtil.renderText(e, e.getBatch(), new LocationFloat(0,36-((index+1)*11)), text[index+e.getDialog().getLine()]);
		if(text.length-e.getDialog().getLine()>3) 
			RenderUtil.renderText(e, e.getBatch(), new LocationFloat(11*16,36-(3*11)), "\\/");
		
		e.getBatch().setColor(new Color(1f,1f,1f,1f));
	}
	
	

	public static void renderCentured(Engine engine,SpriteBatch g,LocationFloat offset,String txt){
		renderText(engine,g,new LocationFloat(engine.getCamera().viewportWidth/2 + offset.x - calculateOffset(txt,txt.length()-1)/2,(int)engine.getCamera().viewportHeight/2 +offset.z),txt);
	}
	
	public static int calculateOffset(String txt,int index) {
		if(txt == null || txt == "" || index >= txt.length())return 0;
		int tempsize = 0;
		for(int a=0;a<txt.length();a++) {
			if(a>index)break;
			tempsize = tempsize+6;	
			if(txt.toCharArray()[a]==' ') 
				tempsize=tempsize-3;
			else if(Character.isUpperCase(txt.toCharArray()[a])) 
				tempsize=tempsize+2;	
			else if(txt.toCharArray()[a]=='l') 
				tempsize=tempsize-4;
			else if(txt.toCharArray()[a]=='t') 
				tempsize=tempsize-2;
			else if(txt.toCharArray()[a]=='r') 
				tempsize=tempsize-2;
			else if(txt.toCharArray()[a]=='m') 
				tempsize=tempsize+2;
			else if(txt.toCharArray()[a]=="'".toCharArray()[0]) 
				tempsize=tempsize-4;
			else if(txt.toCharArray()[a]==':') 
				tempsize=tempsize-4;
			
		}
		return tempsize;
	}
	
	
	public static void renderText(Engine en,SpriteBatch g,LocationFloat loc,String txt){
		if(txt == null || txt == "")return;
		int tempsize = 0;
		for(int a=0;a<txt.length();a++) {
			TextureRegion image = en.getImageHandler().getImage("small_"+txt.toCharArray()[a]);
			if(image==null) continue;
			tempsize = tempsize+6;	
			if(txt.toCharArray()[a]==' ') {
				tempsize=tempsize-3;
				continue;
			}
			g.draw(image, (int)loc.x + tempsize, (int)loc.z ,image.getRegionWidth(),image.getRegionHeight());

			if(Character.isUpperCase(txt.toCharArray()[a])) 
				tempsize=tempsize+2;	
			else if(txt.toCharArray()[a]=='l') 
				tempsize=tempsize-4;
			else if(txt.toCharArray()[a]=='t') 
				tempsize=tempsize-2;
			else if(txt.toCharArray()[a]=='r') 
				tempsize=tempsize-2;
			else if(txt.toCharArray()[a]=='m') 
				tempsize=tempsize+2;
			else if(txt.toCharArray()[a]=="'".toCharArray()[0]) 
				tempsize=tempsize-4;
			else if(txt.toCharArray()[a]==':') 
				tempsize=tempsize-4;
		}
	}

	public static String toShortFloat(float f){
		String text= f+"";
		text=text.replace('.', ',');
		String preA = text+"";
		String preP = "";
		if(text.split(",").length>1)
			{
			text=preA.split(",")[0];
			preP=preA.split(",")[1];
			preP=preP.substring(0, preP.length()>2?2:preP.length());
			text=text+","+preP;
			}
		text=text.replace(',', '.');
		return text;
	}
}
