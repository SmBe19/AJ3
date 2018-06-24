package com.smeanox.games.aj3.ui;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.scenes.scene2d.utils.ScissorStack;

import java.util.List;

public class ScrollList extends UIGroup {

    protected final ListProvider provider;
    private Rectangle boundingRectangle = new Rectangle();
    private Rectangle scissors = new Rectangle();

    private float currentOffset;

    public ScrollList(float x, float y, float w, float h, ListProvider provider) {
        super(x, y, provider.provide());
        this.provider = provider;
        this.w = w;
        this.h = h;
    }

    @Override
    protected void doRender(SpriteBatch spriteBatch, float offX, float offY) {
        elements.clear();
        elements.addAll(provider.provide());
        if (elements.isEmpty()) {
            return;
        }
        float aOff = 0;
        for (UIElement element : elements) {
            element.y = h - aOff + currentOffset - element.h;
            aOff += element.h;
        }
        boundingRectangle.set(offX + x, offY + y, w, h);
        spriteBatch.flush();
        ScissorStack.calculateScissors(WindowManager.wm.camera, spriteBatch.getTransformMatrix(), boundingRectangle, scissors);
        ScissorStack.pushScissors(scissors);
        for (UIElement uiElement : elements) {
            uiElement.render(spriteBatch, offX + x, offY + y);
        }
        spriteBatch.flush();
        ScissorStack.popScissors();
    }

    @Override
    public boolean scrolled(float amount, float xx, float yy) {
        if (!super.scrolled(amount, xx, yy)) {
            currentOffset += amount * 7.5f;

            float maxOff = 0;
            for (UIElement element : elements) {
                maxOff += element.h;
            }
            if (maxOff < h) {
                currentOffset = 0;
            } else {
                currentOffset = MathUtils.clamp(currentOffset, 0, maxOff - h);
            }
        }
        return true;
    }

    @Override
    public boolean inside(float xx, float yy) {
        return visible && x <= xx && xx <= x + w && y <= yy && yy <= y + h;
    }

    public interface ListProvider {
        List<UIElement> provide();
    }
}
