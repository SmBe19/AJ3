package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public abstract class UIElement {

    public float x, y, w, h;
    public Texture background;
    public boolean visible = true, enabled = true;

    public UIElement(float x, float y, float w, float h) {
        this.x = x;
        this.y = y;
        this.w = w;
        this.h = h;
    }

    public void update() {
    }

    public boolean touchDown(float x, float y) {
        return false;
    }

    public void touchUp(float x, float y) {
    }

    public boolean scrolled(float amount, float xx, float yy) {
        return false;
    }

    public boolean inside(float xx, float yy) {
        return visible && x <= xx && xx <= x + w && y <= yy && yy <= y + h;
    }

    public void render(SpriteBatch spriteBatch, float offX, float offY) {
        if (!visible) {
            return;
        }
        if (background != null) {
            if (w < 64 && h < 64) {
                // TODO fix nine patch
                spriteBatch.draw(background, offX + x, offY + y, w, h);
            } else {
                Window.ninePatch(spriteBatch, background, 30, 4, 30, offX + x, offY + y, w, h);
            }
        }
        doRender(spriteBatch, offX, offY);
    }

    protected abstract void doRender(SpriteBatch spriteBatch, float offX, float offY);
}
