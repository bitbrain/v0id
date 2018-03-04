package de.bitbrain.v0id.ui;

import de.bitbrain.v0id.core.PlayerStats;

public class PlayerScoreLabel extends HeightedLabel {

    private final PlayerStats stats;

    public PlayerScoreLabel(PlayerStats stats) {
        super("", Styles.LABEL_TEXT_POINTS);
        this.stats = stats;
    }

    @Override
    public void act(float delta) {
        setText(String.valueOf(stats.getPoints()));
        super.act(delta);
    }
}
