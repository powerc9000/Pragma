package com.peter.rogue.screens;

import java.io.IOException;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL10;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;
import com.peter.rogue.Global;
import com.peter.rogue.Rogue;
import com.peter.rogue.entities.*;

public class Play implements Screen{

	Rogue game;
	
	private OrthogonalTiledMapRenderer renderer;
    private EntityManager entityManager;
    
	public Play(Rogue game){
		this.game = game;
	}
	
	@Override
	public void render(float delta) {

		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
		Gdx.gl.glEnable(GL10.GL_BLEND);
	    Gdx.gl.glBlendFunc(GL10.GL_SRC_ALPHA, GL10.GL_ONE_MINUS_SRC_ALPHA);

		renderer.setView(Global.camera);
		renderer.render();
		renderer.getSpriteBatch().begin();
		entityManager.draw(renderer);
		renderer.getSpriteBatch().end();
	}

	@Override
	public void resize(int width, int height) {
		Global.camera.viewportWidth = width;
		Global.camera.viewportHeight = height;
	}
	
	@Override
	public void show() {
		renderer = new OrthogonalTiledMapRenderer(Global.map);
		Global.camera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		
		try{
			entityManager = new EntityManager();
		}
		catch(IOException e){};
		
		entityManager.init();
	}

	@Override
	public void hide() {
	}

	@Override
	public void pause() {
	}

	@Override
	public void resume() {
	}

	@Override
	public void dispose() {
		renderer.dispose();
		entityManager.dispose();
	}

}
