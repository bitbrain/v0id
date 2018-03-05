package de.bitbrain.v0id.screens;


import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.v0id.core.PlayerStats;

public class GameOverScreen extends AbstractScreen {

    private final PlayerStats stats;

    public GameOverScreen(BrainGdxGame game, PlayerStats stats) {
        super(game);
        this.stats = stats;
    }

    @Override
    protected void onCreate(GameContext context) {

    }
}
