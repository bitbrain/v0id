package de.bitbrain.v0id.core;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.GameConfig;

public class BulletMachine {

    public final class Bullet {

        private final float size;

        Bullet(float size) {
            this.size = size;
        }

        public float getSize() {
            return size;
        }
    }

    private final GameWorld gameWorld;

    private final BehaviorManager behaviorManager;

    private final Camera camera;

    public BulletMachine(GameWorld gameWorld, BehaviorManager behaviorManager, Camera camera) {
        this.gameWorld = gameWorld;
        this.behaviorManager = behaviorManager;
        this.camera = camera;
    }

    private final Map<BulletType, Bullet> bullets = new HashMap<BulletType, Bullet>();

    public void register(BulletType type, float size) {
        bullets.put(type, new Bullet(size));
    }

    public void spawn(GameObject source, final BulletType bulletType, float velocityX, float velocityY) {
        final Bullet bullet = bullets.get(bulletType);
        spawn(source.getLeft() + source.getWidth() / 2f - bullet.getSize() / 2f,
              source.getTop() + source.getHeight(),
              bulletType, velocityX, velocityY);
    }

    public void spawn(final float x, final float y, final BulletType bulletType, float velocityX, float velocityY) {
        final Bullet bullet = bullets.get(bulletType);
        GameObject bulletObject = gameWorld.addObject(new Mutator<GameObject>() {
            @Override
            public void mutate(GameObject target) {
                target.setDimensions(bullet.getSize(), bullet.getSize());
                target.setPosition(x, y);
                target.setType(bulletType);
            }
        }, true);
        behaviorManager.apply(createBehavior(velocityX, velocityY), bulletObject);
    }

    private Behavior createBehavior(final float velocityX, final float velocityY) {
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
                    float speedX = (velocityX) * delta;
                    float speedY = (GameConfig.BASE_SPEED + velocityY) * delta;
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
