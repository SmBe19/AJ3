package com.smeanox.games.aj3.ui;

import com.smeanox.games.aj3.world.Airplane;
import com.smeanox.games.aj3.world.AirplaneStop;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.swing.Icon;

public class RouteEditWindow extends Window {

    private final Airplane airplane;
    private final Map<AirplaneStop, UIElement> stopToElement = new HashMap<AirplaneStop, UIElement>();
    private final List<UIElement> list = new ArrayList<UIElement>();

    public RouteEditWindow(float x, float y, final Airplane airplane) {
        super(x, y, 350, 300);
        this.airplane = airplane;

        uiElements.add(new TextField(10, h - 40, 0, 0, airplane.getFullName()));
        uiElements.add(new DynamicTextField(10, h - 60, 0, 0, new DynamicTextField.TextFieldUpdater() {
            @Override
            public String update(DynamicTextField textField) {
                return airplane.getLocationString();
            }
        }));
        uiElements.add(new DynamicTextField(10, h - 80, 0, 0, new DynamicTextField.TextFieldUpdater() {
            @Override
            public String update(DynamicTextField textField) {
                return "Passengers: " + airplane.passengers.size() + " / " + airplane.type.capacity;
            }
        }));
        uiElements.add(new Button(w - 40, h - 120, 32, 32, Img.add.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                AirplaneStop stop = new AirplaneStop();
                airplane.schedule.add(stop);
                list.add(createStopElement(stop));
            }
        }));
        uiElements.add(new ScrollList(10, 10, w - 20, h - 100, new ScrollList.ListProvider() {
            @Override
            public List<UIElement> provide() {
                return list;
            }
        }));

        for (AirplaneStop airplaneStop : airplane.schedule) {
            list.add(createStopElement(airplaneStop));
        }
    }

    protected UIElement createStopElement(final AirplaneStop stop) {
        UIGroup uiGroup = new UIGroup(0, 0,
                new Button(250, 2, 16, 16, Img.remove.t, new Button.OnClickHandler() {
                    @Override
                    public void onClick() {
                        UIElement uiElement = stopToElement.get(stop);
                        list.remove(uiElement);
                        airplane.schedule.remove(stop);
                        stopToElement.remove(stop);
                    }
                }),
                new DynamicTextField(0, 0, 240, 20, new DynamicTextField.TextFieldUpdater() {
                    @Override
                    public String update(DynamicTextField textField) {
                        if (stop.city == null) {
                            return "?";
                        }
                        return stop.city.fullName() + " (" + stop.condition.toString() + ")";
                    }
                }) {
                    @Override
                    public boolean touchDown(float x, float y) {
                        WindowManager.wm.addWindow(new StopEditWindow(RouteEditWindow.this.x + 25, RouteEditWindow.this.y + RouteEditWindow.this.h - 250, airplane, stop));
                        return true;
                    }
                });
        stopToElement.put(stop, uiGroup);
        return uiGroup;
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof RouteEditWindow) {
            return this.airplane.equals(((RouteEditWindow) o).airplane);
        }
        return super.equals(o);
    }
}
