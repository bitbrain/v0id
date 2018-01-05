package de.bitbrain.v0id.levelgen;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

public class WorldGenerator {

    private final GameWorld world;

    private final Camera camera;

    private final Random random = new Random();

    private final DeltaTimer timer = new DeltaTimer();

    public WorldGenerator(GameWorld world, Camera camera) {
        this.world = world;
        this.camera = camera;
    }

    public void update(float delta) {
        timer.update(delta);
        if (timer.reached(1f)) {
            timer.reset();
            GameObject object = world.addObject();
            object.setType("block");
            object.setDimensions(128f, 32f);
            object.setPosition(camera.position.x - camera.viewportWidth / 2f + (camera.viewportWidth * random.nextFloat()), camera.position.y + camera.viewportHeight / 2f + 32f);
            if (object.getLeft() < camera.position.x - camera.viewportWidth / 2f) {
                object.setPosition(camera.position.x - camera.viewportWidth / 2f, object.getTop());
            }
            if (object.getLeft() + object.getWidth() > camera.position.x + camera.viewportWidth / 2f) {
                object.setPosition(camera.position.x + camera.viewportWidth / 2f, object.getTop());
            }
        }
    }
}
