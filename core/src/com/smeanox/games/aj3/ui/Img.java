package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.Texture;

public enum Img {
    window ("window"),
    buttonDepressed ("button_depressed"),
    buttonPressed ("button_pressed"),
    buttonDisabled ("button_disabled"),
    add("add"),
    buyAirplane("buy_airplane"),
    listAirplane("list_airplane"),
    close("close"),
    levelUp("level_up"),
    remove("remove"),
    ;

    public Texture t;

    Img(String t) {
        this.t = new Texture("img/" + t + ".png");
    }
}
