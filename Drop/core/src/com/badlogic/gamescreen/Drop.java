package com.badlogic.gamescreen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Drop extends Game {

	SpriteBatch batch;
	BitmapFont font;
	Color color;

	public void create() {
		batch = new SpriteBatch();
		font = new BitmapFont();
		font.setColor(Color.CYAN);
		font.getData().setScale(1.2f,1.2f);
		this.setScreen(new MainMenuScreen(this));
		
	}

	public void render() {
		super.render();
	}

	public void dispose() {
		batch.dispose();
		font.dispose();
	}

}
