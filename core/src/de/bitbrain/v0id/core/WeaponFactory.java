package de.bitbrain.v0id.core;

import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.world.GameObject;

public class WeaponFactory {

    private final BulletMachine bulletMachine;
    private final BehaviorManager behaviorManager;

    public WeaponFactory(BulletMachine bulletMachine, BehaviorManager behaviorManager) {
        this.bulletMachine = bulletMachine;
        this.behaviorManager = behaviorManager;
    }

    public Weapon attachWeapon(WeaponTemplate weaponTemplate, GameObject object) {
        Weapon weapon = new Weapon(weaponTemplate.type, weaponTemplate.soundId, bulletMachine, weaponTemplate.frequency, weaponTemplate.velocityX, weaponTemplate.velocityY);
        ShootingBehavior shootingBehavior = new ShootingBehavior();
        shootingBehavior.addWeapon(weapon);
        behaviorManager.apply(shootingBehavior, object);
        return weapon;
    }
}
