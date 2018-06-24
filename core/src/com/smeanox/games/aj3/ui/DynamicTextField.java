package com.smeanox.games.aj3.ui;

public class DynamicTextField extends TextField {

    protected final TextFieldUpdater updater;

    public DynamicTextField(float x, float y, float w, float h, TextFieldUpdater updater) {
        this(x, y, w, h, 0, 0,updater);
    }

    public DynamicTextField(float x, float y, float w, float h, float alignX, float alignY, TextFieldUpdater updater) {
        super(x, y, w, h, "", alignX, alignY);
        this.updater = updater;
    }

    @Override
    public void update() {
        super.update();
        text = updater.update(this);
    }

    interface TextFieldUpdater {
        String update(DynamicTextField textField);
    }
}
