package com.smeanox.games.aj3.world;

import java.util.ArrayList;
import java.util.List;

public class Airplane implements Location, Ticking {

    public long startTime, waitTime, landedTime;
    public City start, destination;
    public final AirplaneType type;
    public final List<AirplaneStop> schedule;
    public int currentScheduleEntry;
    public AirplaneStop currentStop;
    public final List<Passenger> passengers;

    public Airplane(AirplaneType type) {
        this.type = type;
        this.schedule = new ArrayList<AirplaneStop>();
        this.passengers = new ArrayList<Passenger>();
        this.currentScheduleEntry = 0;
        this.waitTime = -1;
    }

    public void place(City city) {
        startTime = -1;
        waitTime = -1;
        landedTime = World.w.tickNo;
        start = city;
        destination = null;
        start.currentAirplanes.add(this);
    }

    public void start() {
        if (destination == null) {
            return;
        }
        float dist = World.dist(start, destination);
        long cost = (long) Math.ceil(dist * type.pricePerDist);
        if (World.w.money < cost) {
            return;
        }
        World.w.money -= cost;
        startTime = World.w.tickNo;
        waitTime = -1;
        landedTime = -1;
        start.currentAirplanes.remove(this);
    }

    public void prepareStart(City destination) {
        startTime = -1;
        waitTime = -1;
        this.destination = destination;
    }

    public void land() {
        startTime = -1;
        waitTime = -1;
        landedTime = World.w.tickNo;
        start = destination;
        destination = null;
        start.currentAirplanes.add(this);

        List<Passenger> origPassengers = new ArrayList<Passenger>(passengers);
        passengers.clear();
        for (Passenger origPassenger : origPassengers) {
            if (!start.arrive(origPassenger)) {
                passengers.add(origPassenger);
            }
        }

        nextScheduleEntry();
    }

    public void nextScheduleEntry() {
        currentStop = currentScheduleEntry < schedule.size() ? schedule.get(currentScheduleEntry) : null;
        currentScheduleEntry++;
        if (currentScheduleEntry >= schedule.size()) {
            currentScheduleEntry = 0;
        }
        if (currentScheduleEntry < schedule.size()) {
            prepareStart(schedule.get(currentScheduleEntry).city);
        }
    }

    public boolean board(Passenger passenger) {
        if (passengers.size() < type.capacity) {
            passengers.add(passenger);
            passenger.currentLocation = this;
            return true;
        }
        return false;
    }

    @Override
    public void tick() {
        if (waitTime >= 0) {
            // arrived, but can not land
            if (destination.currentAirplanes.size() < destination.capacityAirplanes) {
                land();
            }
        } else if (startTime >= 0) {
            // in air
            float dist = World.dist(start, destination);
            float disttime = dist / type.speed;
            long airtime = World.w.tickNo - startTime;
            if (airtime > disttime) {
                if (destination.currentAirplanes.size() < destination.capacityAirplanes) {
                    land();
                } else {
                    waitTime = World.w.tickNo;
                }
            }
        } else if (landedTime >= 0) {
            // at airport
            if (currentStop == null || currentStop.check(this)) {
                start();
            }
        }
    }
}
