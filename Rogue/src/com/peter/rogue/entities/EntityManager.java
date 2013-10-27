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

    private LinkedList<NPC> npcs;
    private LinkedList<Entity> objects;
    
    private int randX, randY;
    private Player player;
    private UI ui = new UI();
    
    public EntityManager(){
    	npcs = new LinkedList<NPC>();
    	objects = new LinkedList<Entity>();
    }
    
	public void draw(){
		for(int i=0; i<objects.size(); i++){
			if(objects.get(i).isPickedUp())
				objects.remove(i);
			else
				objects.get(i).draw(Global.renderer.getSpriteBatch());
		}
		for(int i=0; i<npcs.size(); i++){
			//if(npcs.get(i).getX() > (player.getX() + player.getWidth()/2) - player.getViewDistance() && npcs.get(i).getX() < (player.getX() + player.getWidth()/2) + player.getViewDistance() &&
			//		npcs.get(i).getY() > (player.getY() + player.getHeight()/2) - player.getViewDistance() && npcs.get(i).getY() < (player.getY() + player.getHeight()/2) + player.getViewDistance())
			if(npcs.get(i).getStats().getHitpoints() <= 0){
				npcs.get(i).remove();
				npcs.remove(i);
			}
			else
				npcs.get(i).draw(Global.renderer.getSpriteBatch());
			//else
			//	npcs.get(i).update(Gdx.graphics.getDeltaTime());
		}

		player.draw(Global.renderer);
		
		Global.renderer.setView(Global.camera);
		ui.draw(Global.renderer, player);
		
		Animate.pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(Animate.pos);
		
		if(Animate.pos.y + 32 > (Global.camera.position.y - Global.SCREEN_HEIGHT/3 + 12))
			player.setInformation(Global.data.getCursor(player.getMapID(Animate.pos.x, Animate.pos.y)));
		else
			player.setInformation("null");
		
		Global.camera.project(Animate.pos);
		Global.shapeRenderer.begin(ShapeType.Filled);
		Global.shapeRenderer.setColor(0, 0, 0, 1f);
		Global.shapeRenderer.rect(Animate.pos.x, Animate.pos.y, Entity.font.getBounds(player.getInformation()).width, Entity.font.getLineHeight());
		Global.shapeRenderer.end();
		Animate.pos = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
		Global.camera.unproject(Animate.pos);
		Global.renderer.getSpriteBatch().begin();
		Entity.font.draw(Global.renderer.getSpriteBatch(), player.getInformation(), Animate.pos.x, Animate.pos.y + Entity.font.getLineHeight());
    }
    
    public void init(){
    	
    	for(int i=0; i<Global.data.getCitizens(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			npcs.add(new NPC("c_.png", "Citizen"));
			npcs.getLast().setPosition(randX, randY);
			npcs.getLast().setMap(npcs.getLast().getX(), npcs.getLast().getY(), npcs.getLast().getEntry());
		}
    	for(int i=Global.data.getCitizens(); i<Global.data.getCitizens() + Global.data.getShopkeeps(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			npcs.add(new Shopkeep("s_.png", "Shopkeep"));
			npcs.getLast().setPosition(randX, randY);
			npcs.getLast().setMap(npcs.getLast().getX(), npcs.getLast().getY(), npcs.getLast().getEntry());
		}
    	for(int i=Global.data.getCitizens() + Global.data.getShopkeeps(); i<Global.data.getNPCTotal(); i++){
			randX = Global.rand(13, 3);
			randY = Global.rand(7, 3);
			npcs.add(new Monster("w.png", "Worm"));
			npcs.getLast().setPosition(randX, randY);
			npcs.getLast().setMap(npcs.getLast().getX(), npcs.getLast().getY(), npcs.getLast().getEntry());
    	}
    	
    	for(int i=0; i<npcs.size(); i++){
    		Global.data.add(npcs.get(i).getID(), npcs.get(i));
    	}
    	
		player = new Player("at.png", "Player");
		player.setPosition(18, 7);

		objects.add(new Chest());
		objects.getLast().setPosition(4, 4);
		Global.data.add(objects.getLast().getID(), objects.getLast());
		

		objects.add(new Chest());
		objects.getLast().setPosition(6, 4);
		Global.data.add(objects.getLast().getID(), objects.getLast());
		
		objects.add(Food.BREAD);
		objects.getLast().setPosition(8, 18);
		
		Global.data.add(objects.getLast().getID(), objects.getLast());
		
		objects.add(Head.HAT);
		objects.getLast().setPosition(9, 18);
		
		Global.data.add(objects.getLast().getID(), objects.getLast());

		Global.data.add(player.getID(), player);
		
		Gdx.input.setInputProcessor(player);
	}
    
    public void dispose(){
		for(int i=0; i<Global.data.getNPCTotal(); i++)
			npcs.get(i).getTexture().dispose();
		player.getTexture().dispose();
		ui.dispose();
		Global.shapeRenderer.dispose();
    }
}