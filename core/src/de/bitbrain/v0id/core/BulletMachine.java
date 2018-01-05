package de.bitbrain.v0id.core;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.GameConfig;

public class BulletMachine {

    private final GameWorld gameWorld;

    private final BehaviorManager behaviorManager;

    private final Camera camera;

    public BulletMachine(GameWorld gameWorld, BehaviorManager behaviorManager, Camera camera) {
        this.gameWorld = gameWorld;
        this.behaviorManager = behaviorManager;
        this.camera = camera;
    }

    public void spawn(final float x, final float y, final String bulletType, Vector2 velocity) {
        GameObject bullet = gameWorld.addObject(new Mutator<GameObject>() {
            @Override
            public void mutate(GameObject target) {
                target.setDimensions(20f, 20f);
                target.setPosition(x, y);
                target.setType(bulletType);
            }
        });
        behaviorManager.apply(createBehavior(bulletType, velocity), bullet);
    }

    private Behavior createBehavior(String bulletType, final Vector2 velocity) {
        return new BehaviorAdapter() {
            @Override
            public void update(GameObject source, float delta) {
                super.update(source, delta);
                float cameraLeft = camera.position.x - camera.viewportWidth / 2f;
                float cameraRight = camera.position.x + camera.viewportWidth / 2f;
                float cameraTop = camera.position.y - camera.viewportHeight / 2f;
                float cameraBottom = camera.position.y + camera.viewportHeight / 2f;
                if (cameraLeft > source.getLeft() + source.getWidth()) {
                    gameWorld.remove(source);
                } else if (cameraRight < source.getLeft()) {
                    gameWorld.remove(source);
                } else if (cameraTop > source.getTop() + source.getHeight()) {
                    gameWorld.remove(source);
                } else if (cameraBottom < source.getTop()) {
                    gameWorld.remove(source);
                } else {
                    float speedX = (velocity.x) * delta;
                    float speedY = (GameConfig.BASE_SPEED + velocity.y) * delta;
                    source.move(speedX, speedY);
                }
            }

            @Override
            public void update(GameObject source, GameObject target, float delta) {
                super.update(source, target, delta);
            }
        };
    }
}
