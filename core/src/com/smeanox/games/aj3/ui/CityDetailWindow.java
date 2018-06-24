package com.smeanox.games.aj3.ui;

import com.smeanox.games.aj3.screen.GameScreen;
import com.smeanox.games.aj3.world.City;

public class CityDetailWindow extends Window {

    final City city;

    public CityDetailWindow(float x, float y, final City city) {
        super(x, y, 200, 200);
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
        uiElements.add(new Button(10, h - 180, 64, 64, Img.levelUp.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                city.levelUp();
            }
        }));
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof CityDetailWindow) {
            return this.city.equals(((CityDetailWindow) o).city);
        }
        return super.equals(o);
    }
}
