package com.peter.rogue.map;

import java.util.HashMap;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;
import com.peter.rogue.entities.Entity;

public class Map implements MapRenderer{
	protected Tile[][] tiles;
	private String[][] marker;
    private HashMap<String, Entity> database;
	private Data data;
	public final int HEIGHT = 40, WIDTH = 40;
	public final int ROOM_HEIGHT = 6, ROOM_WIDTH = 10, HALL_LENGTH = 6;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewBounds;
	protected TextureRegion region;
	protected static int floor;
	private static int direction;
	
	static{
	}
	
	public Map(){
		spriteBatch = new SpriteBatch();
		viewBounds = new Rectangle();
		region = new TextureRegion();

		tiles = new Tile[WIDTH][HEIGHT];
		marker = new String[WIDTH][HEIGHT];
		database = new HashMap<String, Entity>();
		data = new Data();
		
		baseFloor();
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				marker[i][j] = new String("");
		
		setData(new Data());
		floor = 0;
	}
	
	public void load(int direction, float x, float y){
		floor += direction;
		setDirection(direction);
		clear();
		if(floor == 0){
			baseFloor();
		}
		else if(direction == -1){
			generateFloor((int)x/32, (int)y/32);
			tiles[(int)x/32][(int)y/32] = Tile.UP;
		}
		else if(direction == 1){
			
		}
	}
	
	private void baseFloor(){
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
					tiles[x][y] = Tile.WALL;
				else
					tiles[x][y] = Tile.GROUND;
		tiles[10][33] = Tile.DOWN;
		tiles[20][20] = Tile.WALL;
		tiles[21][20] = Tile.WALL;
		tiles[20][21] = Tile.WALL;
		tiles[21][21] = Tile.WALL;
	}
	
	private boolean generateFloor(int x, int y){
		System.out.println(x + ", " + y);
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
			System.out.println("Here");
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
	
	private void clear(){
		for(int i=0; i<WIDTH; i++)
			for(int j=0; j<HEIGHT; j++)
				tiles[i][j] = Tile.BLANK;
	}
	
	public void draw(){
		spriteBatch.begin();
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++){
				spriteBatch.draw(tiles[x][y].getTexture(), 32 * x, 32 * y);
			}
		spriteBatch.end();
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

	public static int getDirection() {
		return direction;
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
	
	public Data getData(){
		return data;
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

	public static void setDirection(int direction) {
		Map.direction = direction;
	}
	
	public void setMark(String ID, float x, float y){
		marker[(int)(x/32)][(int)(y/32)] = ID;
	}

	public void setData(Data data) {
		this.data = data;
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
}
