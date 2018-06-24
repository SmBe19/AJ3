package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class ImgField extends UIElement {

    protected Texture img;

    public ImgField(float x, float y, float w, float h, Texture img) {
        super(x, y, w, h);
        this.img = img;
    }

    public ImgField(float x, float y, Texture img) {
        this(x, y, img.getWidth(), img.getHeight(), img);
    }

    @Override
    protected void doRender(SpriteBatch spriteBatch, float offX, float offY) {
        spriteBatch.draw(img, offX + x, offY + y, w, h);
    }
}
