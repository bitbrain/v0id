package de.bitbrain.v0id.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;

import de.bitbrain.braingdx.assets.SharedAssetManager;

public class BitmapFontBaker {

    public static BitmapFont bake(String fontPath, int fontSize) {
        FreeTypeFontGenerator generator = SharedAssetManager.getInstance().get(fontPath, FreeTypeFontGenerator.class);
        FreeTypeFontGenerator.FreeTypeFontParameter parameter = new FreeTypeFontGenerator.FreeTypeFontParameter();
        parameter.size = fontSize;
        parameter.mono = false;
        parameter.color = Color.WHITE.cpy();
        BitmapFont font = generator.generateFont(parameter);
        return font;
    }
}