package com.peter.map;

import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.peter.entities.Animate;
import com.peter.entities.Entity;
import com.peter.entities.Monster;
import com.peter.entities.NPC;
import com.peter.entities.PacketToObject;
import com.peter.inventory.Chest;
import com.peter.inventory.Item;
import com.peter.packets.MPPlayer;
import com.peter.rogue.Global;

public class Map implements MapRenderer{
	public Tile[][] tiles;
	public byte[][] init;
	protected String[][] visible;
	//private LevelData data;
	public final int HEIGHT = 40, WIDTH = 80;
	public final int ROOM_HEIGHT = 6, ROOM_WIDTH = 10, HALL_LENGTH = 6;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewBounds;
	protected TextureRegion region;
	protected static int floor;
	public Marks marks;
	private static boolean initialize = false;

	public HashMap<Integer, MPPlayer> players;
	public HashMap<Integer, NPC> npcs;
	public HashMap<Integer, Item> items;
	public HashMap<Integer, Chest> chests;
	public HashMap<Integer, Entity> database;
	
	public Map(){
		spriteBatch = new SpriteBatch();
		viewBounds = new Rectangle();
		region = new TextureRegion();
		tiles = new Tile[WIDTH][HEIGHT];
		init = new byte[WIDTH][HEIGHT];
		visible = new String[WIDTH][HEIGHT];
		//data = new LevelData();
		players = new HashMap<Integer, MPPlayer>();
		npcs = new HashMap<Integer, NPC>();
		items = new HashMap<Integer, Item>();
		chests = new HashMap<Integer, Chest>();
		database = new HashMap<Integer, Entity>();
		
		marks = new Marks();
		
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				visible[x][y] = "notVisited";
		
		floor = 0;
	}
	
	public void clean(){
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				tiles[i][j] = Tile.BLANK;
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				visible[i][j] = "notVisited";
		/*items.clear();
		chests.clear();
		npcs.clear();*/
	}
	public Entity get(Integer ID){
		if(database.containsKey(ID))
			return database.get(ID);
		return null;
	}
	/*
	private void baseFloor(){
		int seaX = WIDTH-9, seaY = 1;
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
					tiles[x][y] = Tile.WALL;
				else
					tiles[x][y] = Tile.GROUND;
		
		createRoom(seaX-11, HEIGHT-12, seaX-3, HEIGHT-3, Tile.ONE);
		createRoom(seaX-11, 4, seaX-3, 12, Tile.DOOR);
		createRoom(12, 9, 20, 14, Tile.DOOR);


		for(int x=WIDTH/2-1; x<=WIDTH/2+1; x++)
			for(int y=HEIGHT/2-1; y<=HEIGHT/2+1; y++)
				tiles[x][y] = Tile.WATER;
		
		for(int x=seaX; x<WIDTH-1; x++)
			for(int y=seaY; y<HEIGHT-1; y++)
				tiles[x][y] = Tile.WATER;
		
		for(int y=0; y<HEIGHT-1; y++)
			tiles[WIDTH-1][y] = Tile.BLANK;
		
		
		tiles[24][10] = Tile.DOWN;
	}
	
	
	
	private void createRoom(int x, int y, int dx, int dy, Tile type){
		for(int i=x; i<=dx; i++)
			for(int j=y; j<=dy; j++)
				if(j == y || j == dy || i == x || i == dx)
					tiles[i][j] = Tile.WALL;
				else
					tiles[i][j] = Tile.GROUND;
		switch(Global.rand(4, 0)){
		case 0:
			tiles[x][(y + dy)/2] = type;
			break;
		case 1:
			tiles[x + (dx - x)][(y + dy)/2] = type;
			break;
		case 2:
			tiles[(x + dx)/2][y] = type;
			break;
		case 3:
			tiles[(x + dx)/2][y + (dy - y)] = type;
			break;
		}
	}*/
	
	public void draw(){
		if(!initialize){
			for(int x=0; x<WIDTH; x++)
				for(int y=0; y<HEIGHT; y++)
					tiles[x][y] = PacketToObject.tileConverter(init[x][y]);
			initialize = true;
		}
		for(int x=(int) (Global.camera.position.x/32 -  Gdx.graphics.getWidth()/64) > 0 ? (int) (Global.camera.position.x/32 - Gdx.graphics.getWidth()/64) : 0;
		        x<WIDTH && x<(int) (Global.camera.position.x/32 + Gdx.graphics.getWidth()/64) +1; x++)
			for(int y=(int) (Global.camera.position.y/32 -  Gdx.graphics.getHeight()/64) + 2 > 0 ? (int) (Global.camera.position.y/32 - Gdx.graphics.getHeight()/64) + 2 : 0;
			        y<HEIGHT && y<(int) (Global.camera.position.y/32 + Gdx.graphics.getHeight()/64) + 1; y++){
				if(visible[x][y].equals("visited")){
					spriteBatch.draw(tiles[x][y].getTexture(), 32 * x, 32 * y);
					visible[x][y] = "hasVisited";
				}
				else if(visible[x][y].equals("hasVisited"))
					spriteBatch.draw(tiles[x][y].getVisiedTexture(), 32 * x, 32 * y);
				else
					spriteBatch.draw(Tile.BLANK.getTexture(), 32 * x, 32 * y);
			}
	}
	
	// ------------- Getters -------------
	public Tile getTile(float x, float y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return Tile.BLANK;
		return tiles[(int)(x/32)][(int)(y/32)];
	}

	public Rectangle getViewBounds () {
		return viewBounds;
	}
	
	public static int getFloor(){
		return floor;
	}
	
	public SpriteBatch getSpriteBatch () {
		return spriteBatch;
	}
	
	// ------------- Setters -------------
	@Override
	public void setView (OrthographicCamera camera) {
		spriteBatch.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
	}
	@Override
	public void setView (Matrix4 projection, float x, float y, float width, float height) {
		spriteBatch.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
	}
	
	public String cursor(Integer ID, int x, int y){
		if(get(ID) != null && get(ID).canDraw)
			if(get(ID) instanceof Monster)
				return get(ID).getName() + ", level " + ((Animate) get(ID)).getStats().getLevel();
			else if(get(ID) instanceof NPC)
				return get(ID).getName() + ", " + ((Animate) get(ID)).getType();
			else
				return get(ID).getName();
		else if(getTile(x, y) != null)
			return getTile(x, y).getName();
		return "";
	}
	
	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void render(int[] layers) {
		// TODO Auto-generated method stub
		
	}

	public void setVisible(float x, float y, String visible) {
		this.visible[(int)(x/32)][(int)(y/32)] = visible;
	}
}
