package com.peter.rogue.entities;

import java.util.LinkedList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.badlogic.gdx.math.Vector3;
import com.peter.rogue.Global;
import com.peter.rogue.inventory.Chest;
import com.peter.rogue.inventory.Food;
import com.peter.rogue.inventory.Head;
import com.peter.rogue.views.UI;

public class EntityManager {

    private LinkedList<Entity> objects;
    
    private int randX, randY;
    private Player player;
    private UI ui = new UI();
    
    
    public EntityManager(){
    	objects = new LinkedList<Entity>();

		player = new Player("at.png");
		player.setPosition(18, 7);
		
		

		objects.add(new Chest());
		objects.getLast().setPosition(4, 4);
		
		objects.add(new Chest());
		objects.getLast().setPosition(6, 4);
		
		objects.add(Food.BREAD);
		objects.getLast().setPosition(8, 18);
		
		objects.add(Head.HAT);
		objects.getLast().setPosition(9, 18);
    }
    
	public void draw(){
		
		Entity.map.getSpriteBatch().begin();
		for(int i=0; i<objects.size(); i++){
			if(objects.get(i).isPickedUp()){
				objects.remove(i);
			}
			else
				objects.get(i).draw(Entity.map.getSpriteBatch());
		}
		
		for(int i=0; i<NPC.npcs.size(); i++)
			NPC.npcs.get(i).draw(Entity.map.getSpriteBatch());

		Entity.map.getSpriteBatch().end();
		player.light();
		
		Entity.map.getSpriteBatch().begin();
		player.draw(Entity.map.getSpriteBatch());
		Entity.map.getSpriteBatch().end();
		
		
		ui.draw(player, NPC.npcs);
		
		Animate.pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(Animate.pos);

		if(Animate.pos.y + 32 > (Global.camera.position.y - Global.SCREEN_HEIGHT/3 + 12))
			player.setInformation(Entity.map.cursor(Entity.map.getMark(Animate.pos.x, Animate.pos.y)));
		else
			player.setInformation("");
		
		Global.camera.project(Animate.pos);
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0, 0, 0, 1f);
		Global.screenShapes.rect(Animate.pos.x, Animate.pos.y, Entity.font.getBounds(player.getInformation()).width, Entity.font.getLineHeight());
		Global.screenShapes.end();
		Global.screen.begin();
		Entity.font.draw(Global.screen, player.getInformation(), Animate.pos.x, Animate.pos.y + Entity.font.getLineHeight() - 2);
		Global.screen.end();
    }
    
    public void init(){
    	for(int i=0; i<Entity.map.getData().getCitizens(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			NPC.npcs.add(new Citizen("c_.png"));
			NPC.npcs.get(NPC.npcs.size()-1).setPosition(randX, randY);
		}
    	for(int i=0; i<Entity.map.getData().getShopkeeps(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			NPC.npcs.add(new Shopkeep("s_.png"));
			NPC.npcs.get(NPC.npcs.size()-1).setPosition(randX, randY);
		}
    	for(int i=0; i<Entity.map.getData().getMonsters(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			NPC.npcs.add(new Worm("w.png"));
			NPC.npcs.get(NPC.npcs.size()-1).setPosition(randX, randY);
    	}
    	
		Gdx.input.setInputProcessor(player);
	}
    
    /*public void purge(){
    	Entity.map.clean(player.getID());
		npcs.clear();
		objects.clear();
		player.setNewMap();
		Entity.clearMap();
		
		init();
    }*/
    //(float)Math.cos((2*Math.PI*i)/rays.size()), (float)Math.sin((2*Math.PI*i)/rays.size())
    
    public void dispose(){
		for(int i=0; i<NPC.npcs.size(); i++)
			NPC.npcs.get(i).getTexture().dispose();
		player.getTexture().dispose();
		ui.dispose();
		Global.mapShapes.dispose();
		Global.screenShapes.dispose();
    }
}
