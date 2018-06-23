package com.smeanox.games.aj3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.smeanox.games.aj3.AJ3Colors;
import com.smeanox.games.aj3.Consts;
import com.smeanox.games.aj3.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameScreen implements Screen {

    private float width, height;
    private OrthographicCamera camera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private Texture bg, map;
    private BitmapFont font16;
    private BitmapFont font24;
    private BitmapFont font32;

    public GameScreen() {
        camera = new OrthographicCamera(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        width = Consts.DESIGN_WIDTH;
        height = Consts.DESIGN_HEIGHT;

        bg = new Texture("img/bg.png");
        map = new Texture("img/map.png");
        font16 = new BitmapFont(Gdx.files.internal("fnt/dejavu-16.fnt"));
        font24 = new BitmapFont(Gdx.files.internal("fnt/dejavu-24.fnt"));
        font32 = new BitmapFont(Gdx.files.internal("fnt/dejavu-32.fnt"));

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
    }

    public void update(float delta) {
        World.w.doTicks(delta);
    }

    public static String formatMoney(long money) {
        StringBuilder sb = new StringBuilder();
        List<Long> parts = new ArrayList<Long>();
        while (money > 0) {
            parts.add(money % 1000);
            money /= 1000;
        }
        sb.append(parts.get(parts.size()-1));
        for(int i = parts.size() - 2; i >= 0; i--) {
            sb.append("'");
            sb.append(String.format(Locale.US, "%03d", parts.get(i)));
        }
        sb.append(" $");
        return sb.toString();
    }

    public void renderTextAligned(SpriteBatch spriteBatch, BitmapFont font, String text, float x, float y, float xAlign, float yAlign) {
        GlyphLayout layout = new GlyphLayout(font24, text);
        font.draw(spriteBatch, text, x + xAlign * layout.width, y + yAlign * layout.height);
    }

    @Override
    public void render(float delta) {
        update(delta);

        spriteBatch.setProjectionMatrix(camera.combined);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setColor(AJ3Colors.white.c);
        spriteBatch.begin();
        spriteBatch.draw(map, 0, 0);
        spriteBatch.draw(bg, 0, 0, width, height);

        font24.setColor(AJ3Colors.black.c);
        font24.draw(spriteBatch, Consts.GAME_NAME, 10, height - 10);
        renderTextAligned(spriteBatch, font24, formatMoney(World.w.money), width - 10, height - 10, -1, 0);
        spriteBatch.end();
    }

    @Override
    public void resize(int newWidth, int newHeight) {
        width = Consts.DESIGN_HEIGHT * newWidth / ((float) newHeight);
        camera.viewportWidth = width;
        camera.position.set(width / 2, height / 2, 0);
        camera.update();
        // System.out.println("New viewport size: " + width + " x " + height);
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
