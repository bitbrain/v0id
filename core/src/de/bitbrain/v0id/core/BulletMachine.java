package de.bitbrain.v0id.core;


import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Rectangle;
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

    private final Rectangle bulletCollision = new Rectangle(), targetCollision = new Rectangle();

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
        float top = 0f;
        // Moved downwards, shoot down!
        if (velocityY < 0) {
            top = source.getTop() - source.getHeight();
        } else {
            top = source.getTop() + source.getHeight();
        }
        spawn(source.getLeft() + source.getWidth() / 2f - bullet.getSize() / 2f,
              top,
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

                float speedX = (velocityX) * delta;
                float speedY = (GameConfig.BASE_SPEED + velocityY) * delta;
                source.move(speedX, speedY);
            }

            @Override
            public void update(GameObject source, GameObject target, float delta) {
                Object sourceType = source.getType();
                Object targetType = target.getType();
                // Do ignore bullets
                if (sourceType instanceof BulletType && targetType instanceof BulletType) {
                    return;
                }
                bulletCollision.set(source.getLeft(), source.getTop(), source.getWidth(), source.getHeight());
                targetCollision.set(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());
                if (bulletCollision.overlaps(targetCollision)) {
                    if (target.hasAttribute(Attribute.HEALTH)) {
                        Integer health = (Integer)target.getAttribute(Attribute.HEALTH);
                        if (health > 0) {
                            target.setAttribute(Attribute.HEALTH, health - 1);
                        }
                        health = (Integer)target.getAttribute(Attribute.HEALTH);
                        if (health < 1) {
                            gameWorld.remove(target);
                        }
                    }
                    gameWorld.remove(source);
                }

            }
        };
    }
}
