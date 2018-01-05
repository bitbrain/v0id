package de.bitbrain.v0id;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.assets.GameAssetLoader;
import de.bitbrain.braingdx.assets.SmartAssetLoader;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.screens.IngameScreen;

public class V0idGame extends BrainGdxGame {

    @Override
    protected GameAssetLoader getAssetLoader() {
        return new SmartAssetLoader(Assets.class);
    }

    @Override
    protected AbstractScreen<?> getInitialScreen() {
        return new IngameScreen(this);
    }
}
