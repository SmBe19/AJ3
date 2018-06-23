package com.smeanox.games.aj3.screen;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.smeanox.games.aj3.AJ3Colors;
import com.smeanox.games.aj3.Consts;
import com.smeanox.games.aj3.world.City;
import com.smeanox.games.aj3.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameScreen implements Screen {

    private float width, height;
    private OrthographicCamera uiCamera, mapCamera;
    private SpriteBatch spriteBatch;
    private ShapeRenderer shapeRenderer;

    private Texture bg, map;
    private Texture[] cities;
    private BitmapFont font16;
    private BitmapFont font24;
    private BitmapFont font32;

    public GameScreen() {
        uiCamera = new OrthographicCamera(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        mapCamera = new OrthographicCamera(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        width = Consts.DESIGN_WIDTH;
        height = Consts.DESIGN_HEIGHT;

        bg = new Texture("img/bg.png");
        map = new Texture("img/map.png");
        cities = new Texture[7];
        for (int i = 0; i < cities.length; i++) {
            cities[i] = new Texture("img/city_" + i + ".png");
        }

        font16 = new BitmapFont(Gdx.files.internal("fnt/dejavu-16.fnt"));
        font24 = new BitmapFont(Gdx.files.internal("fnt/dejavu-24.fnt"));
        font32 = new BitmapFont(Gdx.files.internal("fnt/dejavu-32.fnt"));

        spriteBatch = new SpriteBatch();
        shapeRenderer = new ShapeRenderer();
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new Inputter());
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

    public void renderCenter(SpriteBatch spriteBatch, Texture texture, float x, float y) {
        spriteBatch.draw(texture, x - texture.getWidth() / 2, y - texture.getHeight() / 2);
    }

    @Override
    public void render(float delta) {
        update(delta);

        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        spriteBatch.setColor(AJ3Colors.white.c);
        spriteBatch.begin();

        spriteBatch.setProjectionMatrix(mapCamera.combined);
        Vector3 topleft = new Vector3(), bottomright = new Vector3();
        Vector3 tmptl = new Vector3(), tmpbr = new Vector3();
        while (mapCamera.project(tmptl).x >= 0) {
            topleft.x -= map.getWidth();
            tmptl.set(topleft);
        }
        while (mapCamera.project(tmpbr).x <= mapCamera.viewportWidth) {
            bottomright.x += map.getWidth();
            tmpbr.set(bottomright);
        }
        while (mapCamera.project(tmptl).y >= 0) {
            topleft.y -= map.getHeight();
            tmptl.set(topleft);
        }
        while (mapCamera.project(tmpbr).y <= mapCamera.viewportHeight) {
            bottomright.y += map.getHeight();
            tmpbr.set(bottomright);
        }
        for(float x = topleft.x; x <= bottomright.x; x += map.getWidth()) {
            for(float y = topleft.y; y <= bottomright.y; y += map.getHeight()) {
                spriteBatch.draw(map, x, y);
            }
        }

        for (City city : World.w.cities) {
            Texture texture = cities[Math.min(city.level, cities.length - 1)];
            renderCenter(spriteBatch, texture, city.x * Consts.CITY_GRID, city.y * Consts.CITY_GRID);
        }

        spriteBatch.setProjectionMatrix(uiCamera.combined);

        Vector3 project = new Vector3();
        font24.setColor(AJ3Colors.white.c);
        for (City city : World.w.cities) {
            mapCamera.project(project.set(city.x * Consts.CITY_GRID, city.y * Consts.CITY_GRID, 0));
            renderTextAligned(spriteBatch, font24, city.code, project.x, project.y + 20, -0.5f, 0.5f);
        }

        spriteBatch.draw(bg, 0, 0, width, height);
        font24.setColor(AJ3Colors.black.c);
        font24.draw(spriteBatch, Consts.GAME_NAME, 10, height - 10);
        renderTextAligned(spriteBatch, font24, formatMoney(World.w.money), width - 10, height - 10, -1, 0);

        spriteBatch.end();
    }

    @Override
    public void resize(int newWidth, int newHeight) {
        width = Consts.DESIGN_HEIGHT * newWidth / ((float) newHeight);
        uiCamera.viewportWidth = width;
        uiCamera.position.set(width / 2, height / 2, 0);
        uiCamera.update();
        mapCamera.viewportWidth = width;
        mapCamera.update();
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

    class Inputter extends InputAdapter {

        int touchDownX = -1;
        int touchDownY = -1;

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (pointer == 0) {
                touchDownX = -1;
                touchDownY = -1;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (pointer == 0) {
                touchDownX = screenX;
                touchDownY = screenY;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (pointer == 0) {
                if (touchDownX >= 0 && touchDownY >= 0) {
                    mapCamera.translate((touchDownX - screenX) * mapCamera.zoom, (screenY - touchDownY) * mapCamera.zoom);
                    mapCamera.update();
                }
                touchDownX = screenX;
                touchDownY = screenY;
                return true;
            }
            return false;
        }

        @Override
        public boolean scrolled(int amount) {
            if (amount > 0) {
                mapCamera.zoom *= 1.5f;
            } else {
                mapCamera.zoom *= 0.75f;
            }
            mapCamera.zoom = MathUtils.clamp(mapCamera.zoom, 0.5f, 20f);
            mapCamera.update();
            return true;
        }
    }

}
