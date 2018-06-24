package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
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
        super(x, y, 250, 300);
        this.airplane = airplane;
        this.stop = stop;

        uiElements.add(new ScrollList(10, 110, 230, 175, new ScrollList.ListProvider() {
            @Override
            public List<UIElement> provide() {
                return list;
            }
        }));

        for (final City city : World.w.cities) {
            list.add(new TextField(0, 0, 230, 20, city.fullName()) {
                @Override
                public boolean touchDown(float x, float y) {
                    stop.city = city;
                    World.w.buildGraph();
                    close();
                    return true;
                }
            });
        }

        uiElements.add(new Button(10, 10, 32, 32, Img.waitEmpty.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                stop.condition = new AirplaneStop.ConditionEmpty();
            }
        }));
        uiElements.add(new Button(50, 10, 32, 32, Img.waitFull.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                stop.condition = new AirplaneStop.ConditionFull();
            }
        }));
        uiElements.add(new Button(90, 10, 32, 32, Img.waitTimeMinus.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                if (stop.condition instanceof AirplaneStop.ConditionTime) {
                    int add = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
                    if (((AirplaneStop.ConditionTime) stop.condition).time >= add) {
                        ((AirplaneStop.ConditionTime) stop.condition).time -= add;
                    }
                } else {
                    stop.condition = new AirplaneStop.ConditionTime(10);
                }
            }
        }));
        uiElements.add(new Button(130, 10, 32, 32, Img.waitTimePlus.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                if (stop.condition instanceof AirplaneStop.ConditionTime) {
                    int add = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
                    ((AirplaneStop.ConditionTime) stop.condition).time += add;
                } else {
                    stop.condition = new AirplaneStop.ConditionTime(10);
                }
            }
        }));
        uiElements.add(new Button(170, 10, 32, 32, Img.waitPassengerMinus.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                if (stop.condition instanceof AirplaneStop.ConditionPassenger) {
                    int add = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
                    if (((AirplaneStop.ConditionPassenger) stop.condition).passenger >= add) {
                        ((AirplaneStop.ConditionPassenger) stop.condition).passenger -= add;
                    }
                } else {
                    stop.condition = new AirplaneStop.ConditionPassenger(1);
                }
            }
        }));
        uiElements.add(new Button(210, 10, 32, 32, Img.waitPassengerPlus.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                if (stop.condition instanceof AirplaneStop.ConditionPassenger) {
                    int add = Gdx.input.isKeyPressed(Input.Keys.SHIFT_LEFT) ? 10 : 1;
                    ((AirplaneStop.ConditionPassenger) stop.condition).passenger += add;
                } else {
                    stop.condition = new AirplaneStop.ConditionPassenger(1);
                }
            }
        }));
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
