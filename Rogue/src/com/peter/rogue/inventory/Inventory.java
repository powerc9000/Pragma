package com.peter.rogue.inventory;

import java.util.LinkedList;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.peter.rogue.Global;

public class Inventory {
	
	private Backpack backpack = new Backpack();
	private LinkedList<Item> items;
	private int weight;
	public static final int HEIGHT = 300, WIDTH = 350;
	
	public Inventory(){
		backpack = Backpack.SMALL;
		items = new LinkedList<Item>();
	}
	
	public void add(Item item){
		if(item.getWeight() + weight <= backpack.getCapacity()){
			item.pickedUp(true);
			items.add(item);
		}
	}
	public void display(SpriteBatch spriteBatch, BitmapFont font){
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 0f, 0f, 1f);
		Global.screenShapes.rect(670, 250, WIDTH, HEIGHT);
		Global.screenShapes.end();
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0.2f, 0.1f, 0.0f, 1f);
		Global.screenShapes.rect(675, 255, WIDTH/2 - 5, HEIGHT - 10);
		Global.screenShapes.end();
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(.3f, .4f, 0, 1f);
		Global.screenShapes.rect(670, 250, WIDTH, HEIGHT);
		Global.screenShapes.end();
		
		spriteBatch.begin();
		for(int i=0; i<items.size(); i++){
			font.draw(spriteBatch, items.get(i).getName(),  675, 255 + HEIGHT - i * 15);
		}
		spriteBatch.end();
	}
	public Texture getBackpack(){
		return backpack.getTexture();
	}
}