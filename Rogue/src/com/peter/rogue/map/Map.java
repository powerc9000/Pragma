package com.peter.rogue.map;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.MapRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.math.Rectangle;

public class Map implements MapRenderer{
	protected static Tile[][] tiles;
	public static final int HEIGHT = 40, WIDTH = 40;
	protected SpriteBatch spriteBatch;
	protected Rectangle viewBounds;
	protected TextureRegion region;
	protected static int floor;
	
	public Map(){
		spriteBatch = new SpriteBatch();
		viewBounds = new Rectangle();
		region = new TextureRegion();
		tiles = new Tile[WIDTH][HEIGHT];
		floor = 0;
		load(floor);
	}
	
	public static void load(int direction){
		floor -= direction;
		if(floor == -1){
			generateFloor();
		}
		else if(floor == 0){
			for(int x=0; x<WIDTH; x++)
				for(int y=0; y<HEIGHT; y++)
					if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-1)
						tiles[x][y] = Tile.WALL;
					else
						tiles[x][y] = Tile.GROUND;
			tiles[20][18] = Tile.DOWN;
		}
		else if(floor == 1){
			
		}
	}
	
	private static void generateFloor(){
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++)
				if(y == 0 || y == HEIGHT-1 || x == 0 || x == WIDTH-5)
					tiles[x][y] = Tile.WALL;
				else
					tiles[x][y] = Tile.GROUND;
		tiles[20][18] = Tile.UP;
	}
	
	public void draw(){
		for(int x=0; x<WIDTH; x++)
			for(int y=0; y<HEIGHT; y++){
				spriteBatch.draw(tiles[x][y].getTexture(), 32 * x, 32 * y);
			}
	}
	
	public Tile getTile(float x, float y){
		if(y < 0 || x < 0)
			return null;
		return tiles[(int)(x/32)][(int)(y/32)];
	}

	public Rectangle getViewBounds () {
		return viewBounds;
	}
	
	public static int getFloor(){
		return floor;
	}
	
	@Override
	public void setView (OrthographicCamera camera) {
		spriteBatch.setProjectionMatrix(camera.combined);
		float width = camera.viewportWidth * camera.zoom;
		float height = camera.viewportHeight * camera.zoom;
		viewBounds.set(camera.position.x - width / 2, camera.position.y - height / 2, width, height);
	}
	
	public SpriteBatch getSpriteBatch () {
		return spriteBatch;
	}

	@Override
	public void setView (Matrix4 projection, float x, float y, float width, float height) {
		spriteBatch.setProjectionMatrix(projection);
		viewBounds.set(x, y, width, height);
	}

	@Override
	public void render() {
		// TODO Auto-generated method stub
		
	}
	

	/*beginRender();
	for (MapLayer layer : map.getLayers()) {
		if (layer.isVisible()) {
			if (layer instanceof TiledMapTileLayer) {
				renderTileLayer((TiledMapTileLayer)layer);
			} else {
				for (MapObject object : layer.getObjects()) {
					renderObject(object);
				}
			}
		}
	}
	*/

	@Override
	public void render(int[] layers) {
		// TODO Auto-generated method stub
		
	}
}