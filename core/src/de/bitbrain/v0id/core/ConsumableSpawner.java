package de.bitbrain.v0id.core;

import java.util.Random;

import de.bitbrain.braingdx.world.GameObject;

public class ConsumableSpawner {

    private final GameObjectFactory factory;
    private final Random random = new Random();

    public ConsumableSpawner(GameObjectFactory factory) {
        this.factory = factory;
    }

    public void spawn(GameObject target) {
        for (ConsumableTemplate template : TemplateService.consumableTemplates) {
            if (template.likelihood > random.nextFloat()) {
                factory.spawnConsumable(template, target.getLeft(), target.getTop());
                return;
            }
        }
    }
}
