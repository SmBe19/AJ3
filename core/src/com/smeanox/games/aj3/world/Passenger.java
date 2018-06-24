package com.smeanox.games.aj3.world;

import com.badlogic.gdx.math.MathUtils;
import com.smeanox.games.aj3.screen.GameScreen;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;

public class Passenger {
    public long startTime;
    public City start, destination, nextDestination;
    public Location currentLocation;

    public void chooseDestination() {
        startTime = World.w.tickNo;
        do {
            destination = World.w.cities.get(MathUtils.random(World.w.cities.size() - 1));
            if (!MathUtils.randomBoolean(destination.popularity)) {
                destination = start;
            }
        } while (destination == start);
        chooseNextDestination();
    }

    public void arriveDestination() {
        long duration = World.w.tickNo - startTime;
        float dist = World.dist(start, destination);
        float speed = dist / duration;
        long newMoney = (long) Math.ceil(speed * 70);
        World.w.money += newMoney;
        start = destination;
        destination = null;
    }

    public void arrive(City city) {
        if (city == destination) {
            arriveDestination();
        }
        currentLocation = city;
        nextDestination = null;
        chooseNextDestination();
    }

    public void chooseNextDestination() {
        if (destination == null || currentLocation == destination) {
            chooseDestination();
        } else {
            chooseNextDestination(MathUtils.randomBoolean(0.6f) ? timeExtractor : distExtractor);
        }
    }

    public void chooseNextDestination(DistExtractor distExtractor) {
        Map<City, City> par = new HashMap<City, City>();
        Map<City, Float> dist = new HashMap<City, Float>();
        Set<City> done = new HashSet<City>();
        PriorityQueue<DijkstraEntry> pq = new PriorityQueue<DijkstraEntry>();

        City currentLocation = (City) this.currentLocation;
        par.put(currentLocation, currentLocation);
        dist.put(currentLocation, 0f);
        pq.add(new DijkstraEntry(currentLocation, 0f));
        while (!pq.isEmpty()) {
            DijkstraEntry ae = pq.poll();
            if (done.contains(ae.city)) {
                continue;
            }
            done.add(ae.city);
            List<World.GraphPair> neighbors = new ArrayList<World.GraphPair>(World.w.networkGraph.get(ae.city));
            Collections.shuffle(neighbors, MathUtils.random);
            for (World.GraphPair neighbor : neighbors) {
                if (done.contains(neighbor.city)) {
                    continue;
                }
                float newDist = ae.dist + distExtractor.dist(ae.city, neighbor);
                if (!dist.containsKey(neighbor.city) || newDist < dist.get(neighbor.city)) {
                    pq.add(new DijkstraEntry(neighbor.city, newDist));
                    dist.put(neighbor.city, newDist);
                    par.put(neighbor.city, ae.city);
                }
            }
        }

        if (!par.containsKey(destination)) {
            nextDestination = null;
        } else {
            nextDestination = destination;
            while (par.get(nextDestination) != currentLocation) {
                nextDestination = par.get(nextDestination);
            }
        }
    }

    class DijkstraEntry implements Comparable {
        public final City city;
        public final float dist;

        public DijkstraEntry(City city, float dist) {
            this.city = city;
            this.dist = dist;
        }

        @Override
        public int compareTo(Object o) {
            if (o instanceof DijkstraEntry) {
                return (int) Math.signum(this.dist - ((DijkstraEntry) o).dist);
            }
            throw new RuntimeException("Can not compare");
        }
    }

    interface DistExtractor {
        float dist(City current, World.GraphPair graphPair);
    }

    public static final DistExtractor distExtractor = new DistExtractor() {
        @Override
        public float dist(City current, World.GraphPair graphPair) {
            return graphPair.dist;
        }
    };

    public static final DistExtractor timeExtractor = new DistExtractor() {
        @Override
        public float dist(City current, World.GraphPair graphPair) {
            return graphPair.time;
        }
    };
}
