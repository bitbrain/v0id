package de.bitbrain.v0id.core;

import java.util.ArrayList;
import java.util.List;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class ShootingBehavior extends BehaviorAdapter {

    private final List<Weapon> weapons = new ArrayList<Weapon>();

    public void addWeapon(Weapon weapon) {
        this.weapons.add(weapon);
    }

    @Override
    public void update(GameObject source, float delta) {
        super.update(source, delta);
        for (Weapon weapon : weapons) {
            weapon.shoot(source, delta);
        }
    }
}
