package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;

import java.util.ArrayList;
import java.util.List;

public class WindowManager extends InputAdapter {

    public static WindowManager wm;

    public final OrthographicCamera camera;
    public final List<Window> windows;
    private Window currentWindow;
    private float touchDownX = -1, touchDownY = -1;
    private boolean dragging = false;
    private Vector3 unproject = new Vector3();

    public WindowManager(OrthographicCamera camera) {
        wm = this;
        this.camera = camera;
        windows = new ArrayList<Window>();
    }

    public void addWindow(Window window) {
        if (windows.contains(window)) {
            for (Window window1 : windows) {
                if (window.equals(window1)) {
                    bringToFront(window1);
                    break;
                }
            }
        } else {
            windows.add(0, window);
        }
    }

    public void closeWindow(Window window) {
        windows.remove(window);
    }

    public void bringToFront(Window window) {
        if (windows.get(0) != window) {
            windows.remove(window);
            windows.add(0, window);
        }
    }

    public void update() {
        for (Window window : windows) {
            window.update();
        }
    }

    public void render(SpriteBatch spriteBatch) {
        for (int i = windows.size() - 1; i >= 0; i--) {
            windows.get(i).render(spriteBatch);
        }
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        camera.unproject(unproject.set(screenX, screenY, 0));
        currentWindow = null;
        for (Window window : windows) {
            if (window.inside(unproject.x, unproject.y)) {
                currentWindow = window;
                break;
            }
        }
        if (currentWindow == null) {
            return false;
        }
        bringToFront(currentWindow);
        dragging = unproject.y > currentWindow.y + currentWindow.h - 11;
        touchDownX = unproject.x;
        touchDownY = unproject.y;

        if (dragging && unproject.x > currentWindow.x + currentWindow.w - 11) {
            closeWindow(currentWindow);
            currentWindow = null;
            dragging = false;
        } else if (!dragging) {
            currentWindow.onTouchDown(unproject.x, unproject.y);
        }

        return true;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        if (currentWindow == null) {
            return false;
        }
        if (dragging) {
            camera.unproject(unproject.set(screenX, screenY, 0));
            currentWindow.x += unproject.x - touchDownX;
            currentWindow.y += unproject.y - touchDownY;
            touchDownX = unproject.x;
            touchDownY = unproject.y;
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        if (currentWindow == null) {
            return false;
        }
        camera.unproject(unproject.set(screenX, screenY, 0));
        currentWindow.onTouchUp(unproject.x - currentWindow.x, unproject.y - currentWindow.y);
        currentWindow.x = MathUtils.clamp(currentWindow.x, -currentWindow.w * 0.8f, camera.viewportWidth - currentWindow.w * 0.2f);
        currentWindow.y = MathUtils.clamp(currentWindow.y, -currentWindow.h + 22,camera.viewportHeight - currentWindow.h);
        currentWindow = null;
        dragging = false;
        return true;
    }

    @Override
    public boolean scrolled(int amount) {
        camera.unproject(unproject.set(Gdx.input.getX(), Gdx.input.getY(), 0));
        for (Window window : windows) {
            if (window.scrolled(amount, unproject.x, unproject.y)) {
                return true;
            }
        }
        return false;
    }
}
