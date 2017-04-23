package me.game.level;

import me.game.main.Engine;

public abstract class Level {
	int strokes=0;
	public Level() {
		strokes=0;
	}
	public int getStrokes() {
		return strokes;
	}
	public void setStrokes(int i) {
		strokes = i;
	}
	public void addStrokes(int i) {
		strokes = strokes + i;
		}
	public abstract int getIndex();
	public abstract boolean done(Engine e);
	public abstract void load(Engine e);
	public abstract int minStrokes();
}
