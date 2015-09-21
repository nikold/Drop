package com.badlogic.gamescreen;

import java.util.Iterator;

import com.badlogic.gdx.Screen;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.TimeUtils;

public class GameScreen implements Screen {

	enum gameState {
		PAUSED, PLAYED, MENU
	}

	Texture dropImage;
	Texture bucketImage;
	Texture resumeImage;
	Texture backgroundImage;

	Sound dropSound;
	Music rainMusic;

	SpriteBatch batch;
	Sprite resumeSprite;
	Sprite backgroundSprite;

	OrthographicCamera camera;

	Rectangle bucket;
	Rectangle resume;
	Array<Rectangle> rainDrops;

	Vector3 touchPos;
	BitmapFont font;

	long lastDropTime;
	boolean GAME_PAUSED = false;
	long score = 0;

	public GameScreen(final Drop gam) {

		font = new BitmapFont();
		dropImage = new Texture(Gdx.files.internal("droplet.png"));
		bucketImage = new Texture(Gdx.files.internal("bucket.png"));
		resumeImage = new Texture(Gdx.files.internal("button_grey_play.png"));
		backgroundImage = new Texture(Gdx.files.internal("background.jpg"));
		dropSound = Gdx.audio.newSound(Gdx.files
				.internal("30341__junggle__waterdrop24.wav"));
		rainMusic = Gdx.audio.newMusic(Gdx.files
				.internal("28283__acclivity__undertreeinrain.mp3"));
		rainMusic.setLooping(true);

		batch = new SpriteBatch();

		camera = new OrthographicCamera();
		camera.setToOrtho(false, 800, 480);

		bucket = new Rectangle();
		bucket.x = 800 / 2 - 64 / 2;
		bucket.y = 20;
		bucket.width = 64;
		bucket.height = 64;

		resumeSprite = new Sprite(resumeImage);
		// resumeSprite.setPosition(800/2 , 480/2);
		resumeSprite.setCenter(800 / 2, 480 / 2);
		backgroundSprite = new Sprite(backgroundImage);

		touchPos = new Vector3();
		rainDrops = new Array<Rectangle>();
		SetRainDrops();
	}

	private void SetRainDrops() {
		Rectangle rainDrop = new Rectangle();
		rainDrop.x = MathUtils.random(0, 800 - 64);
		rainDrop.y = 480;
		rainDrop.width = 64;
		rainDrop.height = 64;
		rainDrops.add(rainDrop);
		lastDropTime = TimeUtils.nanoTime();
	}

	@Override
	public void render(float delta) {
		if (!GAME_PAUSED) {
			// rainMusic.play();
			Gdx.gl.glClearColor(0, 0, 0.2f, 1);
			Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
			batch.setProjectionMatrix(camera.combined);

			batch.begin();
			backgroundSprite.draw(batch);
			batch.draw(bucketImage, bucket.x, bucket.y);
			for (Rectangle rectangle : rainDrops) {

				batch.draw(dropImage, rectangle.x, rectangle.y);
			}
			font.draw(batch, "score: " + score, 700, 450);
			batch.end();

			if (Gdx.input.isTouched()) {
				touchPos.set(Gdx.input.getX(), Gdx.input.getY(), 0);
				camera.unproject(touchPos);
				bucket.x = touchPos.x - 64 / 2;
			}

			if (Gdx.input.isKeyPressed(Keys.LEFT))
				bucket.x -= 200 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.RIGHT))
				bucket.x += 200 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.UP))
				bucket.y += 200 * Gdx.graphics.getDeltaTime();
			if (Gdx.input.isKeyPressed(Keys.DOWN))
				bucket.y -= 200 * Gdx.graphics.getDeltaTime();

			if (bucket.x < 0)
				bucket.x = 0;
			if (bucket.x > 800 - 64)
				bucket.x = 800 - 64;

			if (bucket.y < 0)
				bucket.y = 0;
			if (bucket.y > 480 - 64)
				bucket.y = 480 - 64;

			if (TimeUtils.nanoTime() - lastDropTime > 1000000000)
				SetRainDrops();

			Iterator<Rectangle> iter = rainDrops.iterator();
			while (iter.hasNext()) {
				Rectangle raindrop = iter.next();
				raindrop.y -= 200 * Gdx.graphics.getDeltaTime();
				if (raindrop.y + 64 < 0)
					iter.remove();
				if (raindrop.overlaps(bucket)) {
					dropSound.play();
					iter.remove();
					score++;
					System.out.println("score: " + score);
				}
			}

		} else {
			// rainMusic.stop();
			batch.begin();
			resumeSprite.draw(batch);
			batch.end();
			if (Gdx.input.isTouched()) {
				camera.unproject(touchPos.set(Gdx.input.getX(),
						Gdx.input.getY(), 0));
				if (resumeSprite.getBoundingRectangle().contains(touchPos.x,
						touchPos.y)) {
					GAME_PAUSED = false;
				}
			}

		}

	}

	@Override
	public void dispose() {
		// высвобождение всех нативных ресурсов
		dropImage.dispose();
		bucketImage.dispose();
		dropSound.dispose();
		rainMusic.stop();
		rainMusic.dispose();
		batch.dispose();
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub
		rainMusic.stop();
		GAME_PAUSED = true;
	}

	@Override
	public void resume() {
		// TODO Auto-generated method stub
	}

	@Override
	public void show() {
		// TODO Auto-generated method stub

	}

	@Override
	public void resize(int width, int height) {
		// TODO Auto-generated method stub

	}

	@Override
	public void hide() {
		// TODO Auto-generated method stub

	}
}
