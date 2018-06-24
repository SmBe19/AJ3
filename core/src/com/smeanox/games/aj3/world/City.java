package com.smeanox.games.aj3.world;

import java.util.ArrayList;
import java.util.List;

public class City implements Location, Ticking {
    public final String name, code;
    public final float x, y, popularity;
    public int level;
    public int capacityAirplanes, capacityPassengers;
    public final List<Passenger> passengers;
    public final List<Airplane> currentAirplanes;

    public City(String name, String code, float x, float y, float popularity) {
        this.name = name;
        this.code = code;
        this.x = x;
        this.y = y;
        this.popularity = popularity;
        this.passengers = new ArrayList<Passenger>();
        this.currentAirplanes = new ArrayList<Airplane>();
        this.capacityAirplanes = 10;
        this.capacityPassengers = 100;
    }

    public boolean arrive(Passenger passenger) {
        if (passengers.size() < capacityPassengers) {
            passengers.add(passenger);
            passenger.arrive(this);
            return true;
        }
        return false;
    }

    public long levelUpCost() {
        return (long) Math.ceil(Math.pow(2, level/2)) * 100;
    }

    public void levelUp() {
        if (World.w.money < levelUpCost()) {
            return;
        }
        World.w.money -= levelUpCost();
        level++;
        capacityAirplanes += 10;
        capacityPassengers *= 4;
    }

    public String fullName(){
        return name + " [" + code + "]";
    }

    @Override
    public void tick() {
        for (Passenger passenger : new ArrayList<Passenger>(passengers)) {
            if (passenger.nextDestination == null) {
                if (World.w.tickNo % 100 == 0) {
                    passenger.chooseNextDestination();
                }
                break;
            }
            for (Airplane currentAirplane : currentAirplanes) {
                if (currentAirplane.destination == passenger.nextDestination) {
                    if (currentAirplane.board(passenger)) {
                        passengers.remove(passenger);
                        break;
                    }
                }
            }
        }
    }
}
