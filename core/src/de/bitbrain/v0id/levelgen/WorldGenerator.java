package de.bitbrain.v0id.levelgen;

import com.badlogic.gdx.graphics.Camera;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.GameObjectFactory;
import de.bitbrain.v0id.core.ShipSpawnTemplate;
import de.bitbrain.v0id.core.TemplateService;

public class WorldGenerator {

    private final GameObjectFactory factory;

    private final Camera camera;

    private final Random random = new Random();

    private final GameObject player;

    private float time;

    private final Map<Object, DeltaTimer> timers = new HashMap<Object, DeltaTimer>();

    public WorldGenerator(GameObjectFactory factory, Camera camera, GameObject player) {
        this.factory = factory;
        this.camera = camera;
        this.player = player;
        for (ShipSpawnTemplate template : TemplateService.shipTemplates) {
            timers.put(template.type, new DeltaTimer());
        }
    }

    public void update(float delta) {
        time += delta;
        for (ShipSpawnTemplate template : TemplateService.shipTemplates) {
            if (template.type.equals(player.getType())) {
                continue;
            }
            DeltaTimer timer = timers.get(template.type);
            timer.update(delta);
            if (timer.reached(template.interval)) {
                int spawnRange = (int) Math.ceil(time / 60f);
                int count =  template.likelihood > random.nextFloat() ? random.nextInt(spawnRange) + 1 : 1;
                for (int i = 0; i < count; ++i) {
                    if (template.likelihood > random.nextFloat()) {
                        // Do spawn a new enemy
                        factory.spawnShip(template, getRandomX(), getCameraTop());
                    }
                }
                timer.reset();
            }
        }
    }

    private float getRandomX() {
        float x = camera.position.x - camera.viewportWidth / 2f + (camera.viewportWidth * random.nextFloat());
        if (x < camera.position.x - camera.viewportWidth / 2f) {
            return camera.position.x - camera.viewportWidth / 2f;
        }
        return x;
    }

    private float getCameraTop() {
        return camera.position.y + camera.viewportHeight / 2f - 1;
    }
}
