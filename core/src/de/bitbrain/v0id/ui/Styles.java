package de.bitbrain.v0id.ui;

import com.badlogic.gdx.scenes.scene2d.ui.Label;

import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.graphics.BitmapFontBaker;
import de.bitbrain.v0id.graphics.Colors;

public class Styles {

    public static final Label.LabelStyle LABEL_TEXT_MEDIUM = new Label.LabelStyle();
    public static final Label.LabelStyle LABEL_TEXT_TOOLTIP = new Label.LabelStyle();

    public static void init() {
        LABEL_TEXT_MEDIUM.fontColor = Colors.NEON_CHRIMSON;
        LABEL_TEXT_MEDIUM.font = BitmapFontBaker.bake(Assets.Fonts.BUNGEE, 26);
        LABEL_TEXT_TOOLTIP.fontColor = Colors.NEON_CHRIMSON;
        LABEL_TEXT_TOOLTIP.font = BitmapFontBaker.bake(Assets.Fonts.BUNGEE, 46);
    }
}
