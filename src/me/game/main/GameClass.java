package me.game.main;


import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.JsonValue;




import me.game.dif.TickClassHandler;
import me.game.level.*;
import me.game.render.RawAnimation;
import me.game.util.FileChecker;
import me.game.util.json.JsonHandler;

public class GameClass extends Engine{

	

	public GameClass(){
		super();
		
		
	}
	
	
	public void loadDefault() {
		try {
			JsonValue obj  = (new JsonHandler()).getObjectFromFile(Gdx.files.getFileHandle("scripts/game.json", FileType.Internal).read());
			if(obj.has("icon"))
				setIconPath(obj.getString("icon"));
			if(obj.has("name"))
				setGameName(obj.getString("name"));
			if(obj.has("world"))
				setStartWorld(obj.getString("world"));
			if(obj.has("debug"))
				setDebugMode(obj.getBoolean("debug"));
			if(obj.has("save"))
				setCanSave(obj.getBoolean("save"));
			if(obj.has("text")) {
				List<String> list = new ArrayList<String>();
				for(JsonValue jst:obj.get("text"))
					list.add(jst.asString());
				setText(list.toArray(new String[list.size()]));
			}
			if(obj.has("variables")) {
				for(JsonValue jst:obj.get("variables"))
					getVariable().put(jst.name(),jst.asInt());
			}
		} catch(Exception e){e.printStackTrace();}
	
	}
	
	
	@Override
	public void preInit() {
		FileChecker.createStreams();
		TickClassHandler.initTickHandler(this);
		this.setBatch(new SpriteBatch());
		this.setFont(new BitmapFont());
		
		
		camera = new OrthographicCamera();
        camera.setToOrtho(false, 213, 120);
		try{
			
			for(Entry<String,FileHandle> entry:FileChecker.checkFolderToHashMap("", "png").entrySet()) {
				String name = FileChecker.splitNonRegex(entry.getKey(),".")[0];
				FileHandle file = entry.getValue();
				if(name.equalsIgnoreCase("letters")) {
					String[] letter = { "A", "B", "C", "D", "E", "F", "G", "H", "I",
							"J", "K", "L", "M", "N", "O", "P", "Q", "R", "S", "T", "U",
							"V", "W", "X", "Y", "Z", ":", "!", "?", ".", "[", "]", "0",
							"1", "2", "3", "4", "5", "6", "7", "8", "9", "(", ")", "+",
							"-", "/", " ", "_","," };
					
					

					Texture tex = new Texture(file);
					TextureRegion[][]  tmp = TextureRegion.split(tex, tex.getWidth()/8, tex.getHeight()/8);
			        int index = 0;
			        for (int i = 0; i < 8; i++) {
			            for (int j = 0; j < 8; j++) {
			            	getImageHandler().addImage("char_" + letter[index], tmp[i][j]);
			                index++;
			                if(index >= letter.length)
			                	break;
			            }
		                if(index >= letter.length)
		                	break;
			        }
			        
			     
				}else if(name.equalsIgnoreCase("chars")) {
					
					char[] smallletters = { ' ','a','b','c','d','e','f','g','h','i',
											'j','k','l','m','n','o','p','q','r','s',
											't','u','v','w','x','y','z','A','B','C',
											'D','E','F','G','H','I','J','K','L','M',
											'N','O','P','Q','R','S','T','U','V','W',
											'X','Y','Z','0','1','2','3','4','5','6',
											'7', '8','9','!','"','%','&','/','(',')',
											'=', '?','[',']','{','}','\\','|','<','>',
											'*', '+','~',"'".toCharArray()[0],'#','-','_','.',':',',',
											';'};
					
					
					
					Texture tex = new Texture(file);
					TextureRegion[][]  tmp = TextureRegion.split(tex, tex.getWidth()/10, tex.getHeight()/10);
			        int index = 0;
			        for (int i = 0; i < tmp.length; i++) {
			            for (int j = 0; j < tmp[i].length; j++) {
			            	getImageHandler().addImage("small_" + smallletters[index], tmp[i][j]);
			                index++;
			                if(index >= smallletters.length)
			                	break;
			            }
		                if(index >= smallletters.length)
		                	break;
			        }
				}else {
					Texture texture = new Texture(file);
					if(name.contains("bg") || name.contains("dialog") || texture.getWidth() != 32 || texture.getHeight() != 32)
						getImageHandler().addImage(name, new TextureRegion(texture));
					else {
						int splitterX = texture.getWidth()/32;
						int splitterY = texture.getHeight()/32;
						TextureRegion[][]  tmp = TextureRegion.split(texture, texture.getWidth()/splitterX, texture.getHeight()/splitterY);
				        int index = 0;
				        for (int i = 0; i < tmp.length; i++) {
				            for (int j = 0; j < tmp[i].length; j++) {
				            	getImageHandler().addImage(name+"_"+index, tmp[i][j]);
				                index++;
				            }
				        }
					}
						
				}
			}


			//Animation loader
			
			JsonHandler handler = new JsonHandler();
		
				try {
					JsonValue jsobj  = handler.getArrayFromFile(Gdx.files.getFileHandle("scripts/animations.json", FileType.Internal).read());;
					for(JsonValue jobj:jsobj){
						RawAnimation animation = new RawAnimation(jobj);
						this.getAnimationHandler().addAnimation(jobj.getString("name"), animation);
					}
				}catch (Exception e){
					e.printStackTrace();
				}
			
		

		}catch(Exception e){e.printStackTrace();}
		
		for(Entry<String,FileHandle> entry:FileChecker.checkFolderToHashMap("", "wav").entrySet()) {
			getSound().addSound(FileChecker.splitNonRegex(entry.getKey(),".")[0],entry.getValue(),false);
		}
		
	}
	

	@Override
	public void Init() {
		getLevelHandler().setLevel(0, new Level00());
		getLevelHandler().setLevel(1, new Level01());
		getLevelHandler().setLevel(2, new Level02());
		getLevelHandler().setLevel(3, new Level03());
		getLevelHandler().setLevel(4, new Level04());
		getLevelHandler().setLevel(5, new Level05());
		getLevelHandler().init(this, 0);
	}

	@Override
	public void postInit() {

	}


	@Override
	public void initStartScreen() {
		this.setScreen(TickClassHandler.handler.getTick(this, 0));
	    Gdx.input.setInputProcessor(TickClassHandler.handler.getTick(this, 0));
	}
    
}
