package com.smeanox.games.aj3.world;

import com.badlogic.gdx.graphics.Texture;

public enum AirplaneType {
    A ("Cessna", "a", 100, 4, 1, 1, 10),
    B ("Jumbo", "b", 1000, 50, 5, 2, 100),
    C ("A380", "c", 10000, 200, 8, 3, 1000),
    D ("Falcon9", "d", 100000, 20, 20, 5, 10000),
    ;

    public String name;
    public Texture imgBuy, imgMap;
    public int price, capacity;
    public float pricePerDist, speed, range;

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
