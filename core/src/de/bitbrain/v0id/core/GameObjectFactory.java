package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.ai.RegularEnemyBehavior;

public class GameObjectFactory {

    private final GameWorld world;
    private final BehaviorManager behaviorManager;
    private final BulletMachine bulletMachine;

    public GameObjectFactory(GameWorld world, BehaviorManager behaviorManager, BulletMachine bulletMachine) {
        this.world = world;
        this.behaviorManager = behaviorManager;
        this.bulletMachine = bulletMachine;
    }

    public GameObject spawnMeteror() {

        GameObject object = world.addObject();
        object.setAttribute(Attribute.HEALTH, 5);
        object.setType("block");
        return object;
    }

    public GameObject spawnEnemy() {
        GameObject object = world.addObject();
        object.setAttribute(Attribute.HEALTH, 5);
        object.setType("viper");
        behaviorManager.apply(new RegularEnemyBehavior(), object);
        ShootingBehavior shootingBehavior = new ShootingBehavior();
        shootingBehavior.addWeapon(new Weapon(BulletType.PLASMA, bulletMachine, 1f, 0.0f, -500f));
        behaviorManager.apply(shootingBehavior, object);
        return object;
    }
}
