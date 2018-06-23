package com.smeanox.games.aj3;

import com.badlogic.gdx.graphics.Color;

public enum AJ3Colors {
    black(0, 0, 0, 1),
    green(131, 182, 146, 1),
    yellow(255, 221, 143, 1),
    orange(234, 144, 16, 1),
    red(254, 94, 65, 1),
    blue(51, 101, 138, 1),
    white(255, 255, 255, 1),
    ;

    public Color c;

    AJ3Colors(float r, float g, float b, float a) {
        this.c = new Color(r/255f, g/255f, b/255f, a);
    }
}
