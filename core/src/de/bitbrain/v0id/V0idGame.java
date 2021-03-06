package de.bitbrain.v0id;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.assets.loaders.resolvers.InternalFileHandleResolver;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGeneratorLoader;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.screens.IntroScreen;
import de.bitbrain.v0id.ui.Styles;

import static com.badlogic.gdx.Application.LOG_DEBUG;

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
        if (GameConfig.DEBUG_MODE) {
            Gdx.app.setLogLevel(LOG_DEBUG);
        }
        Styles.init();
        return new IntroScreen(this);
    }
}
