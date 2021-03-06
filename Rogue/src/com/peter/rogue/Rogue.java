package com.peter.rogue;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.peter.rogue.screens.Death;
import com.peter.rogue.screens.Play;
import com.peter.rogue.screens.Splash;

public class Rogue extends Game{
	
	public static final String TITLE = "Rogue", VERSION = "0.4.9";

	Splash splash;
	Play play;
	
	@Override
	public void create() {
		
		splash = new Splash(this);
		play = new Play(this);
		setScreen(new Play(this));
	}

	@Override
	public void dispose() {
		super.dispose();
	}

	@Override
	public void render() {
		super.render();
		
		if(Global.gameOver){
			setScreen(new Death(this));
		    if(Gdx.input.isKeyPressed(Keys.ENTER)){
		    	setScreen(new Play(this));
		    	Global.gameOver = false;
		    }
		}
	}

	@Override
	public void resize(int width, int height) {
		super.resize(width, height);
	}

	@Override
	public void pause() {
		super.pause();
	}

	@Override
	public void resume() {
		super.resume();
	}
}
