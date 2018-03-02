package de.bitbrain.v0id.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

public class HeightedLabel extends Label {

    private final HeightedLabelStyle style;

    private final Label underLabel;

    public HeightedLabel(CharSequence text, HeightedLabelStyle style) {
        super(text, style);
        this.style = style;
        LabelStyle copy = new LabelStyle(style);
        copy.fontColor = Color.WHITE;
        underLabel = new Label("", copy);
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        float offset = getPrefHeight() / 8f;
        underLabel.setPosition(getX(), getY() + getHeight() / 2f - offset);
        underLabel.setColor(style.fontColorHeighted);
        underLabel.setText(getText());
        underLabel.draw(batch, parentAlpha);

        super.draw(batch, parentAlpha);
    }
}