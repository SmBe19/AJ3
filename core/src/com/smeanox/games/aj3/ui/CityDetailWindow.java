package com.smeanox.games.aj3.ui;

import com.smeanox.games.aj3.screen.GameScreen;
import com.smeanox.games.aj3.world.City;
import com.smeanox.games.aj3.world.Passenger;
import com.smeanox.games.aj3.world.World;

import java.util.ArrayList;
import java.util.List;

public class CityDetailWindow extends Window {

    final City city;

    List<UIElement> destinations = new ArrayList<UIElement>();

    public CityDetailWindow(float x, float y, final City city) {
        super(x, y, 200, 300);
        this.city = city;
        uiElements.add(new TextField(10, h - 40, 0, 0, city.fullName()));
        uiElements.add(new DynamicTextField(10, h - 60, 0, 0, new DynamicTextField.TextFieldUpdater() {
            @Override
            public String update(DynamicTextField textField) {
                return "Airplanes: " + city.currentAirplanes.size() + " / " + city.capacityAirplanes;
            }
        }));
        uiElements.add(new DynamicTextField(10, h - 80, 0, 0, new DynamicTextField.TextFieldUpdater() {
            @Override
            public String update(DynamicTextField textField) {
                return "Passengers: " + city.passengers.size() + " / " + city.capacityPassengers;
            }
        }));
        uiElements.add(new DynamicTextField(10, h - 100, 0, 0, new DynamicTextField.TextFieldUpdater() {
            @Override
            public String update(DynamicTextField textField) {
                return "Level: " + city.level + " (" + GameScreen.formatMoney(city.levelUpCost()) + ")";
            }
        }));
        uiElements.add(new Button(10, h - 170, 64, 64, Img.levelUp.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                city.levelUp();
            }
        }));
        uiElements.add(new ScrollList(10, 10, 180, 100, new ScrollList.ListProvider() {
            @Override
            public List<UIElement> provide() {
                return destinations;
            }
        }));
    }

    @Override
    public void update() {
        super.update();
        while (destinations.size() < World.w.cities.size()) {
            final City aCity = World.w.cities.get(destinations.size());
            destinations.add(new UIGroup(0, 0,
                    new TextField(0, 0, 0, 20, aCity.fullName()),
                    new DynamicTextField(0, 0, 180, 20, -1, 0, new DynamicTextField.TextFieldUpdater() {
                        @Override
                        public String update(DynamicTextField textField) {
                            long res = 0;
                            for (Passenger passenger : city.passengers) {
                                if (passenger.destination == aCity) {
                                    res++;
                                }
                            }
                            return "" + res;
                        }
                    })
            ));
        }
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CityDetailWindow) {
            return this.city.equals(((CityDetailWindow) o).city);
        }
        return super.equals(o);
    }
}
