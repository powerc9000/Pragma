package com.peter.rogue;

import java.util.Random;

import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.peter.rogue.entities.Inbox;

public class Global {
	
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
	public static TiledMap map = new TmxMapLoader().load("maps/map.tmx");;
    public final static int WIDTH = 100;
    public final static int HEIGHT = 100;
	public static Inbox inbox = new Inbox();
	public static OrthographicCamera camera;
	public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    
    private static Random generator = new Random(System.currentTimeMillis());
	
    public static int rand(int range, int origin) {
    	return generator.nextInt(range) + origin;
    }
}
