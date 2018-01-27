package de.bitbrain.v0id;

import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;
import com.badlogic.gdx.maps.tiled.TiledMap;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.screens.IngameScreen;

public class V0idGame extends BrainGdxGame {

    @Override
    protected GameAssetLoader getAssetLoader() {
        SmartAssetLoader.SmartAssetLoaderConfiguration config = new SmartAssetLoader.SmartAssetLoaderConfiguration() {
            @Override
            public Map<String, Class<?>> getClassMapping() {
                final Map<String, Class<?>> mapping = new HashMap<String, Class<?>>();
                mapping.putAll(SmartAssetLoader.defaultConfiguration().getClassMapping());
                mapping.put("Fonts", FreeTypeFontGenerator.class);
                return mapping;
            }
        };
        SharedAssetManager.getInstance().setLoader(FreeTypeFontGenerator.class, new FreeTypeFontGeneratorLoader(new InternalFileHandleResolver()));
        return new SmartAssetLoader(Assets.class, config);
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
        return new IngameScreen(this);
    }
}
