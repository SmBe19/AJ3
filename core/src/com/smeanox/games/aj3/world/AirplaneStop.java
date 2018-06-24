package com.smeanox.games.aj3.world;

public class AirplaneStop {
    public City city;
    public Condition condition;

    public AirplaneStop() {
        this.condition = new ConditionFull();
    }

    public AirplaneStop(City city) {
        this();
        this.city = city;
    }

    public boolean check(Airplane airplane) {
        return condition.check(airplane, city);
    }

    public static abstract class Condition {
        public abstract boolean check(Airplane airplane, City city);
    }

    public static class ConditionFull extends Condition {
        @Override
        public boolean check(Airplane airplane, City city) {
            return airplane.passengers.size() == airplane.type.capacity;
        }

        @Override
        public String toString() {
            return "full";
        }
    }

    public static class ConditionEmpty extends Condition {
        @Override
        public boolean check(Airplane airplane, City city) {
            return airplane.passengers.size() == 0;
        }

        @Override
        public String toString() {
            return "empty";
        }
    }

    public static class ConditionTime extends Condition {
        final long time;

        public ConditionTime(long time) {
            this.time = time;
        }

        @Override
        public boolean check(Airplane airplane, City city) {
            return airplane.landedTime >= 0 && World.w.tickNo - airplane.landedTime >= time;
        }

        @Override
        public String toString() {
            return time + "s passed";
        }
    }

    public static class ConditionPassenger extends Condition {
        final int passenger;

        public ConditionPassenger(int passenger) {
            this.passenger = passenger;
        }

        @Override
        public boolean check(Airplane airplane, City city) {
            return airplane.passengers.size() == passenger;
        }

        @Override
        public String toString() {
            return "> " + passenger + " passengers";
        }
    }
}
