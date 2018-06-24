package com.smeanox.games.aj3.screen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.InputMultiplexer;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.GlyphLayout;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.smeanox.games.aj3.AJ3Colors;
import com.smeanox.games.aj3.Consts;
import com.smeanox.games.aj3.ui.Button;
import com.smeanox.games.aj3.ui.BuyAirplaneWindow;
import com.smeanox.games.aj3.ui.Font;
import com.smeanox.games.aj3.ui.Img;
import com.smeanox.games.aj3.ui.UIElement;
import com.smeanox.games.aj3.ui.Window;
import com.smeanox.games.aj3.ui.WindowManager;
import com.smeanox.games.aj3.world.Airplane;
import com.smeanox.games.aj3.world.AirplaneType;
import com.smeanox.games.aj3.world.City;
import com.smeanox.games.aj3.world.World;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class GameScreen implements Screen {

    private float width, height;
    private OrthographicCamera uiCamera, mapCamera;
    private SpriteBatch spriteBatch;
    private WindowManager windowManager;
    private UIElement clickedUIElement;
    private List<UIElement> uiElements;

    private Texture bg, map;
    private Texture[] cities;
    private Vector3 project = new Vector3();

    private AirplaneType buyingAirplane;
    private static GlyphLayout glyphLayout = new GlyphLayout();

    public GameScreen() {
        uiCamera = new OrthographicCamera(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        mapCamera = new OrthographicCamera(Consts.DESIGN_WIDTH, Consts.DESIGN_HEIGHT);
        width = Consts.DESIGN_WIDTH;
        height = Consts.DESIGN_HEIGHT;
        windowManager = new WindowManager(uiCamera);

        bg = new Texture("img/bg.png");
        map = new Texture("img/map.png");
        cities = new Texture[7];
        for (int i = 0; i < cities.length; i++) {
            cities[i] = new Texture("img/city_" + i + ".png");
        }

        spriteBatch = new SpriteBatch();

        uiElements = new ArrayList<UIElement>();
        uiElements.add(new Button(0, 4, Img.buyAirplane.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                if (buyingAirplane != null) {
                    buyingAirplane = null;
                } else {
                    for (Window window : windowManager.windows) {
                        if (window instanceof BuyAirplaneWindow) {
                            return;
                        }
                    }
                    windowManager.addWindow(new BuyAirplaneWindow(width - 300, 20, new BuyAirplaneWindow.ChooseHandler() {
                        @Override
                        public void chose(AirplaneType type) {
                            buyingAirplane = type;
                        }
                    }));
                }
            }
        }));
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(new InputMultiplexer(windowManager, new Inputter()));
    }

    public void update(float delta) {
        World.w.doTicks(delta);
        windowManager.update();
    }

    public static String formatMoney(long money) {
        StringBuilder sb = new StringBuilder();
        if (money < 0) {
            sb.append("-");
            money = -money;
        }
        List<Long> parts = new ArrayList<Long>();
        while (money > 0) {
            parts.add(money % 1000);
            money /= 1000;
        }
        if (parts.isEmpty()) {
            parts.add(0L);
        }
        sb.append(parts.get(parts.size() - 1));
        for (int i = parts.size() - 2; i >= 0; i--) {
            sb.append("'");
            sb.append(String.format(Locale.US, "%03d", parts.get(i)));
        }
        sb.append(" $");
        return sb.toString();
    }

    public static void renderTextAligned(SpriteBatch spriteBatch, BitmapFont font, String text, float x, float y, float xAlign, float yAlign) {
        glyphLayout.setText(font, text);
        //font.draw(spriteBatch, text, x + xAlign * glyphLayout.width, y + yAlign * glyphLayout.height);
        font.draw(spriteBatch, glyphLayout, x + xAlign * glyphLayout.width, y + yAlign * glyphLayout.height);
    }

    public static void renderCenter(SpriteBatch spriteBatch, Texture texture, float x, float y) {
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
        for (float x = topleft.x; x <= bottomright.x; x += map.getWidth()) {
            for (float y = topleft.y; y <= bottomright.y; y += map.getHeight()) {
                spriteBatch.draw(map, x, y);
            }
        }

        for (City city : World.w.cities) {
            Texture texture = cities[Math.min(city.level, cities.length - 1)];
            renderCenter(spriteBatch, texture, city.x, city.y);
        }

        if (buyingAirplane != null) {
            mapCamera.unproject(project.set(Gdx.input.getX(), Gdx.input.getY(), 0));
            renderCenter(spriteBatch, buyingAirplane.imgMap, project.x, project.y);
        }

        spriteBatch.setProjectionMatrix(uiCamera.combined);

        Font.f24.f.setColor(AJ3Colors.white.c);
        for (City city : World.w.cities) {
            mapCamera.project(project.set(city.x, city.y, 0));
            renderTextAligned(spriteBatch, Font.f24.f, city.code, project.x, project.y + 20, -0.5f, 0.5f);
        }

        Font.f16.f.setColor(AJ3Colors.red.c);
        for (World.ErrorText errorText : World.w.errorTexts) {
            mapCamera.project(project.set(errorText.x, errorText.y, 0));
            renderTextAligned(spriteBatch, Font.f16.f, errorText.text, project.x, project.y, -0.5f, -0.5f);
        }

        windowManager.render(spriteBatch);

        spriteBatch.draw(bg, 0, 0, width, height);

        uiElements.get(0).x = width - 68; // buy airplane
        for (UIElement uiElement : uiElements) {
            uiElement.render(spriteBatch, 0, 0);
        }

        Font.f24.f.setColor(AJ3Colors.black.c);
        Font.f24.f.draw(spriteBatch, Consts.GAME_NAME, 10, height - 10);
        renderTextAligned(spriteBatch, Font.f24.f, formatMoney(World.w.money), width - 10, height - 10, -1, 0);

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
        boolean dragged = false;

        @Override
        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            if (pointer == 0) {
                if (clickedUIElement != null) {
                    uiCamera.unproject(project.set(screenX, screenY, 0));
                    clickedUIElement.touchUp(project.x, project.y);
                }
                clickedUIElement = null;
                dragged = false;
                touchDownX = -1;
                touchDownY = -1;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDown(int screenX, int screenY, int pointer, int button) {
            if (pointer == 0) {
                uiCamera.unproject(project.set(screenX, screenY, 0));
                clickedUIElement = null;
                for (UIElement uiElement : uiElements) {
                    if (uiElement.inside(project.x, project.y)) {
                        clickedUIElement = uiElement;
                        break;
                    }
                }
                if (clickedUIElement != null) {
                    clickedUIElement.touchDown(project.x, project.y);
                }
                if (buyingAirplane != null) {
                    City clickedCity = findClickedCity(screenX, screenY);
                    if (clickedCity != null) {
                        if (clickedCity.currentAirplanes.size() < clickedCity.capacityAirplanes) {
                            if (World.w.money >= buyingAirplane.price) {
                                clickedCity.currentAirplanes.add(new Airplane(buyingAirplane));
                                World.w.money -= buyingAirplane.price;
                            } else {
                                World.w.errorTexts.add(World.w.new ErrorText("Not enough money", project.x, project.y));
                            }
                            buyingAirplane = null;
                        } else {
                            World.w.errorTexts.add(World.w.new ErrorText("Airport is full", project.x, project.y));
                        }
                    }
                }
                touchDownX = screenX;
                touchDownY = screenY;
                dragged = false;
                return true;
            }
            return false;
        }

        @Override
        public boolean touchDragged(int screenX, int screenY, int pointer) {
            if (pointer == 0 && clickedUIElement == null) {
                dragged = true;
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

        public City findClickedCity(int screenX, int screenY) {
            mapCamera.unproject(project.set(screenX, screenY, 0));
            for (City city : World.w.cities) {
                if (World.dist2(city.x, city.y, project.x, project.y) < 81) {
                    return city;
                }
            }
            return null;
        }
    }

}
