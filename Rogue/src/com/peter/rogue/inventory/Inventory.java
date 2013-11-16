package com.peter.rogue.inventory;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.peter.rogue.Global;
import com.peter.rogue.entities.Entity;
import com.peter.rogue.entities.Player;
import com.peter.rogue.entities.Shopkeep;

public class Inventory {
	
	private Backpack backpack = new Backpack();
	private ArrayList<Item> items;
	private ArrayList<Rectangle> collisions;
	private int weight;
	private int wallet;
	private Item hover;
	public static final int WIDTH = 350, HEIGHT = 300;
	public static final int ORIGIN_X = 670, ORIGIN_Y = 250;
	private Entity trade;
	
	public Inventory(){
		backpack = Backpack.SMALL;
		items = new ArrayList<Item>();
		collisions = new ArrayList<Rectangle>();
		wallet = 0;
		add(Food.BREAD);
		add(Food.BREAD);
		add(Food.MEAT);
		add(Food.BREAD);
		add(Food.BREAD);
	}
	
	public boolean checkIsFull(Item item){
		if(item.getWeight() + weight > backpack.getCapacity())
			return true;
		return false;
	}
	
	public void add(Item item){
		if(!checkIsFull(item)){
			weight += item.getWeight();
			items.add(item);
			collisions.add(new Rectangle(0, 0, 150, 15));
		}
		else
			System.out.println("Backpack full!");
	}
	public Item remove(int i){
		weight -= items.get(i).getWeight();
		getCollision().remove(i);
		return items.remove(i);
	}
	public void display(SpriteBatch spriteBatch, BitmapFont font, Vector2 screenCoord, Player player){
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 0f, 0f, 1f);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.end();

		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0.1f, 0.1f, 0.1f, 1f);
		Global.screenShapes.rect(ORIGIN_X + 5, ORIGIN_Y + 5, WIDTH/2 - 35, HEIGHT - 10);
		Global.screenShapes.end();
		
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(Color.DARK_GRAY);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.line(ORIGIN_X + 150, ORIGIN_Y + 100, ORIGIN_X + WIDTH, ORIGIN_Y + 100);
		Global.screenShapes.line(ORIGIN_X + 150, ORIGIN_Y, ORIGIN_X + 150, HEIGHT + 250);
		Global.screenShapes.end();

		spriteBatch.begin();
		font.draw(spriteBatch, " Strength: " + player.getStats().getStrength(), ORIGIN_X + 195, ORIGIN_Y + 285);
		font.draw(spriteBatch, "    Health: " + player.getStats().getMaxHitpoints(), ORIGIN_X + 195, ORIGIN_Y + 265);
		font.draw(spriteBatch, " Defense: " + 2, ORIGIN_X + 195, ORIGIN_Y + 245);
		font.draw(spriteBatch, "Dexterity: " + player.getStats().getDexterity(), ORIGIN_X + 195, ORIGIN_Y + 225);
		font.draw(spriteBatch, "    Speed: " + 30, ORIGIN_X + 195, ORIGIN_Y + 205);
		
		if(player.getStats().getPoints() > 0){
			font.draw(spriteBatch, "  Points: " + player.getStats().getPoints(), ORIGIN_X + 205, ORIGIN_Y + 180);
			for(int i=0; i<5; i++)
				font.draw(spriteBatch, "+", ORIGIN_X + 285, ORIGIN_Y + 205 + i*20);
		}
		
		font.draw(spriteBatch, "Hunger: " + (int)(player.getHunger()*100) + "%", ORIGIN_X + 155, ORIGIN_Y + 145);
		font.draw(spriteBatch, "Thirst: " + "90%", ORIGIN_X + 260, ORIGIN_Y + 145);
		font.draw(spriteBatch, "Wallet: " + wallet, ORIGIN_X + 165, ORIGIN_Y + 125);
		font.draw(spriteBatch, "Weight: " + weight + "/" + backpack.getCapacity(), ORIGIN_X + 260, ORIGIN_Y + 125);
		
		for(int i=0; i<items.size(); i++){
			font.draw(spriteBatch, items.get(i).getName(), ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 10) - i*15);
			collisions.get(i).setPosition(ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 24) - i*15);
		}
		if(hover != null){
			spriteBatch.draw(hover.getTexture(), ORIGIN_X + 205, ORIGIN_Y + 50);
			font.draw(spriteBatch, hover.getName(), ORIGIN_X + 245, ORIGIN_Y + 70);
			font.draw(spriteBatch, "Value: " + hover.getValue(), ORIGIN_X + 180, ORIGIN_Y + 30);
			font.draw(spriteBatch, "Weight: " + hover.getWeight(), ORIGIN_X + 260, ORIGIN_Y + 30);
			hover = null;
		}
		spriteBatch.end();
		
		
		
		// Uncomment shape rendering for item collision debug
		/*Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 1f, 1f, .4f);*/


		// Item-mouse collision
		for(int i=0; i<getItems().size(); i++){
			//Global.screenShapes.rect(getCollision().get(i).x, getCollision().get(i).y, 150, 15);
			if(getCollision().get(i).contains(screenCoord)){
				if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched()){
					remove(i);
				}
				else if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
					if(trade != null){
						// In essence -> sells the item, then adds it to shopkeep's inventory
						if(trade instanceof Shopkeep){
							wallet += getItems().get(i).getValue();
							((Shopkeep) trade).add(remove(i));
						}
						// In essence -> adds item to chest and removes from inventory
						else if(trade instanceof Chest)
							((Chest) trade).add(remove(i));
					}
				}
				else{
					setHover(getItems().get(i));
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
	public Texture getBackpack(){
		return backpack.getTexture();
	}
	public boolean isItem(){
		if(hover == null)
			return false;
		return true;
	}
	public void setHover(Item hover){
		this.hover = hover;
	}
	
	public ArrayList<Item> getItems(){
		return items;
	}
	

	public int getWallet() {
		return wallet;
	}

	public void setWallet(int wallet) {
		this.wallet = wallet;
	}
	
	public void mutateWallet(int amount) {
		this.wallet += amount;
	}

	// These are assigned to whomever you are trading with
	public Entity getTrade() {
		return trade;
	}

	public void setTrade(Entity trade) {
		this.trade = trade;
	}
}