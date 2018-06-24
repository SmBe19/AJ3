package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Button extends UIElement {

    protected Texture icon;
    protected OnClickHandler onClickHandler;

    public Button(float x, float y, float w, float h, Texture icon, OnClickHandler onClickHandler) {
        super(x, y, w, h);
        this.icon = icon;
        this.onClickHandler = onClickHandler;
        this.background = Img.buttonDepressed.t;
    }

    public Button(float x, float y, Texture icon, OnClickHandler onClickHandler) {
        this(x, y, 64, 64, icon, onClickHandler);
    }

    public void enable(boolean enabled) {
        if (enabled == this.enabled) {
            return;
        }
        this.enabled = enabled;
        if (enabled) {
            background = Img.buttonDepressed.t;
        } else {
            background = Img.buttonDisabled.t;
        }
    }

    @Override
    public boolean touchDown(float x, float y) {
        if (!enabled) {
            return true;
        }
        background = Img.buttonPressed.t;
        if (onClickHandler != null) {
            onClickHandler.onClick();
        }
        return true;
    }

    @Override
    public void touchUp(float x, float y) {
        if (!enabled) {
            return;
        }
        background = Img.buttonDepressed.t;
    }

    @Override
    protected void doRender(SpriteBatch spriteBatch, float offX, float offY) {
        if (icon != null) {
            spriteBatch.draw(icon, offX + x, offY + y, w, h);
        }
    }

    public interface OnClickHandler {
        void onClick();
    }
}
