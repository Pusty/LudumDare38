package me.game.level;

import me.game.main.Engine;

public class LevelHandler {
	Level[] levelArray;
	int currentIndex;
	
	public static final int STATUS_RUNNING = 0;
	public static final int STATUS_WINNING = 1;
	public static final int STATUS_LOST = 2;
	int status;
	public LevelHandler() {
		levelArray = new Level[10];
		currentIndex = 0;
	}
	public Level getLevel() {
		return levelArray[currentIndex];
	}
	public void setLevel(int i, Level l) {
		levelArray[i] = l;
	}
	public int getStatus() {
		return status;
	}
	public void setStatus(int i) {
		status = i;
	}
	public void checkDone(Engine e) {
		if(currentIndex>5) {
			e.setEndGame(1);
			currentIndex = 5;
		}
		if(levelArray[currentIndex].done(e)) {
			status = STATUS_WINNING;
			e.getSound().playClip("win");
//			currentIndex++;
//			levelArray[currentIndex].load(e);
		}
	}
	public void init(Engine e, int id) {
		currentIndex = id;
		if(id>5) {
			e.setEndGame(1);
			currentIndex = 5;
		}else
			levelArray[currentIndex].load(e);
	}
	public Level[] getLevelArray() {
		return levelArray;
	}
	public int getIndex() {
		return currentIndex;
	}
}
