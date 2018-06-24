package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.smeanox.games.aj3.AJ3Colors;
import com.smeanox.games.aj3.screen.GameScreen;

public class TextField extends UIElement {

    protected String text;
    protected float alignX, alignY;

    public TextField(float x, float y, float w, float h, String text) {
        super(x, y, w, h);
        this.text = text;
    }

    public TextField(float x, float y, float w, float h, String text, float alignX, float alignY) {
        this(x, y, w, h, text);
        this.alignX = alignX;
        this.alignY = alignY;
    }

    @Override
    protected void doRender(SpriteBatch spriteBatch, float offX, float offY) {
        Font.f16.f.setColor(AJ3Colors.black.c);
        GameScreen.renderTextAligned(spriteBatch, Font.f16.f, text, offX + x - alignX * w, offY + y - alignY * h, alignX, alignY);
    }
}
