package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class Window {

    public float x, y, w, h;
    protected List<UIElement> uiElements;
    protected UIElement touchDownElement;

    public Window(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
        uiElements = new ArrayList<UIElement>();
    }

    public void add(UIElement... uiElements) {
        this.uiElements.addAll(Arrays.asList(uiElements));
    }

    public void update() {
        for (UIElement uiElement : uiElements) {
            uiElement.update();
        }
    }

    public void onTouchDown(float xx, float yy) {
        touchDownElement = null;
        for (UIElement uiElement : uiElements) {
            if (uiElement.inside(xx, yy)) {
                touchDownElement = uiElement;
                if (uiElement.touchDown(xx - uiElement.x, yy - uiElement.y)) {
                    break;
                }
            }
        }
    }

    public void onTouchUp(float xx, float yy) {
        if (touchDownElement != null) {
            touchDownElement.touchUp(xx - touchDownElement.x, yy - touchDownElement.y);
        }
        touchDownElement = null;
    }

    public void render(SpriteBatch spriteBatch) {
        ninePatch(spriteBatch, Img.window.t, 30, 4, 30, x, y, w, h);
        spriteBatch.draw(Img.close.t, x + w - 11, y + h - 11, 11, 11);

        for (int i = uiElements.size() - 1; i >= 0; i--) {
            uiElements.get(i).render(spriteBatch, x, y);
        }
    }

    public boolean inside(float xx, float yy) {
        return x <= xx && xx <= x + w && y <= yy && yy <= y + h;
    }

    public static void ninePatch(SpriteBatch spriteBatch, Texture texture, int n1, int n2, int n3, float x, float y, float w, float h) {
        spriteBatch.draw(texture, x, y, n1, n1, 0, n1 + n2, n1, n3, false, false);
        spriteBatch.draw(texture, x + n1, y, w - n1 - n3, n1, n1, n1 + n2, n2, n3, false, false);
        spriteBatch.draw(texture, x + w - n3, y, n3, n1, n1 + n2, n1 + n2, n3, n3, false, false);

        spriteBatch.draw(texture, x, y + n1, n1, h - n1 - n3, 0, n1, n1, n2, false, false);
        spriteBatch.draw(texture, x + n1, y + n1, w - n1 - n3, h - n1 - n3, n1, n1, n2, n2, false, false);
        spriteBatch.draw(texture, x + w - n3, y + n1, n3, h - n1 - n3, n1 + n2, n1, n3, n2, false, false);

        spriteBatch.draw(texture, x, y + h - n1, n1, n3, 0, 0, n1, n1, false, false);
        spriteBatch.draw(texture, x + n1, y + h - n1, w - n1 - n3, n3, n1, 0, n2, n1, false, false);
        spriteBatch.draw(texture, x + w - n3, y + h - n1, n3, n3, n1 + n2, 0, n3, n1, false, false);
    }

}
