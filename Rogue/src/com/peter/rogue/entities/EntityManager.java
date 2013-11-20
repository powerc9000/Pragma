package com.peter.rogue.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;
import com.peter.rogue.inventory.Chest;
import com.peter.rogue.inventory.Food;
import com.peter.rogue.inventory.Wearable;
import com.peter.rogue.views.UI;

public class EntityManager{
	
    private Player player;
    private UI ui;
	private Vector3 mapCoord;
    private int randX, randY;
    
    
    public EntityManager(){
		player = new Player("at.png");
		player.setPosition(28, 7);

		ui = new UI(player);
    }
    
	public void draw(){

		Entity.map.getSpriteBatch().begin();
		Entity.map.draw();
		player.draw(Entity.map.getSpriteBatch());
		Entity.map.getSpriteBatch().end();

		// If near edge of map then don't update respective axis
		if(player.getX() > Global.SCREEN_WIDTH/2 - 32*3 && player.getX() < Entity.map.WIDTH*32 - 18*32)
			Global.camera.position.x = player.getX() + player.getWidth() / 2;
		if(player.getY() > Global.SCREEN_HEIGHT/2 - 32*6 && player.getY() < Entity.map.HEIGHT*32 - 9*32)
			Global.camera.position.y = player.getY() + player.getHeight() / 2;
		Global.camera.update();
		Global.mapShapes.setProjectionMatrix(Global.camera.combined);
		Entity.map.getSpriteBatch().setProjectionMatrix(Global.camera.combined);
	    Entity.map.setView(Global.camera);
	    
		player.light();
		ui.draw(player, Entity.map.npcs);
		
		mapCoord = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(mapCoord);
		
		if(!player.isMenuActive()){
			player.setInformation(Entity.map.cursor(Entity.map.getMark(mapCoord.x, mapCoord.y), mapCoord.x, mapCoord.y));
			Global.mapShapes.begin(ShapeType.Filled);
			Global.mapShapes.setColor(0, 0, 0, 1f);
			Global.mapShapes.rect(mapCoord.x, mapCoord.y, Global.font.getBounds(player.getInformation()).width, Global.font.getLineHeight());
			Global.mapShapes.end();
			Entity.map.getSpriteBatch().begin();
			Global.font.draw(Entity.map.getSpriteBatch(), player.getInformation(), mapCoord.x, mapCoord.y + Global.font.getLineHeight() - 2);
			Entity.map.getSpriteBatch().end();
		}
		else
			player.setInformation("");
		
    }
    
    public void init(){    	
    	Global.multiplexer.addProcessor(player);
		Gdx.input.setInputProcessor(Global.multiplexer);
		
		Entity.map.chests.add(new Chest());
		Entity.map.chests.get(Entity.map.chests.size()-1).setPosition(4, 4);

		Entity.map.chests.add(new Chest());
		Entity.map.chests.get(Entity.map.chests.size()-1).setPosition(6, 4);

		Entity.map.items.add(new Food(Food.BREAD));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(8, 32);
		
		Entity.map.items.add(new Wearable(Wearable.HAT));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(9, 32);
		
		Entity.map.items.add(new Wearable(Wearable.HAT));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(9, 31);
		
		Entity.map.items.add(new Wearable(Wearable.RING));
		Entity.map.items.get(Entity.map.items.size()-1).setPosition(9, 34);
		
		for(int i=0; i<Entity.map.getData().getCitizens(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			Entity.map.npcs.add(new Citizen("c_.png"));
			Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
		}
		
		randX = Global.rand(13, 3);
		randY = Global.rand(7, 3);
		Entity.map.npcs.add(Shopkeep.Bartender);
		Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
		
		randX = Global.rand(13, 3);
		randY = Global.rand(7, 3);
		Entity.map.npcs.add(Shopkeep.Shopkeep);
		Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
			
    	for(int i=0; i<Entity.map.getData().getMonsters(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			Entity.map.npcs.add(new Worm("tilda.png"));
			Entity.map.npcs.get(Entity.map.npcs.size()-1).setPosition(randX, randY);
    	}
	}
    
    public void dispose(){
		player.getTexture().dispose();
		ui.dispose();
		Global.mapShapes.dispose();
		Global.screenShapes.dispose();
		Global.gothicFont.dispose();
		Global.font.dispose();
    }
}
