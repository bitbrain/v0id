package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

public class PlayerStats implements KillingMachine.KillingListener {

    private final GameObject player;

    private int points;

    private int lifeCount = GameConfig.PLAYER_LIFE_COUNT;

    public PlayerStats(GameObject player) {
        this.player = player;
    }

    public void addPoints(int points) {
        this.points += points;
    }

    public int getPoints() {
        return points;
    }

    public void reduceLifeCount() {
        if (!isGameOver()) {
            lifeCount--;
        }
    }

    public boolean isGameOver() {
        return lifeCount <= 0;
    }

    public int getLifeCount() {
        return lifeCount;
    }

    public int getCurrentHealth() {
        return (Integer)player.getAttribute(Attribute.INITIAL_HEALTH);
    }

    public int getTotalHealth() {
        return (Integer)player.getAttribute(Attribute.HEALTH);
    }

    @Override
    public void onKill(GameObject target) {
        if (target.hasAttribute(Attribute.POINTS) && !target.hasAttribute(Attribute.PLAYER)) {
            int points = (Integer)target.getAttribute(Attribute.POINTS);
            addPoints(points);
            Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP, String.valueOf(points));
        } else if (target.hasAttribute(Attribute.PLAYER)) {
            Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP, "BOOM!");
        }
    }
}
