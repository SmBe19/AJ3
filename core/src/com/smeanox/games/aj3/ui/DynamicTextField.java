package com.smeanox.games.aj3.ui;

public class DynamicTextField extends TextField {

    protected final TextFieldUpdater updater;

    public DynamicTextField(float x, float y, float w, float h, TextFieldUpdater updater) {
        super(x, y, w, h, "");
        this.updater = updater;
    }

    @Override
    public void update() {
        super.update();
        text = updater.update();
    }

    interface TextFieldUpdater {
        String update();
    }
}
