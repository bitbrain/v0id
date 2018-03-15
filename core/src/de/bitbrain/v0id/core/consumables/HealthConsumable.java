package de.bitbrain.v0id.core.consumables;

import com.badlogic.gdx.audio.Sound;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.Consumable;
import de.bitbrain.v0id.core.PlayerStats;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

public class HealthConsumable implements Consumable {

    @Override
    public void consume(GameObject target) {
        if (target.hasAttribute(Attribute.PLAYER_STATS) && target.hasAttribute(Attribute.HEALTH)) {
            PlayerStats stats = (PlayerStats)target.getAttribute(Attribute.PLAYER_STATS);
            if (hasFullHealthAndLife(stats)) {
                Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_HEALTH_UP, "FULL LIFE!");
            }else if (hasFullHealth(target)) {
                Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_HEALTH_UP, "LIFE UP!");
                stats.increaseLifeCount();
            } else {
                Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_HEALTH_UP, "HEALTH UP!");
                target.setAttribute(Attribute.HEALTH, target.getAttribute(Attribute.INITIAL_HEALTH));
            }
            SharedAssetManager.getInstance().get(Assets.Sounds.HEALTH_UP, Sound.class).play(0.3f);
        }
    }

    private boolean hasFullHealthAndLife(PlayerStats stats) {
        return stats.getLifeCount() == stats.getTotalLifeCount() && stats.getCurrentHealth() == stats.getTotalHealth();
    }

    private boolean hasFullHealth(GameObject target) {
        return target.getAttribute(Attribute.HEALTH).equals(target.getAttribute(Attribute.INITIAL_HEALTH));
    }
}
