package com.smeanox.games.aj3.ui;

import com.smeanox.games.aj3.world.Airplane;
import com.smeanox.games.aj3.world.AirplaneStop;
import com.smeanox.games.aj3.world.City;
import com.smeanox.games.aj3.world.World;

import java.util.ArrayList;
import java.util.List;

public class StopEditWindow extends Window {

    private final Airplane airplane;
    private final AirplaneStop stop;

    private final List<UIElement> list = new ArrayList<UIElement>();

    public StopEditWindow(float x, float y, Airplane airplane, final AirplaneStop stop) {
        super(x, y, 200, 200);
        this.airplane = airplane;
        this.stop = stop;

        uiElements.add(new ScrollList(10, 10, 180, 175, new ScrollList.ListProvider() {
            @Override
            public List<UIElement> provide() {
                return list;
            }
        }));

        for (final City city : World.w.cities) {
            list.add(new TextField(0, 0, 180, 20, city.fullName()) {
                @Override
                public boolean touchDown(float x, float y) {
                    stop.city = city;
                    World.w.buildGraph();
                    close();
                    return true;
                }
            });
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof StopEditWindow) {
            StopEditWindow stopEditWindow = (StopEditWindow) o;
            return this.airplane.equals(stopEditWindow.airplane) && this.stop.equals(stopEditWindow.stop);
        }
        return super.equals(o);
    }
}
