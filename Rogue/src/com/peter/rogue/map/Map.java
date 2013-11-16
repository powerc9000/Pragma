package com.peter.rogue.map;

import java.util.ArrayList;
import java.util.HashMap;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.peter.rogue.Global;
import com.peter.rogue.data.LevelData;
import com.peter.rogue.entities.Entity;
import com.peter.rogue.entities.NPC;
import com.peter.rogue.entities.Shopkeep;
import com.peter.rogue.inventory.Chest;
import com.peter.rogue.inventory.Item;

public class Map implements MapRenderer{
	protected Tile[][] tiles;
	protected String[][] visible;
	private String[][] marker;
	private LevelData data;
    private HashMap<String, Entity> database;
	public final int HEIGHT = 40, WIDTH = 80;
	public final int ROOM_HEIGHT = 6, ROOM_WIDTH = 10, HALL_LENGTH = 6;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewBounds;
	protected TextureRegion region;
	protected static int floor;
	public ArrayList<Item> items;
	public ArrayList<Chest> chests;
	public ArrayList<NPC> npcs;
	
	public Map(){
		spriteBatch = new SpriteBatch();
		viewBounds = new Rectangle();
		region = new TextureRegion();

		tiles = new Tile[WIDTH][HEIGHT];
		visible = new String[WIDTH][HEIGHT];
		marker = new String[WIDTH][HEIGHT];
		data = new LevelData();
		database = new HashMap<String, Entity>();
		items = new ArrayList<Item>();
		chests = new ArrayList<Chest>();
		npcs = new ArrayList<NPC>();
		
		baseFloor();
		
		floor = 0;
	}
	
	public void load(int direction, float x, float y){
		floor += direction;

		if(direction == -1){
			data.save(WIDTH, HEIGHT, tiles, visible, marker, items, chests, npcs);
			clean();
			generateFloor((int)x/32, (int)y/32);
			tiles[(int)x/32][(int)y/32] = Tile.UP;
		}
		else if(direction == 1){
			clean();
			tiles = data.tiles.pop();
			visible = data.visible.pop();
			marker = data.marker.pop();
			items = data.items.pop();
			chests = data.chests.pop();
			npcs = data.npcs.pop();
		}
		
		
	}
	
	public void clean(){
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				tiles[i][j] = Tile.BLANK;
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				visible[i][j] = "notVisited";
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				marker[i][j] = "";
		items.clear();
		chests.clear();
		npcs.clear();
	}
	
	private void baseFloor(){
		int seaX = WIDTH-9, seaY = 1;
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
					tiles[x][y] = Tile.WALL;
				else
					tiles[x][y] = Tile.GROUND;
		tiles[10][33] = Tile.DOWN;
		
		createRoom(seaX-11, HEIGHT-12, seaX-3, HEIGHT-3);
		createRoom(seaX-11, 4, seaX-3, 12);
		createRoom(12, 9, 20, 14);


		for(int x=WIDTH/2-1; x<=WIDTH/2+1; x++)
			for(int y=HEIGHT/2-1; y<=HEIGHT/2+1; y++)
				tiles[x][y] = Tile.WATER;
		
		for(int x=seaX; x<WIDTH-1; x++)
			for(int y=seaY; y<HEIGHT-1; y++)
				tiles[x][y] = Tile.WATER;
		
		for(int y=0; y<HEIGHT-1; y++)
			tiles[WIDTH-1][y] = Tile.BLANK;
		
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				visible[x][y] = "notVisited";
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				marker[i][j] = new String("");
	}
	
	private void createRoom(int x, int y, int dx, int dy){
		for(int i=x; i<=dx; i++)
			for(int j=y; j<=dy; j++)
				if(j == y || j == dy || i == x || i == dx)
					tiles[i][j] = Tile.WALL;
				else
					tiles[i][j] = Tile.GROUND;
		switch(Global.rand(4, 0)){
		case 0:
			tiles[x][(y + dy)/2] = Tile.DOOR;
			break;
		case 1:
			tiles[x + (dx - x)][(y + dy)/2] = Tile.DOOR;
			break;
		case 2:
			tiles[(x + dx)/2][y] = Tile.DOOR;
			break;
		case 3:
			tiles[(x + dx)/2][y + (dy - y)] = Tile.DOOR;
			break;
		}
	}
	
	private boolean generateFloor(int x, int y){
		generateRoom(x, y);
		return false;
	}
	
	private boolean generateRoom(int x, int y){
		if(x-ROOM_WIDTH/2 < 0 || x+ROOM_WIDTH/2 >= WIDTH || y-ROOM_HEIGHT/2 < 0 || y+ROOM_HEIGHT/2 >= WIDTH){
			return false;
		}
		for(int i=(x-ROOM_WIDTH/2 + 1); i<(x+ROOM_WIDTH/2); i++)
			for(int j=(y-ROOM_HEIGHT/2 + 1); j<(y+ROOM_HEIGHT/2); j++)
				tiles[i][j] = Tile.GROUND;
		for(int i=(x-ROOM_WIDTH/2); i<(x+ROOM_WIDTH/2+1); i++)
			for(int j=(y-ROOM_HEIGHT/2); j<(y+ROOM_HEIGHT/2+1); j++)
				if(i == (x-ROOM_WIDTH/2) || i == (x+ROOM_WIDTH/2)
				   || j == (y-ROOM_HEIGHT/2) || j == (y+ROOM_HEIGHT/2))
				tiles[i][j] = Tile.WALL;
		if(!generateHall(x, y+ROOM_HEIGHT/2, "up"))
			if(!generateHall(x-ROOM_WIDTH/2, y, "left"))
				if(!generateHall(x, y-ROOM_HEIGHT/2, "down"))
					if(!generateHall(x+ROOM_WIDTH/2, y, "right"))
						return false;
					else{
						if(generateRoom(x+ROOM_WIDTH+HALL_LENGTH, y))
							tiles[x+ROOM_WIDTH/2+HALL_LENGTH][y] = Tile.DOOR;
					}
				else{
					if(generateRoom(x, y-ROOM_HEIGHT-HALL_LENGTH))
						tiles[x][y-ROOM_HEIGHT/2-HALL_LENGTH] = Tile.DOOR;
				}
			else{
				if(generateRoom(x-ROOM_WIDTH-HALL_LENGTH, y))
					tiles[x-ROOM_WIDTH/2-HALL_LENGTH][y] = Tile.DOOR;
			}
		else{
			if(generateRoom(x, y+ROOM_HEIGHT+HALL_LENGTH))
				tiles[x][y+ROOM_HEIGHT/2+HALL_LENGTH] = Tile.DOOR;
		}
		return true;
	}
	
	private boolean generateHall(int x, int y, String direction){
		boolean flag = true;
		tiles[x][y] = Tile.DOOR;
		if(direction.equals("left")){
			x--;
			for(int i=x; i>x-HALL_LENGTH; i--)
				if(i<0 || tiles[i][y] != Tile.BLANK)
					flag = false;
			if(x-HALL_LENGTH - ROOM_WIDTH >= 0 && flag){
				for(int i=x; i>x-HALL_LENGTH; i--)
					tiles[i][y] = Tile.GROUND;
			}
			else
				tiles[++x][y] = Tile.WALL;
		}
		else if(direction.equals("right")){
			x++;
			for(int i=x; i<x+HALL_LENGTH; i++)
				if(i>=WIDTH || tiles[i][y] != Tile.BLANK)
					flag = false;
			if(x+HALL_LENGTH + ROOM_WIDTH < WIDTH && flag)
				for(int i=x; i<x+HALL_LENGTH; i++)
					tiles[i][y] = Tile.GROUND;
			else
				tiles[--x][y] = Tile.WALL;
		}
		else if(direction.equals("up")){
			y++;
			for(int j=y; j<y+HALL_LENGTH; j++)
				if(j>=HEIGHT || tiles[x][j] != Tile.BLANK)
					flag = false;
			if(y+HALL_LENGTH + ROOM_HEIGHT < HEIGHT && flag)
				for(int j=y; j<y+HALL_LENGTH; j++)
					tiles[x][j] = Tile.GROUND;
			else
				tiles[x][--y] = Tile.WALL;
		}
		else if(direction.equals("down")){
			y--;
			for(int j=y; j>y-HALL_LENGTH; j--)
				if(j<0 || tiles[x][j] != Tile.BLANK)
					flag = false;
			if(y-HALL_LENGTH - ROOM_HEIGHT >= 0 && flag)
				for(int j=y; j>y-HALL_LENGTH; j--)
					tiles[x][j] = Tile.GROUND;
			else
				tiles[x][++y] = Tile.WALL;
		}
		return flag;
	}
	
	public void draw(){
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
		for(int i=0; i<items.size(); i++){
			if(items.get(i).isPickedUp())
				items.remove(i);
			else if(items.get(i).canDraw)
				items.get(i).draw(getSpriteBatch());
		}
		for(int i=0; i<chests.size(); i++){
			if(chests.get(i).canDraw)
				chests.get(i).draw(getSpriteBatch());
		}

		for(int i=0; i<npcs.size(); i++)
			if(!npcs.get(i).canDraw)
				npcs.get(i).update(Gdx.graphics.getDeltaTime());
			else
				npcs.get(i).draw(getSpriteBatch());
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
	
	public String getMark(float x, float y){
		if(y < 0 || x < 0 || y/32 >= HEIGHT || x/32 >= WIDTH)
			return "";
		else
			return marker[(int)(x/32)][(int)(y/32)];
	}
	
	public Entity get(String ID){
		if(ID != "")
			return database.get(ID);
		return null;
	}
	
	public Entity get(float x, float y){
		return database.get(getMark(x, y));
	}
	
	public void put(String ID, Entity entity){
		database.put(ID, entity);
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
	
	public void setMark(String ID, float x, float y){
		marker[(int)(x/32)][(int)(y/32)] = ID;
	}
	
	public void remove(String ID){
		setMark("", (int)database.get(ID).getX()/32, (int)database.get(ID).getY()/32);
		database.remove(ID);
	}

	public String cursor(String ID){
		if(!(ID.equals("") || ID.equals(null)))
			return database.get(ID).getName();
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

	public void purge(String id) {
		
	}

	public LevelData getData() {
		return data;
	}

	public void setData(LevelData data) {
		this.data = data;
	}
}
