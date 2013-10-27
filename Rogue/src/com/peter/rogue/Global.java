package com.peter.rogue;

import java.util.Random;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.peter.rogue.data.MapData;
import com.peter.rogue.map.Map;

public class Global {
	
    public static final int SCREEN_WIDTH = 1280;
    public static final int SCREEN_HEIGHT = 720;
    
	public static Map renderer = new Map();
	public static MapData data = new MapData();
	public static OrthographicCamera camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
	public static ShapeRenderer shapeRenderer = new ShapeRenderer();
    
    private static Random generator = new Random(System.currentTimeMillis());
    
    public static void changeMap(int level, Map renderer){
    }
    public static int rand(int range, int origin) {
    	return generator.nextInt(range) + origin;
    }
}