package com.peter.inventory;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Buttons;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.peter.entities.Entity;
import com.peter.entities.Player;
import com.peter.entities.Shopkeep;
import com.peter.packets.AddTradeItemPacket;
import com.peter.packets.ItemPacket;
import com.peter.rogue.Global;
import com.peter.rogue.screens.Play;

public class Inventory {
	
	
	private Backpack backpack = new Backpack();
	private ArrayList<Item> items;
	private ArrayList<Rectangle> collisions;
	private Rectangle[] pointCollisions;
	private Gear gear;
	private int weight;
	private int wallet;
	private Item hover;
	private Rectangle hoverCollision;
	public static final int BOX1_WIDTH = 150, BOX2_WIDTH = 165, HEIGHT = 300, WIDTH = 500;
	public static final int ORIGIN_X = 670, ORIGIN_Y = 250, STATS = 5;
	private Entity trade;
	
	public Inventory(){
		backpack = Backpack.SMALL;
		items = new ArrayList<Item>();
		collisions = new ArrayList<Rectangle>();
		gear = new Gear(ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y);
		pointCollisions = new Rectangle[STATS];
		for(int i=0; i<STATS; i++){
			pointCollisions[i] = new Rectangle();
			pointCollisions[i].setSize(8);
		}
		wallet = 0;
		add(new Wearable(Wearable.BREAST_PLATE));
		add(new Wearable(Wearable.SHOES));
		add(new Food(Food.BREAD));
		add(new Food(Food.BREAD));
	}
	
	public boolean checkIsFull(Item item){
		if(item.getWeight() + weight > backpack.getCapacity())
			return true;
		return false;
	}
	
	public void add(Item item){
		if(item.getName().equals("Gold"))
			wallet += item.getValue();
		else if(!checkIsFull(item)){
			weight += item.getWeight();
			collisions.add(new Rectangle());
			collisions.get(collisions.size()-1).setSize(130, 14);
			items.add(item);
		}
		else
			System.out.println("Backpack full!");
	}
	public Item remove(int i){
		weight -= items.get(i).getWeight();
		return items.remove(i);
	}
	public Item move(int i){
		return items.remove(i);
	}
	public void move(Item item){
		collisions.add(new Rectangle());
		collisions.get(collisions.size()-1).setSize(130, 15);
		items.add(item);
	}
	public void display(SpriteBatch spriteBatch, BitmapFont font, Vector2 screenCoord, Player player){
		Vector3 coord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(coord);
		
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 0f, 0f, 1f);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.setColor(0.1f, 0.1f, 0.1f, 1f);
		Global.screenShapes.rect(ORIGIN_X + 5, ORIGIN_Y + 5, BOX1_WIDTH - 10, HEIGHT - 10);
		Global.screenShapes.end();
		
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(Color.DARK_GRAY);
		Global.screenShapes.rect(ORIGIN_X, ORIGIN_Y, WIDTH, HEIGHT);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y + HEIGHT - 60, ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH, ORIGIN_Y + 100, ORIGIN_X + BOX1_WIDTH + BOX2_WIDTH, ORIGIN_Y + 100);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH, ORIGIN_Y + HEIGHT - 60, ORIGIN_X + WIDTH, ORIGIN_Y + HEIGHT - 60);
		Global.screenShapes.line(ORIGIN_X + BOX1_WIDTH, ORIGIN_Y, ORIGIN_X + 150, HEIGHT + 250);
		Global.screenShapes.end();


		if(hover != null){
			spriteBatch.begin();
			spriteBatch.draw(hover.getTexture(), ORIGIN_X + 185, ORIGIN_Y + 50);
			font.draw(spriteBatch, hover.getName(), ORIGIN_X + 225, ORIGIN_Y + 70);
			font.draw(spriteBatch, "Value: " + hover.getValue(), ORIGIN_X + 170, ORIGIN_Y + 30);
			font.draw(spriteBatch, "Weight: " + hover.getWeight(), ORIGIN_X + 235, ORIGIN_Y + 30);
			spriteBatch.end();
			if(hoverCollision != null){
				Global.screenShapes.begin(ShapeType.Filled);
				Global.screenShapes.setColor(.2f, .2f, .2f, 1f);
				Global.screenShapes.rect(hoverCollision.x, hoverCollision.y, hoverCollision.width, hoverCollision.height);
				Global.screenShapes.end();
				hoverCollision = null;
			}
			hover = null;
		}
		
		
		spriteBatch.begin();
		font.draw(spriteBatch, "    ?????: " + 30, ORIGIN_X + 190, ORIGIN_Y + 225);
		font.draw(spriteBatch, " Strength: " + player.getStats().getStrength(), ORIGIN_X + 190, ORIGIN_Y + 205);
		font.draw(spriteBatch, "    Health: " + player.getStats().getMaxHitpoints(), ORIGIN_X + 190, ORIGIN_Y + 185);
		font.draw(spriteBatch, " Defense: " + player.getStats().getDefense(), ORIGIN_X + 190, ORIGIN_Y + 165);
		font.draw(spriteBatch, "Dexterity: " + player.getStats().getDexterity(), ORIGIN_X + 190, ORIGIN_Y + 145);
		font.draw(spriteBatch, "  Points: " + player.getStats().getPoints(), ORIGIN_X + 200, ORIGIN_Y + 125);

		if(player.getStats().getPoints() > 0)
			for(int i=0; i<STATS; i++){
				font.draw(spriteBatch, "+", ORIGIN_X + 285, ORIGIN_Y + 145 + i*20);
				pointCollisions[i].setPosition(ORIGIN_X + 285, ORIGIN_Y + 134 + i*20);
			}
		
		font.draw(spriteBatch, "Hunger   " + (int)(player.getHunger()*100) + "%", ORIGIN_X + WIDTH - 120, ORIGIN_Y + HEIGHT - 15);
		font.draw(spriteBatch, "Thirst        " + "90%", ORIGIN_X + WIDTH - 120, ORIGIN_Y + HEIGHT - 30);
		font.draw(spriteBatch, "Wallet  " + wallet, ORIGIN_X + BOX1_WIDTH + 10, ORIGIN_Y + HEIGHT - 15);
		font.draw(spriteBatch, "Weight " + weight + "/" + backpack.getCapacity(), ORIGIN_X + BOX1_WIDTH + 10, ORIGIN_Y + HEIGHT - 30);
		
		gear.draw(spriteBatch);
		
		for(int i=0; i<items.size(); i++){
			font.draw(spriteBatch, items.get(i).getName(), ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 10) - i*15);
			collisions.get(i).setPosition(ORIGIN_X + 10, (ORIGIN_Y + HEIGHT - 24) - i*15);
		}

		spriteBatch.end();
		
		for(int i=0; i<STATS && player.getStats().getPoints() != 0; i++){
			if(pointCollisions[i].contains(screenCoord)){
				if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
					switch(i){
					case 0:
						player.getStats().setDexterity(player.getStats().getDexterity() + 1);
						break;
					case 1:
						player.getStats().mutateDefense(1);
						break;
					case 2:
						player.getStats().setMaxHitpoints(player.getStats().getMaxHitpoints() + 1);
						player.getStats().mutateHitpoints(1);
						break;
					case 3:
						player.getStats().setStrength(player.getStats().getStrength() + 1);
						break;
					case 4:
						// Stub
						break;
					}
					player.getStats().mutatePoints(-1);
				}
			}
		}
		
		// Uncomment shape rendering for item collision debug
		/*Gdx.gl.glEnable(GL10.GL_BLEND);
        Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0f, 1f, 1f, .4f);
		*/

		setHover(gear.check(screenCoord, coord, player));
		// Item-mouse collision
		for(int i=0; i<getItems().size(); i++){
			//Global.screenShapes.rect(collisions.get(i).x, collisions.get(i).y, 130, 15);
			if(collisions.get(i).contains(screenCoord)){
				setHover(getItems().get(i), collisions.get(i));
				if(items.get(i) instanceof Food){
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0f, 0, 0, 1f);
					Global.mapShapes.rect(coord.x + 2, coord.y, Global.font.getBounds("eat").width, Global.font.getLineHeight());
					Global.mapShapes.end();
					Play.map.getSpriteBatch().begin();
					
					Global.font.draw(Play.map.getSpriteBatch(), "eat", coord.x + 2, coord.y + Global.font.getLineHeight() - 2);
					Play.map.getSpriteBatch().end();
					
					if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched()){
						player.mutateHunger(.1f);
						remove(i);
					}
				}
				else if(items.get(i) instanceof Wearable || items.get(i) instanceof Equipable){
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0f, 0, 0, 1f);
					Global.mapShapes.rect(coord.x + 2, coord.y, Global.font.getBounds("wear").width, Global.font.getLineHeight());
					Global.mapShapes.end();
					Play.map.getSpriteBatch().begin();
					
					Global.font.draw(Play.map.getSpriteBatch(), "wear", coord.x + 2, coord.y + Global.font.getLineHeight() - 2);
					Play.map.getSpriteBatch().end();
					
					if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched())
						gear.wear((Wearable) move(i), player);
				}
				if(trade != null){
					if(trade instanceof Shopkeep){
						Global.mapShapes.begin(ShapeType.Filled);
						Global.mapShapes.setColor(0f, 0, 0, 1f);
						Global.mapShapes.rect(coord.x - Global.font.getBounds("sell").width - 2, coord.y, Global.font.getBounds("sell").width, Global.font.getLineHeight());
						Global.mapShapes.end();
						Play.map.getSpriteBatch().begin();
						
						Global.font.draw(Play.map.getSpriteBatch(), "sell", coord.x - Global.font.getBounds("sell").width - 2, coord.y + Global.font.getLineHeight() - 2);
						Play.map.getSpriteBatch().end();
					}
					else if(trade instanceof Chest){
						Global.mapShapes.begin(ShapeType.Filled);
						Global.mapShapes.setColor(0f, 0, 0, 1f);
						Global.mapShapes.rect(coord.x - Global.font.getBounds("move").width - 2, coord.y, Global.font.getBounds("move").width, Global.font.getLineHeight());
						Global.mapShapes.end();
						Play.map.getSpriteBatch().begin();
						
						Global.font.draw(Play.map.getSpriteBatch(), "move", coord.x - Global.font.getBounds("move").width - 2, coord.y + Global.font.getLineHeight() - 2);
						Play.map.getSpriteBatch().end();
					}
				}
				else{
					Global.mapShapes.begin(ShapeType.Filled);
					Global.mapShapes.setColor(0f, 0, 0, 1f);
					Global.mapShapes.rect(coord.x - Global.font.getBounds("drop").width - 2, coord.y, Global.font.getBounds("drop").width, Global.font.getLineHeight());
					Global.mapShapes.end();
					Play.map.getSpriteBatch().begin();
					
					Global.font.draw(Play.map.getSpriteBatch(), "drop", coord.x - Global.font.getBounds("drop").width - 2, coord.y + Global.font.getLineHeight() - 2);
					Play.map.getSpriteBatch().end();
				}
				if(Gdx.input.isButtonPressed(Buttons.LEFT) && Gdx.input.justTouched()){
					// In essence -> sells the item, then adds it to shopkeep's inventory
					if(trade instanceof Shopkeep){
						AddTradeItemPacket tradeItem = new AddTradeItemPacket();
						System.out.println(trade.ID);
						tradeItem.ID = trade.getID();
						tradeItem.item = new ItemPacket(items.get(i).getName());
						Play.clientWrapper.client.sendUDP(tradeItem);
						wallet += getItems().get(i).getValue();
						((Shopkeep) trade).add(remove(i));
					}
					// In essence -> adds item to chest and removes from inventory
					else if(trade instanceof Chest){
						AddTradeItemPacket tradeItem = new AddTradeItemPacket();
						tradeItem.ID = trade.getID();
						tradeItem.item = new ItemPacket(items.get(i).getName());
						Play.clientWrapper.client.sendUDP(tradeItem);
						((Chest) trade).add(remove(i));
					}
					else
						remove(i);
				}
			}
		}
		/*Global.screenShapes.end();
	    Gdx.gl.glDisable(GL10.GL_BLEND);*/
		
	}
	
	public Texture getBackpack(){
		return backpack.getTexture();
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

class Gear{
	private Item[] items;
	// 0 - head, 1 - body, 2 - arms, 3 - legs, 4 - feet, 5 - hand, 6 - ring
	private Texture[] unused;
	private String[] names;
	private final int SLOTS = 7;
	private int originX, originY;
	private Rectangle[] collisions;
	
	public Gear(int originX, int originY){
		collisions = new Rectangle[SLOTS];
		unused = new Texture[SLOTS];
		items = new Wearable[SLOTS];
		names = new String[SLOTS];
		
		this.originX = originX;
		this.originY = originY;
		
		unused[0] = new Texture(Gdx.files.internal("img/head.png"));
		unused[1] = new Texture(Gdx.files.internal("img/body.png"));
		unused[2] = new Texture(Gdx.files.internal("img/arms.png"));
		unused[3] = new Texture(Gdx.files.internal("img/legs.png"));
		unused[4] = new Texture(Gdx.files.internal("img/feet.png"));
		unused[5] = new Texture(Gdx.files.internal("img/hand.png"));
		unused[6] = new Texture(Gdx.files.internal("img/ring.png"));
		
		for(int i=0; i<SLOTS; i++){
			collisions[i] = new Rectangle();
			collisions[i].setSize(32, 32);
		}

		collisions[0].setPosition(originX + 80, originY + 165);
		collisions[1].setPosition(originX + 80, originY + 125);
		collisions[2].setPosition(originX + 40, originY + 125);
		collisions[3].setPosition(originX + 80, originY + 85);
		collisions[4].setPosition(originX + 80, originY + 45);
		collisions[5].setPosition(originX + 120, originY + 125);
		collisions[6].setPosition(originX + 40, originY + 165);
		
		names[0] = "Head";
		names[1] = "Body";
		names[2] = "Arms";
		names[3] = "Legs";
		names[4] = "Feet";
		names[5] = "Hand";
		names[6] = "Ring";
	}
	public Item check(Vector2 screenCoord, Vector3 coord, Player player) {
		for(int i=0; i<SLOTS; i++)
			if(collisions[i].contains(screenCoord) && this.items[i] != null){
				Global.mapShapes.begin(ShapeType.Filled);
				Global.mapShapes.setColor(0f, 0, 0, 1f);
				Global.mapShapes.rect(coord.x, coord.y, Global.font.getBounds("remove").width, Global.font.getLineHeight());
				Global.mapShapes.end();
				
				Play.map.getSpriteBatch().begin();
				Global.font.draw(Play.map.getSpriteBatch(), "remove", coord.x, coord.y + Global.font.getLineHeight() - 2);
				Play.map.getSpriteBatch().end();
				
				if(Gdx.input.isButtonPressed(Buttons.RIGHT) && Gdx.input.justTouched()){
					player.getStats().mutateDefense(-((Wearable) (items[i])).getDefense());
					player.getInventory().move(items[i]);
					this.items[i] = null;
				}
				return this.items[i] != null ? this.items[i] : null;
			}
		return null;			
	}
	public void draw(SpriteBatch spriteBatch) {
		spriteBatch.draw(items[0] != null ? items[0].getTexture() : unused[0], originX + 80, originY + 165);
		spriteBatch.draw(items[1] != null ? items[1].getTexture() : unused[1], originX + 80, originY + 125);
		spriteBatch.draw(items[2] != null ? items[2].getTexture() : unused[2], originX + 40, originY + 125);
		spriteBatch.draw(items[3] != null ? items[3].getTexture() : unused[3], originX + 80, originY + 85);
		spriteBatch.draw(items[4] != null ? items[4].getTexture() : unused[4], originX + 80, originY + 45);
		spriteBatch.draw(items[5] != null ? items[5].getTexture() : unused[5], originX + 120, originY + 125);
		spriteBatch.draw(items[6] != null ? items[6].getTexture() : unused[6], originX + 40, originY + 165);
	}
	
	public void wear(Wearable item, Player player){
		for(int i=0; i<SLOTS; i++)
			if(names[i] == item.getType()){
				if(items[i] != null){
					player.getStats().mutateDefense(-((Wearable) (items[i])).getDefense());
					player.getInventory().move(items[i]);
				}
				items[i] = item;
				player.getStats().mutateDefense(((Wearable) (items[i])).getDefense());
			}
	}
}