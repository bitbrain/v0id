package de.bitbrain.v0id.core;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;
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
        object.setAttribute(Attribute.KIND, Kind.SHIP);
        object.setAttribute(Attribute.POINTS, template.points);
        object.setAttribute(Attribute.MOVEMENT_DATA, new MovementData(template.accellerationFactor, template.minVelocity, template.maxVelocity));
        if (template.weapon != null) {
            object.setAttribute(Attribute.WEAPON, weaponFactory.attachWeapon(template.weapon, object));
        }
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

    public GameObject spawnConsumable(ConsumableTemplate template, float x, float y) {
        GameObject object = context.getGameWorld().addObject(true);
        object.setPosition(x, y);
        object.setDimensions(64f, 64f);
        object.setType(template.type);
        object.setAttribute(Attribute.KIND, Kind.CONSUMABLE);
        object.setAttribute(Attribute.CONSUMABLE, template.consumable);
        context.getBehaviorManager().apply(new BehaviorAdapter() {
            @Override
            public void update(GameObject source, float delta) {
                source.rotate(0.2f * delta);
                source.move(0f, GameConfig.BASE_SPEED * delta);
            }
        }, object);
        return object;
    }
}
