package de.bitbrain.v0id.core.consumables;

import com.badlogic.gdx.audio.Sound;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.Consumable;
import de.bitbrain.v0id.core.Weapon;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

public class WeaponUpgradeConsumable implements Consumable {
    
    @Override
    public void consume(GameObject target) {
        if (target.hasAttribute(Attribute.WEAPON)) {
            Weapon weapon = (Weapon)target.getAttribute(Attribute.WEAPON);
            weapon.upgrade();
            Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_WEAPON_UP, "WEAPON UPGRADED!");
            SharedAssetManager.getInstance().get(Assets.Sounds.WEAPON_UP, Sound.class).play(0.3f);
        }
    }
}
