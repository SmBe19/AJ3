package com.smeanox.games.aj3.world;

import com.badlogic.gdx.math.MathUtils;

public class Passenger {
    public long startTime;
    public City start, destination;
    public Location currentLocation;

    public void chooseDestination() {
        startTime = World.w.tickNo;
        do {
            destination = World.w.cities.get(MathUtils.random(World.w.cities.size()));
            if (!MathUtils.randomBoolean(destination.popularity)) {
                destination = start;
            }
        } while (destination == start);
    }

    public void arrive() {
        long duration = World.w.tickNo - startTime;
        float dist = World.dist(start, destination);
        float speed = dist / duration;
        long newMoney = (long) Math.ceil(speed * 0.1f);
        World.w.money += newMoney;
        start = destination;
        destination = null;
    }
}
