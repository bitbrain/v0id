package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

public class GameObjectFactory {

    private final GameWorld world;

    public GameObjectFactory(GameWorld world) {
        this.world = world;
    }

    public GameObject spawnMeteror() {

        GameObject object = world.addObject();
        object.setAttribute(Attribute.HEALTH, 5);
        object.setType("block");
        return object;
    }
}
