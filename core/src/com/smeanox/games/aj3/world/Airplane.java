package com.smeanox.games.aj3.world;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class Airplane implements Location, Ticking {

    public static int lastUniqueNumber = 0;

    public int uniqueNumber;
    public long startTime, waitTime, landedTime;
    public City start, destination;
    public final AirplaneType type;
    public final List<AirplaneStop> schedule;
    public int currentScheduleEntry;
    public AirplaneStop currentStop;
    public final List<Passenger> passengers;
    private Vector2 location = new Vector2(), location2 = new Vector2();

    public Airplane(AirplaneType type) {
        this.type = type;
        this.schedule = new ArrayList<AirplaneStop>();
        this.passengers = new ArrayList<Passenger>();
        this.currentScheduleEntry = 0;
        this.startTime = -1;
        this.waitTime = -1;
        this.landedTime = -1;
        this.uniqueNumber = ++lastUniqueNumber;
    }

    public void place(City city) {
        startTime = -1;
        waitTime = -1;
        landedTime = World.w.tickNo;
        start = city;
        destination = null;
        start.currentAirplanes.add(this);
        schedule.add(new AirplaneStop(city));
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

    public String getFullName() {
        return type.name + " #" + uniqueNumber;
    }

    public String getLocationString() {
        if (landedTime >= 0) {
            return start.name + " [" + start.code + "]";
        } else if (startTime >= 0 || waitTime >= 0) {
            return start.fullName() + " -> " + destination.fullName();
        } else {
            return "unknown";
        }
    }

    public float getProgress() {
        float dist = World.dist(start, destination);
        float disttime = dist / type.speed;
        long airtime = World.w.tickNo - startTime;
        return airtime / disttime;
    }

    public Vector2 getLocation() {
        if (waitTime < 0 && landedTime < 0) {
            float progress = getProgress();
            location.set(start.x, start.y);
            location2.set(destination.x, destination.y);
            location.lerp(location2, MathUtils.clamp(progress, 0, 1));
            return location;
        } else {
            // TODO implement if necessary
            return location;
        }
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
            if (destination == null || (currentStop != null && start != currentStop.city)) {
                nextScheduleEntry();
            }
            // at airport
            if (currentStop == null || currentStop.check(this)) {
                start();
            }
        }
    }
}
