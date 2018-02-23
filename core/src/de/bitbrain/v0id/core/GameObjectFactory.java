package de.bitbrain.v0id.core;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.ai.RegularEnemyBehavior;
import de.bitbrain.v0id.core.movement.MovementData;

public class GameObjectFactory {

    private final GameContext context;
    private final WeaponFactory weaponFactory;

    public GameObjectFactory(GameContext context, WeaponFactory weaponFactory) {
        this.context = context;
        this.weaponFactory = weaponFactory;
    }

    public GameObject spawnShip(ShipSpawnTemplate template, float x, float y, boolean npc) {
        GameObject object = context.getGameWorld().addObject(true);
        object.setPosition(x, y);
        object.setAttribute(Attribute.INITIAL_HEALTH, template.life);
        object.setAttribute(Attribute.HEALTH, template.life);
        object.setType(template.type);
        object.setAttribute(Attribute.MOVEMENT_DATA, new MovementData(template.accellerationFactor, template.minVelocity, template.maxVelocity));
        weaponFactory.attachWeapon(template.weapon, object);
        if (npc) {
            RegularEnemyBehavior behavior = new RegularEnemyBehavior(context.getGameCamera().getInternal());
            context.getBehaviorManager().apply(behavior, object);
            object.setRotation(180f);
        }
        return object;
    }

    public GameObject spawnShip(ShipSpawnTemplate template, float x, float y) {
        return spawnShip(template, x, y, true);
    }
}
