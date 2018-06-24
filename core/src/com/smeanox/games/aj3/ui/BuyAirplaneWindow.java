package com.smeanox.games.aj3.ui;

import com.smeanox.games.aj3.screen.GameScreen;
import com.smeanox.games.aj3.world.AirplaneType;
import com.smeanox.games.aj3.world.World;

import java.util.HashMap;
import java.util.Map;

public class BuyAirplaneWindow extends Window {

    private final ChooseHandler handler;
    private final Map<AirplaneType, Button> typeButtons;

    public BuyAirplaneWindow(float x, float y, final ChooseHandler handler) {
        super(x, y, 250, 250);
        this.handler = handler;

        typeButtons = new HashMap<AirplaneType, Button>();

        int offy = 16;
        for (final AirplaneType airplaneType : AirplaneType.values()) {
            uiElements.add(new ImgField(20, h - offy - 40, airplaneType.imgBuy));
            uiElements.add(new TextField(140, h - offy - 26, 90, 20, airplaneType.name, -1, 0));
            uiElements.add(new TextField(140, h - offy - 46, 90, 20, GameScreen.formatMoney(airplaneType.price), -1, 0));
            Button button = new Button(10, h - offy - 46, 230, 46, null, new Button.OnClickHandler() {
                @Override
                public void onClick() {
                    handler.chose(airplaneType);
                }
            });
            typeButtons.put(airplaneType, button);
            uiElements.add(button);
            offy += 50;
        }

        uiElements.add(new Button(w - 42, 10, 32, 32, Img.close.t, new Button.OnClickHandler() {
            @Override
            public void onClick() {
                handler.chose(null);
            }
        }));
    }

    @Override
    public void update() {
        for (AirplaneType airplaneType : AirplaneType.values()) {
            typeButtons.get(airplaneType).enable(World.w.money >= airplaneType.price);
        }
    }

    public interface ChooseHandler {
        void chose(AirplaneType type);
    }
}
