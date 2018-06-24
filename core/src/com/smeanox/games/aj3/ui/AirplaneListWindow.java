package com.smeanox.games.aj3.ui;

import com.smeanox.games.aj3.world.Airplane;
import com.smeanox.games.aj3.world.World;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class AirplaneListWindow extends Window {

    Map<Airplane, UIGroup> groups;
    List<UIElement> list;

    public AirplaneListWindow(float x, float y) {
        super(x, y, 300, 300);
        groups = new LinkedHashMap<Airplane, UIGroup>();
        list = new ArrayList<UIElement>();
        uiElements.add(new ScrollList(10, 10, 280, 270, new ScrollList.ListProvider() {

            @Override
            public List<UIElement> provide() {
                return list;
            }
        }));
    }

    @Override
    public void update() {
        super.update();

        boolean changed = false;
        for (final Airplane airplane : World.w.airplanes) {
            if (!groups.containsKey(airplane)) {
                changed = true;
                groups.put(airplane, new UIGroup(0, 0,
                        new DynamicTextField(0, 0, 280, 20, new DynamicTextField.TextFieldUpdater() {
                            @Override
                            public String update(DynamicTextField textField) {
                                return airplane.getFullName() + " (" + airplane.getLocationString() + ")";
                            }
                        }) {
                            @Override
                            public boolean touchDown(float xx, float yy) {
                                WindowManager.wm.addWindow(new RouteEditWindow(AirplaneListWindow.this.x - 300, AirplaneListWindow.this.y + AirplaneListWindow.this.h - 300, airplane));
                                return true;
                            }
                        }));
            }
        }
        if (World.w.airplanes.size() != groups.size()) {
            for (Airplane airplane : groups.keySet()) {
                if (!World.w.airplanes.contains(airplane)) {
                    changed = true;
                    groups.remove(airplane);
                }
            }
        }
        if (changed) {
            list.clear();
            list.addAll(groups.values());
        }
    }
}
