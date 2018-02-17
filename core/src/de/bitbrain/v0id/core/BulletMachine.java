package de.bitbrain.v0id.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.Map;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
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
    private final Rectangle bulletCollision = new Rectangle(), targetCollision = new Rectangle();

    private final KillingMachine killingMachine;

    public BulletMachine(GameWorld gameWorld, BehaviorManager behaviorManager, KillingMachine killingMachine) {
        this.gameWorld = gameWorld;
        this.behaviorManager = behaviorManager;
        this.killingMachine = killingMachine;
    }

    private final Map<BulletType, Bullet> bullets = new HashMap<BulletType, Bullet>();

    public void register(BulletType type, float size) {
        bullets.put(type, new Bullet(size));
    }

    public void spawn(GameObject source, final BulletType bulletType, float velocityX, float velocityY) {
        final Bullet bullet = bullets.get(bulletType);
        if (bullet == null) {
            Gdx.app.error("bullet", "Bullet type " + bulletType + " not registered.");
            return;
        }
        float top = source.getTop() + source.getHeight();
        if (!source.hasAttribute(Attribute.PLAYER)) {
            top = source.getTop() - source.getHeight();
            velocityY = -velocityY;
        }
        spawn(source, source.getLeft() + source.getWidth() / 2f - bullet.getSize() / 2f,
              top,
              bulletType, velocityX, velocityY);
    }

    private void spawn(GameObject source, final float x, final float y, final BulletType bulletType, float velocityX, float velocityY) {
        final Bullet bullet = bullets.get(bulletType);
        GameObject bulletObject = gameWorld.addObject(new Mutator<GameObject>() {
            @Override
            public void mutate(GameObject target) {
                target.setDimensions(bullet.getSize(), bullet.getSize());
                target.setPosition(x, y);
                target.setType(bulletType);
            }
        }, true);
        behaviorManager.apply(createBehavior(source, velocityX, velocityY), bulletObject);
    }

    private Behavior createBehavior(final GameObject source, final float velocityX, final float velocityY) {
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
                // Do ignore source
                if (targetType.equals(source)) {
                    return;
                }
                // Do ignore bullets
                if (sourceType instanceof BulletType && targetType instanceof BulletType) {
                    return;
                }
                bulletCollision.set(source.getLeft(), source.getTop(), source.getWidth(), source.getHeight());
                targetCollision.set(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());
                if (bulletCollision.overlaps(targetCollision) || targetCollision.contains(bulletCollision)) {
                    if (target.hasAttribute(Attribute.HEALTH)) {
                        Integer health = (Integer)target.getAttribute(Attribute.HEALTH);
                        if (health > 1) {
                            target.setAttribute(Attribute.HEALTH, health - 1);
                            pushBack(source, target);
                        } else {
                            killingMachine.kill(target);
                        }
                    }
                    gameWorld.remove(source);
                }

            }
        };
    }

    private void pushBack(GameObject bullet, GameObject target) {
        final float pushDistance = 32f;
        if (bullet.getLastPosition().y < bullet.getTop()) {
            // Bullet is moving up
            pushInternally(target, pushDistance);
        } else {
            pushInternally(target, -pushDistance);
        }
    }

    private void pushInternally(GameObject object, float pushDistance) {
        object.move(0f, pushDistance);
        object.setOffset(0f, -pushDistance);
        Tween.to(object, GameObjectTween.OFFSET_Y, 2f)
                .target(0f)
                .ease(TweenEquations.easeOutCubic)
                .start(SharedTweenManager.getInstance());
    }
}
