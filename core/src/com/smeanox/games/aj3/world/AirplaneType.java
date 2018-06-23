package com.smeanox.games.aj3.world;

import com.badlogic.gdx.graphics.Texture;

public enum AirplaneType {
    A ("A", "a", 100, 4, 1, 1, 10),
    B ("B", "a", 1000, 50, 5, 2, 100),
    C ("C", "a", 10000, 100, 8, 3, 1000),
    ;

    String name;
    Texture imgBuy, imgMap;
    int price, capacity;
    float pricePerDist, speed, range;

    AirplaneType(String name, String img, int price, int capacity, float pricePerDist, float speed, float range) {
        this.name = name;
        this.imgBuy = new Texture("img/airplane_buy_" + img + ".png");
        this.imgMap = new Texture("img/airplane_map_" + img + ".png");
        this.price = price;
        this.capacity = capacity;
        this.pricePerDist = pricePerDist;
        this.speed = speed;
        this.range = range;
    }
}
