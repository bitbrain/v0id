package de.bitbrain.v0id.core;

import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.ai.RegularEnemyBehavior;
import de.bitbrain.v0id.core.movement.MovementData;

public class GameObjectFactory {

    private final GameWorld world;
    private final BehaviorManager behaviorManager;
    private final WeaponFactory weaponFactory;

    public GameObjectFactory(GameWorld world, BehaviorManager behaviorManager, WeaponFactory weaponFactory) {
        this.world = world;
        this.behaviorManager = behaviorManager;
        this.weaponFactory = weaponFactory;
    }

    public GameObject spawnShip(ShipSpawnTemplate template, float x, float y, boolean npc) {
        GameObject object = world.addObject(true);
        object.setPosition(x, y);
        object.setAttribute(Attribute.INITIAL_HEALTH, template.life);
        object.setAttribute(Attribute.HEALTH, template.life);
        object.setType(template.type);
        object.setDimensions(64f, 64f);
        object.setAttribute(Attribute.MOVEMENT_DATA, new MovementData(template.accellerationFactor, template.minVelocity, template.maxVelocity));
        weaponFactory.attachWeapon(template.weapon, object);
        if (npc) {
            RegularEnemyBehavior behavior = new RegularEnemyBehavior();
            behaviorManager.apply(behavior, object);
            object.setRotation(180f);
        }
        return object;
    }

    public GameObject spawnShip(ShipSpawnTemplate template, float x, float y) {
        return spawnShip(template, x, y, true);
    }
}
