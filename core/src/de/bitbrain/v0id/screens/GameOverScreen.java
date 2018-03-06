package de.bitbrain.v0id.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.PlayerStats;

public class GameOverScreen extends AbstractScreen {

    private final PlayerStats stats;

    private boolean exiting;

    private GameContext context;

    public GameOverScreen(BrainGdxGame game, PlayerStats stats) {
        super(game);
        this.stats = stats;
    }

    @Override
    protected void onCreate(GameContext context) {
        this.context = context;
        context.getScreenTransitions().in(0.5f);
        setBackgroundColor(Colors.DARK_SORROW);
    }

    @Override
    protected void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
            exiting = true;
            context.getScreenTransitions().out(new MainMenuScreen(getGame()), 1f);
        }
    }
}
