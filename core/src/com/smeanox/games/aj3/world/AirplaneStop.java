package com.smeanox.games.aj3.world;

public class AirplaneStop {
    public City city;
    public Condition condition;

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
    }

    public static class ConditionEmpty extends Condition {
        @Override
        public boolean check(Airplane airplane, City city) {
            return airplane.passengers.size() == 0;
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
    }
}
