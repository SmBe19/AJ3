package com.smeanox.games.aj3.world;

import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.aj3.Consts;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class World {
    public static final World w = new World();

    public long tickNo;
    public float tickTime;

    public long money;

    public List<City> cities;
    public List<Airplane> airplanes;
    public List<ErrorText> errorTexts;
    public List<ErrorText> successTexts;
    public float passengerRate, cityRate, cityRange;

    public Map<City, List<GraphPair>> networkGraph;

    private World() {
    }

    public void newGame() {
        tickNo = 0;
        money = 1000;
        passengerRate = 0.02f;
        cityRate = 0.001f;
        cityRange = 100;

        cities = new ArrayList<City>();
        airplanes = new ArrayList<Airplane>();
        errorTexts = new ArrayList<ErrorText>();
        successTexts = new ArrayList<ErrorText>();
        networkGraph = new HashMap<City, List<GraphPair>>();

        String name = generateName();
        String code = generateCode(name);
        cities.add(new City(name, code, 0, 0, 1));
        addCity(cityRange);

        buildGraph();

        for (int i = 0; i < 20; i++) {
            addPassenger();
        }
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
            if (dist2 <= range * range && dist2 > 81) {
                break;
            }
        }
        City newCity = new City(name, code, x, y, MathUtils.random(0.1f, 1));
        cities.add(newCity);
        buildGraph();

        successTexts.add(new ErrorText("New City: " + newCity.fullName(), 0, 0, tickNo + 100));
    }

    public void addPassenger() {
        Passenger passenger = new Passenger();
        City city = cities.get(MathUtils.random(cities.size() - 1));
        city.passengers.add(passenger);
        passenger.start = city;
        passenger.currentLocation = city;
        passenger.chooseDestination();
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
        //int length = MathUtils.random(4, 10);
        int length = 8;
        for (int i = 0; i < length; i++) {
            if (i % 2 == 0) {
                name.append(vocals.charAt(MathUtils.random(vocals.length() - 1)));
            } else {
                name.append(consonants.charAt(MathUtils.random(consonants.length() - 1)));
            }
        }
        name.replace(0, 1, name.substring(0, 1).toUpperCase());
        return name.toString();
    }

    public static String generateCode(String name) {
        StringBuilder code = new StringBuilder();
        code.append(name.charAt(0));
        int a, b;
        a = MathUtils.random(1, name.length()-1);
        b = MathUtils.random(1, name.length()-1);
        if (a > b) {
            a = a ^ b;
            b = a ^ b;
            a = a ^ b;
        }
        code.append(name.charAt(a));
        code.append(name.charAt(b));
        return code.toString().toUpperCase();
    }

    public Airplane buyAirplane(AirplaneType type, City city) {
        if (World.w.money < type.price) {
            return null;
        }
        Airplane newAirplane = new Airplane(type);
        airplanes.add(newAirplane);
        newAirplane.place(city);
        World.w.money -= type.price;
        return newAirplane;
    }

    public void buildGraph() {
        networkGraph.clear();
        for (City city : cities) {
            networkGraph.put(city, new ArrayList<GraphPair>());
        }
        for (Airplane airplane : airplanes) {
            if (airplane.schedule.size() < 2) {
                continue;
            }
            City prev = airplane.schedule.get(airplane.schedule.size() - 1).city;
            for (AirplaneStop airplaneStop : airplane.schedule) {
                if (airplaneStop.city == null || prev == null) {
                    continue;
                }
                float dist = dist(prev, airplaneStop.city);
                float time = dist / airplane.type.speed;
                networkGraph.get(prev).add(new GraphPair(airplaneStop.city, dist, time));
                prev = airplaneStop.city;
            }
        }
    }

    public void tick() {
        tickNo++;

        if (MathUtils.randomBoolean(passengerRate)) {
            System.out.println("New Passenger");
            addPassenger();
        }
        passengerRate *= 1.001f;

        if (MathUtils.randomBoolean(cityRate)) {
            cityRange *= 1.2f;
            System.out.println("New City");
            addCity(cityRange);
        }
        cityRate *= 1.00001f;

        for (Airplane airplane : airplanes) {
            airplane.tick();
        }
        for (City city : cities) {
            city.tick();
        }

        for (int i = errorTexts.size() - 1; i >= 0; i--) {
            if (tickNo - errorTexts.get(i).shown > 10) {
                errorTexts.remove(i);
            }
        }

        for (int i = successTexts.size() - 1; i >= 0; i--) {
            if (tickNo - successTexts.get(i).shown > 40) {
                successTexts.remove(i);
            }
        }
    }

    public void doTicks(float delta) {
        tickTime += delta;
        while (tickTime >= Consts.TICK_TIME) {
            tickTime -= Consts.TICK_TIME;
            tick();
        }
    }

    public class GraphPair {
        public final City city;
        public final float dist;
        public final float time;

        public GraphPair(City city, float dist, float time) {
            this.city = city;
            this.dist = dist;
            this.time = time;
        }
    }

    public class ErrorText {
        public final String text;
        public final float x, y;
        public final long shown;

        public ErrorText(String text, float x, float y) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.shown = tickNo;
        }

        public ErrorText(String text, float x, float y, long shown) {
            this.text = text;
            this.x = x;
            this.y = y;
            this.shown = shown;
        }
    }
}
