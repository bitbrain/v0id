package de.bitbrain.v0id.ui;

import com.badlogic.gdx.graphics.Color;

import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.graphics.BitmapFontBaker;
import de.bitbrain.v0id.Colors;

public class Styles {

    public static final HeightedLabelStyle LABEL_DEBUG_SMALL = new HeightedLabelStyle();
    public static final HeightedLabelStyle LABEL_TEXT_MEDIUM = new HeightedLabelStyle();
    public static final HeightedLabelStyle LABEL_TEXT_POINTS = new HeightedLabelStyle();
    public static final HeightedLabelStyle LABEL_TEXT_TOOLTIP = new HeightedLabelStyle();
    public static final HeightedLabelStyle LABEL_TEXT_TOOLTIP_OVERLAY = new HeightedLabelStyle();
    public static final HeightedLabelStyle LABEL_TEXT_TOOLTIP_DEATH = new HeightedLabelStyle();
    public static final HeightedLabelStyle LABEL_CAPTION = new HeightedLabelStyle();

    public static void init() {
        LABEL_TEXT_MEDIUM.fontColor = Colors.NEON_CHRIMSON;
        LABEL_TEXT_MEDIUM.fontColorHeighted = Colors.NEON_CHRIMSON;
        LABEL_TEXT_MEDIUM.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 26);

        LABEL_CAPTION.fontColor = Colors.NEON_LIME;
        LABEL_CAPTION.fontColorHeighted = Colors.LIGHT_SORROW;
        LABEL_CAPTION.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 116);

        LABEL_TEXT_POINTS.fontColor = Colors.NEON_LIME;
        LABEL_TEXT_POINTS.fontColorHeighted = Colors.LIGHT_SORROW;
        LABEL_TEXT_POINTS.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 56);

        LABEL_TEXT_TOOLTIP.fontColor = Colors.NEON_LIME;
        LABEL_TEXT_TOOLTIP.fontColorHeighted = Colors.LIGHT_SORROW;
        LABEL_TEXT_TOOLTIP.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 46);

        LABEL_TEXT_TOOLTIP_OVERLAY.fontColor = Colors.LIGHT_SORROW;
        LABEL_TEXT_TOOLTIP_OVERLAY.fontColorHeighted = Colors.LIGHT_SORROW;
        LABEL_TEXT_TOOLTIP_OVERLAY.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 72);

        LABEL_TEXT_TOOLTIP_DEATH.fontColor = Colors.NEON_CHRIMSON;
        LABEL_TEXT_TOOLTIP_DEATH.fontColorHeighted = Colors.DARK_CHRIMSON;
        LABEL_TEXT_TOOLTIP_DEATH.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 46);

        LABEL_DEBUG_SMALL.fontColor = Color.WHITE;
        LABEL_DEBUG_SMALL.fontColorHeighted = Color.WHITE;
        LABEL_DEBUG_SMALL.font = BitmapFontBaker.bake(Assets.Fonts.MAIN, 16);
    }
}
