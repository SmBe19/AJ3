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
    waitEmpty("wait_empty"),
    waitFull("wait_full"),
    waitTimePlus("wait_time_plus"),
    waitTimeMinus("wait_time_minus"),
    waitPassengerPlus("wait_passenger_plus"),
    waitPassengerMinus("wait_passenger_minus"),
    ;

    public Texture t;

    Img(String t) {
        this.t = new Texture("img/" + t + ".png");
    }
}
