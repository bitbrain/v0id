package de.bitbrain.v0id.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import de.bitbrain.braingdx.assets.SharedAssetManager;

public class BitmapFontBaker {

    public static BitmapFont bake(String fontPath, FreeTypeFontGenerator.FreeTypeFontParameter config) {
        FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
        return generator.generateFont(config);
    }
}