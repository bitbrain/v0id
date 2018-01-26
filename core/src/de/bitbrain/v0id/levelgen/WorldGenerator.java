package de.bitbrain.v0id.levelgen;

import com.badlogic.gdx.graphics.Camera;

import java.util.Random;

import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.GameObjectFactory;

public class WorldGenerator {

    private final GameObjectFactory factory;

    private final Camera camera;

    private final Random random = new Random();

    private final DeltaTimer obstacleTimer = new DeltaTimer();
    private final DeltaTimer enemyTimer = new DeltaTimer();

    private float obstacleOffset = 0f;
    private float enemyOffset = 0f;

    public WorldGenerator(GameObjectFactory factory, Camera camera) {
        this.factory = factory;
        this.camera = camera;
    }

    public void update(float delta) {
        obstacleTimer.update(delta);
        enemyTimer.update(delta);
        if (obstacleTimer.reached(2f + obstacleOffset)) {
            obstacleTimer.reset();
            obstacleOffset = random.nextFloat() * 4f;
            GameObject object = factory.spawnMeteror();
            object.setDimensions(64f, 64f);
            object.setPosition(camera.position.x - camera.viewportWidth / 2f + (camera.viewportWidth * random.nextFloat()), camera.position.y + camera.viewportHeight / 2f - 1);
            if (object.getLeft() < camera.position.x - camera.viewportWidth / 2f) {
                object.setPosition(camera.position.x - camera.viewportWidth / 2f, object.getTop());
            }
            if (object.getLeft() + object.getWidth() > camera.position.x + camera.viewportWidth / 2f) {
                object.setPosition(camera.position.x + camera.viewportWidth / 2f, object.getTop());
            }
        }
        if (enemyTimer.reached(3f + enemyOffset)) {
            enemyTimer.reset();
            enemyOffset = random.nextFloat() * 5f;
            GameObject object = factory.spawnEnemy();
            object.setDimensions(64f, 64f);
            object.setPosition(camera.position.x - camera.viewportWidth / 2f + (camera.viewportWidth * random.nextFloat()), camera.position.y + camera.viewportHeight / 2f - 1);
            if (object.getLeft() < camera.position.x - camera.viewportWidth / 2f) {
                object.setPosition(camera.position.x - camera.viewportWidth / 2f, object.getTop());
            }
            if (object.getLeft() + object.getWidth() > camera.position.x + camera.viewportWidth / 2f) {
                object.setPosition(camera.position.x + camera.viewportWidth / 2f, object.getTop());
            }
        }
    }
}
