package com.smeanox.games.aj3.world;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.aj3.Consts;

import java.util.ArrayList;
import java.util.List;

public class World {
    public static final World w = new World();

    public long tickNo;
    public float tickTime;

    public long money;

    public List<City> cities;
    public List<Airplane> airplanes;

    private World() {
        newGame();
    }

    public void newGame() {
        tickNo = 0;
        money = 1000;

        cities = new ArrayList<City>();
        airplanes = new ArrayList<Airplane>();

        String name = generateName();
        String code = generateCode(name);
        cities.add(new City(name, code, 0, 0, 1));
        addCity(10);
    }

    public void addCity(float range) {
        String name = generateName();
        String code = generateCode(name);
        float minx = 0, maxx = 0, miny = 0, maxy = 0;
        for (City city : cities) {
            minx = Math.min(minx, city.x);
            maxx = Math.max(maxx, city.x);
            miny = Math.min(miny, city.y);
            maxy = Math.max(maxy, city.y);
        }
        minx -= range;
        maxx += range;
        miny -= range;
        maxy += range;
        float x = 0, y = 0;
        while (true) {
            x = MathUtils.random(minx, maxx);
            y = MathUtils.random(miny, maxy);
            float dist2 = range + 1;
            for (City city : cities) {
                dist2 = Math.min(dist2, dist2(x, y, city.x, city.y));
            }
            if (dist2 <= range * range) {
                break;
            }
        }
        cities.add(new City(name, code, x, y, MathUtils.random(0.1f, 1)));
    }

    public static float dist(City city1, City city2) {
        return dist(city1.x, city1.y, city2.x, city2.y);
    }

    public static float dist(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(dist2(x1, y1, x2, y2));
    }

    public static float dist2(City city1, City city2) {
        return dist2(city1.x, city1.y, city2.x, city2.y);
    }

    public static float dist2(float x1, float y1, float x2, float y2) {
        return (x2 - x1) * (x2 - x1) + (y2 - y1) * (y2 - y1);
    }

    public static String generateName() {
        StringBuilder name = new StringBuilder();
        String vocals = "aeiou";
        String consonants = "bcdfghjklmnpqrstvwxyz";
        int length = MathUtils.random(4, 10);
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                name.append(vocals.charAt(MathUtils.random(vocals.length() - 1)));
            } else {
                name.append(consonants.charAt(MathUtils.random(consonants.length() - 1)));
            }
        }
        return name.toString();
    }

    public static String generateCode(String name) {
        StringBuilder code = new StringBuilder();
        code.append(name.charAt(0));
        int a, b;
        a = MathUtils.random(1, name.length()-1);
        b = MathUtils.random(1, name.length()-1);
        if (a < b) {
            a = a ^ b;
            b = a ^ b;
            a = a ^ b;
        }
        code.append(name.charAt(a));
        code.append(name.charAt(b));
        return code.toString().toUpperCase();
    }

    public void buildGraph() {
        // TODO build graph from routes
    }

    public void tick() {
        tickNo++;

        for (Airplane airplane : airplanes) {
            airplane.tick();
        }
        for (City city : cities) {
            city.tick();
        }
    }

    public void doTicks(float delta) {
        tickTime += delta;
        while (tickTime >= Consts.TICK_TIME) {
            tickTime -= Consts.TICK_TIME;
            tick();
        }
    }
}
