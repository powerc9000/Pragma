package com.peter.rogue.entities;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;
import com.peter.rogue.inventory.Food;
import com.peter.rogue.inventory.Item;

public class Shopkeep extends NPC{

	public static Shopkeep Bartender = new Shopkeep("s_.png", "Bartender");
	public static Shopkeep Shopkeep = new Shopkeep("s_.png", "Shopkeep");
	
	private ArrayList<Item> items;
	private ArrayList<Rectangle> collisions;
	private Item hover;
	private Rectangle hoverCollision;
	private Entity trade;
	
	public static final int HEIGHT = 300, WIDTH = 350;
	public static final int ORIGIN_X = 250, ORIGIN_Y = 250;
	
	public Shopkeep(String filename, String type) {
		super(filename, type);
		
		this.items = new ArrayList<Item>();
		this.collisions = new ArrayList<Rectangle>();
		stats.setLevel(1);
		stats.setHitpoints(10);
		stats.setDexterity(5);
		stats.setStrength(1);
		stats.setExperience(100);
		
		add(Food.MEAT);
		add(Food.MEAT);
		add(Food.BREAD);
		add(Food.MEAT);
		add(Food.MEAT);
	}
	
	public void update(float delta){
		super.update(delta);
	}
	
	@Override
	public void checkCollision(){
		if(map.getTile(getX(), getY()).isBlocked()){
			setX(oldX);
			setY(oldY);
		}
		if(!(map.getMark(getX(), getY()).equals("") || map.getMark(getX(), getY()).equals(ID))){
			if(map.get(getX(), getY()).getType().equals("Item") || map.get(getX(), getY()).getType().equals("Chest")
					|| map.getTile(getX(), getY()).isDoor())
				bump();
			else
				if(list.check((Animate)map.get(getX(), getY())))
					if(map.get(getX(), getY()).getType().equals("Player"))
						attack((Player) map.get(getX(), getY()));
					else
						attack((NPC) map.get(getX(), getY()));
				else
					bump((Animate) map.get(getX(), getY()));
		}
		

		map.setMark("", oldX, oldY);
		map.setMark(ID, getX(), getY());
		oldX = getX();
		oldY = getY();
	}

	public void add(Item item){
		items.add(item);
		collisions.add(new Rectangle(0, 0, 150, 15));
	}
	
	public Item remove(int i){
		getCollision().remove(i);
		return items.remove(i);
	}
	
	public void display(SpriteBatch spriteBatch, BitmapFont font, Vector2 screenCoord){
		Vector3 coord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(coord);
		
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 0f, 0f, 1f);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.end();

		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0.1f, 0.1f, 0.1f, 1f);
		Global.screenShapes.rect(ORIGIN_X + 5, ORIGIN_Y + 5, WIDTH/2 - 15, HEIGHT - 10);
		Global.screenShapes.end();
		
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(Color.DARK_GRAY);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.line(ORIGIN_X + 170, ORIGIN_Y + 100, ORIGIN_X + WIDTH, ORIGIN_Y + 100);
		Global.screenShapes.line(ORIGIN_X + 170, ORIGIN_Y, ORIGIN_X + 170, HEIGHT + 250);
		Global.screenShapes.end();

		if(hover != null){
			spriteBatch.begin();
			spriteBatch.draw(hover.getTexture(), ORIGIN_X + 205, ORIGIN_Y + 50);
			font.draw(spriteBatch, hover.getName(), ORIGIN_X + 245, ORIGIN_Y + 70);
			font.draw(spriteBatch, "Value: " + hover.getValue(), ORIGIN_X + 180, ORIGIN_Y + 30);
			font.draw(spriteBatch, "Weight: " + hover.getWeight(), ORIGIN_X + 260, ORIGIN_Y + 30);
			spriteBatch.end();
			Global.screenShapes.begin(ShapeType.Filled);
			Global.screenShapes.setColor(.2f, .2f, .2f, 1f);
			Global.screenShapes.rect(hoverCollision.x, hoverCollision.y, hoverCollision.width, hoverCollision.height);
			Global.screenShapes.end();
			hover = null;
		}
		
		spriteBatch.begin();
		
		for(int i=0; i<items.size(); i++){
			font.draw(spriteBatch, items.get(i).getName(), ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 10) - i*15);
			collisions.get(i).setPosition(ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 24) - i*15);
		}
		
		spriteBatch.end();
		
		
		
		
		/*Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 1f, 1f, .4f);*/

		// Item-mouse collision
		for(int i=0; i<getItems().size(); i++){
			//Global.screenShapes.rect(getCollision().get(i).x, getCollision().get(i).y, 150, 15);
			if(getCollision().get(i).contains(screenCoord)){

				Global.mapShapes.begin(ShapeType.Filled);
				Global.mapShapes.setColor(0f, 0, 0, 1f);
				Global.mapShapes.rect(coord.x - Global.font.getBounds("buy").width - 2, coord.y, Global.font.getBounds("buy").width, Global.font.getLineHeight());
				Global.mapShapes.end();
				Entity.map.getSpriteBatch().begin();
				
				Global.font.draw(Entity.map.getSpriteBatch(), "buy", coord.x - Global.font.getBounds("buy").width - 2, coord.y + Global.font.getLineHeight() - 2);
				Entity.map.getSpriteBatch().end();
				
				if(Gdx.input.isButtonPressed(Buttons.RIGHT))
					System.out.println("Pressed right mouse button");
				else if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
					if(trade != null){
						// In essence -> removes item from chest and gives to player
						if(trade instanceof Player){
							if(((Player) trade).getInventory().checkIsFull(getItems().get(i)))
								System.out.println("Backpack is full!");
							else if(((Player) trade).getInventory().getWallet() - items.get(i).getValue() < 0)
								System.out.println("Insufficient funds!");
							else{
								((Player) trade).getInventory().mutateWallet(-getItems().get(i).getValue());
								((Player) trade).getInventory().add(remove(i));
							}
						}
					}
				}
				else{
					setHover(getItems().get(i), collisions.get(i));
				}
			}
		}
		Global.screenShapes.end();
	    Gdx.gl.glDisable(GL10.GL_BLEND);
	}
	
	public ArrayList<Rectangle> getCollision() {
		return collisions;
	}
	public void setCollisions(ArrayList<Rectangle> collisions) {
		this.collisions = collisions;
	}
	
	public boolean isItem(){
		if(hover == null)
			return false;
		return true;
	}
	public void setHover(Item hover, Rectangle collision){
		this.hover = hover;
		this.hoverCollision = collision;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	

	public void setTrade(Entity trade) {
		this.trade = trade;
	}
}
