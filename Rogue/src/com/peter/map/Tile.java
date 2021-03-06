package com.peter.map;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;

public class Tile{
	/*blocked, stairway, door, direction, canSee
	*/
	private Texture texture;
	private Texture visitedTexture;
	private String name;
	private boolean[] properties;

	public static final Tile BLANK = new Tile("blank.png", "blank.png", "", new boolean[]{true, false, false, false, false});
	public static final Tile GROUND = new Tile("tile.png", "tile2.png", "", new boolean[]{false, false, false, false, true});
	public static final Tile WALL = new Tile("pound.png", "pound2.png", "", new boolean[]{true, false, false, false, false});
	public static final Tile DOOR = new Tile("slash.png", "slash2.png", "Door", new boolean[]{false, false, true, false, false});
	public static final Tile WATER = new Tile("w.png", "w2.png", "", new boolean[]{true, false, false, false, true});
	public static final Tile DOWN = new Tile("greater.png", "greater2.png", "Downstairs", new boolean[]{false, true, false, false, true});
	public static final Tile UP = new Tile("less.png", "less2.png", "Upstairs", new boolean[]{false, true, false, true, true});
	public static final Tile ONE = new Tile("1.png", "1.png", "Inn", new boolean[]{false, false, true, false, true});
	
	public Tile(String filename, String filename2, String name, boolean[] properties){
		texture = new Texture(Gdx.files.internal("maps/" + filename));
		visitedTexture = new Texture(Gdx.files.internal("maps/" + filename2));
		this.name = name;
		this.properties = properties;
	}

	public String getName(){
		return name;
	}

	public Texture getTexture() {
		return texture;
	}

	public Texture getVisiedTexture() {
		return visitedTexture;
	}
	
	public void setTexture(Texture texture) {
		this.texture = texture;
	}
	
	public boolean isBlocked(){
		return properties[0];
	}
	public boolean hasStairs() {
		return properties[1];
	}
	public boolean isDoor(){
		return properties[2];
	}
	public boolean direction() {
		return properties[3];
	}

	public boolean canSee() {
		return properties[4];
	}

}
