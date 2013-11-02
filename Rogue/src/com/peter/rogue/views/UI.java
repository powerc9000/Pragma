package com.peter.rogue.views;

import java.util.ArrayList;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer.ShapeType;
import com.peter.rogue.Global;
import com.peter.rogue.entities.Entity;
import com.peter.rogue.entities.NPC;
import com.peter.rogue.entities.Player;
import com.peter.rogue.map.Map;

public class UI{
    private BitmapFont gothicFont;
    private BitmapFont font;
    
	public UI(){
		font = new BitmapFont();
		gothicFont = new BitmapFont(Gdx.files.internal("fonts/Cardinal.fnt"), Gdx.files.internal("fonts/Cardinal.png"), false);
	}
	
	public void draw(Player player, ArrayList<NPC> npcs){
		
		// Draws the messages and statuses on top of everything
		for(int i=0; i<npcs.size(); i++){
			
			if(npcs.get(i).messageFlag){
				Global.mapShapes.begin(ShapeType.Filled);
				Global.mapShapes.setColor(0, 0f, 0, 1f);
				Global.mapShapes.rect(npcs.get(i).getX(), npcs.get(i).getY() - 17, font.getBounds(npcs.get(i).getMessage()).width, font.getLineHeight());
				Global.mapShapes.end();
			}
			if(npcs.get(i).statusFlag){
				Global.mapShapes.begin(ShapeType.Filled);
				Global.mapShapes.setColor(.4f, 0f, 0f, 1f);
				Global.mapShapes.circle(npcs.get(i).getX(), npcs.get(i).getY() + 20, font.getBounds(npcs.get(i).getStatus().toString()).width);
				Global.mapShapes.end();
			}
			Entity.map.getSpriteBatch().begin();
			font.draw(Entity.map.getSpriteBatch(), npcs.get(i).getMessage(), npcs.get(i).getX(), npcs.get(i).getY());
			if(npcs.get(i).getStatus() != 0){
				font.draw(Entity.map.getSpriteBatch(), npcs.get(i).getStatus().toString(), npcs.get(i).getX() - 8, npcs.get(i).getY() + 26);
			}
			Entity.map.getSpriteBatch().end();
		}
		
		if(player.messageFlag){
			Global.mapShapes.begin(ShapeType.Filled);
			Global.mapShapes.setColor(0, 0, 0, 1f);
			Global.mapShapes.rect(player.getX(), player.getY() - 17, font.getBounds(player.getMessage()).width, font.getLineHeight());
			Global.mapShapes.end();
		}
		
		if(player.statusFlag){
			Global.mapShapes.begin(ShapeType.Filled);
			Global.mapShapes.setColor(.4f, 0f, 0f, 1f);
			Global.mapShapes.circle(player.getX(), player.getY() + 20, font.getBounds(player.getStatus().toString()).width);
			Global.mapShapes.end();
		}

		Entity.map.getSpriteBatch().begin();
		font.draw(Entity.map.getSpriteBatch(), player.getMessage(), player.getX(), player.getY());
		if(player.getStatus() != 0){
			font.draw(Entity.map.getSpriteBatch(), player.getStatus().toString(), player.getX() - 8, player.getY() + 26);
		}
		Entity.map.getSpriteBatch().end();
		
		// If near edge of map then don't update respective axis
		if(player.getX() > Global.SCREEN_WIDTH/2 - 32*3 && player.getX() < Entity.map.WIDTH*32 - 18*32)
			Global.camera.position.x = player.getX() + player.getWidth() / 2;
		if(player.getY() > Global.SCREEN_HEIGHT/2 - 32*6 && player.getY() < Entity.map.HEIGHT*32 - 9*32)
			Global.camera.position.y = player.getY() + player.getHeight() / 2;
		Global.camera.update();
		
		Global.screenShapes.begin(ShapeType.Filled);
		Global.screenShapes.setColor(0, 0, 0, 1f);
		Global.screenShapes.rect(0, 0, Global.SCREEN_WIDTH, 100f);
		Global.screenShapes.end();
		Global.screenShapes.begin(ShapeType.Line);
		Global.screenShapes.setColor(1f, .84f, 0, 1f);
		Global.screenShapes.line(0, 100f, Global.SCREEN_WIDTH, 100f);
		Global.screenShapes.end();
		
		display(Global.screen, player);
		
		if(player.isMenuActive()){
			if(player.getMenu().equals("Inventory"))
				player.getInventory().display(Global.screen, font);
			
			else if(player.getMenu().equals("Chest")){
				player.getInventory().display(Global.screen, font);
				player.getMenuObject().display(Global.screen, font);
			}
		}
		
		update(Gdx.graphics.getDeltaTime());
	}
	
	public void update(float delta){
	}
	
	public void display(SpriteBatch spriteBatch, Player player){
		spriteBatch.begin();
		spriteBatch.draw(new Texture(Gdx.files.internal("img/guiLeftTest.png")),  0, 0);
		spriteBatch.draw(new Texture(Gdx.files.internal("img/guiRightTest.png")),  Global.SCREEN_WIDTH - 179, 0);
		
		spriteBatch.draw(player.getPicture(),  50, 15);
		
		gothicFont.setScale(1f);
		gothicFont.draw(spriteBatch, player.getName(), 130, 90);
		gothicFont.setScale(.7f);
		gothicFont.draw(spriteBatch, "Level: " + player.getStats().getLevel() + player.getStats().getLevelPending(), 130, 50);
		gothicFont.draw(spriteBatch, "Hitpoints: " + player.getStats().getHitpoints() + "/" + player.getStats().getMaxHitpoints(), 280, 50);
		gothicFont.draw(spriteBatch, "Strenght:  " + player.getStats().getStrength(), 280, 80);
		gothicFont.draw(spriteBatch, "Dexterity:  " + player.getStats().getDexterity(), 480, 50);
		gothicFont.draw(spriteBatch, "Experience: " + player.getStats().getExperience(), 480, 80);
		gothicFont.draw(spriteBatch, "Demeanor: ", 720, 80);
		if(player.isHostile())
			gothicFont.draw(spriteBatch, "Hostile", 805, 80);
		else
			gothicFont.draw(spriteBatch, "Friendly", 805, 80);
		gothicFont.draw(spriteBatch, "Floor: " + Map.getFloor(), 720, 50);
		spriteBatch.draw(player.getInventory().getBackpack(),  1020, 30);
		spriteBatch.end();
	}
	
	public void dispose(){
		gothicFont.dispose();
	}

}
