package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;

public class PlayerStats {

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

    public int getTotalLifeCount() {
        return GameConfig.PLAYER_LIFE_COUNT;
    }

    public int getCurrentHealth() {
        return (Integer)player.getAttribute(Attribute.HEALTH);
    }

    public int getTotalHealth() {
        return (Integer)player.getAttribute(Attribute.INITIAL_HEALTH);
    }
}
