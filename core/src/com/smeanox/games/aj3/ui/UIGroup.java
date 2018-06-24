package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class UIGroup extends UIElement {

    protected final List<UIElement> elements = new ArrayList<UIElement>();

    public UIGroup(float x, float y, List<UIElement> elements) {
        super(x, y, 0, 0);
        float maxX = 0, maxY = 0;
        for (UIElement element : elements) {
            maxX = Math.max(maxX, element.x + element.w);
            maxY = Math.max(maxY, element.y + element.h);
        }
        w = maxX;
        h = maxY;
        this.elements.addAll(elements);
    }

    public UIGroup(float x, float y, UIElement... elements) {
        this(x, y, Arrays.asList(elements));
    }

    @Override
    public void update() {
        for (UIElement element : elements) {
            element.update();
        }
    }

    @Override
    public boolean touchDown(float xx, float yy) {
        for (UIElement element : elements) {
            if (element.inside(xx - x, yy - y)) {
                if (element.touchDown(xx - x, yy - y)) {
                    return true;
                }
            }
        }
        return false;
    }

    @Override
    public void touchUp(float x, float y) {
        for (UIElement element : elements) {
            element.touchUp(x, y);
        }
    }

    @Override
    public boolean scrolled(float amount, float xx, float yy) {
        for (UIElement element : elements) {
            if (element.scrolled(amount, xx - x, yy - y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public boolean inside(float xx, float yy) {
        for (UIElement element : elements) {
            if (element.inside(xx - x, yy - y)) {
                return true;
            }
        }
        return false;
    }

    @Override
    protected void doRender(SpriteBatch spriteBatch, float offX, float offY) {
        for (UIElement element : elements) {
            element.render(spriteBatch, offX + x, offY + y);
        }
    }
}
