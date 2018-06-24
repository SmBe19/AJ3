package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.BitmapFont;

public enum Font {
    f16 ("dejavu-16"),
    f24 ("dejavu-24"),
    f32 ("dejavu-32"),
    ;

    public BitmapFont f;

    Font(String f) {
        this.f = new BitmapFont(Gdx.files.internal("fnt/" + f + ".fnt"));
    }
}
