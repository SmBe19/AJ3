package com.smeanox.games.aj3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.smeanox.games.aj3.AJ3Colors;
import com.smeanox.games.aj3.Consts;

public class GameScreen implements Screen {

    private float width, height;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private Texture bg;
    private BitmapFont font16;
    private BitmapFont font24;
    private BitmapFont font32;

    public GameScreen() {
        camera = new OrthographicCamera(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        width = Consts.DESIGN_WIDTH;
        height = Consts.DESIGN_HEIGHT;

        bg = new Texture("img/bg.png");
        font16 = new BitmapFont(Gdx.files.internal("fnt/dejavu-16.fnt"));
        font24 = new BitmapFont(Gdx.files.internal("fnt/dejavu-24.fnt"));
        font32 = new BitmapFont(Gdx.files.internal("fnt/dejavu-32.fnt"));

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
    }

    @Override
    public void render(float delta) {
        spriteBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setColor(AJ3Colors.white.c);
        spriteBatch.begin();
        spriteBatch.draw(bg, 0, 0, width, height);

        font24.setColor(AJ3Colors.black.c);
        font24.draw(spriteBatch, Consts.GAME_NAME, 10, height - 10);
        font24.draw(spriteBatch, "1'000'000 $", width * 0.8f, height - 10);
        spriteBatch.end();
    }

    @Override
    public void resize(int newWidth, int newHeight) {
        width = Consts.DESIGN_HEIGHT * newWidth / ((float) newHeight);
        camera.viewportWidth = width;
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        System.out.println("New viewport size: " + width + " x " + height);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
