package de.bitbrain.v0id.core;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Rectangle;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.util.Mutator;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.graphics.ParticleManager;

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

    public static interface BulletListener {
        void onShoot(GameObject owner, GameObject bullet, GameObject target);
        void onKill(GameObject owner, GameObject bullet, GameObject target);
    }

    private final GameWorld gameWorld;

    private final Random random = new Random();

    private final BehaviorManager behaviorManager;
    private final Rectangle bulletCollision = new Rectangle(), targetCollision = new Rectangle();

    private final KillingMachine killingMachine;

    private final ParticleManager particleManager;

    private final Set<BulletListener> listeners = new HashSet<BulletListener>();

    public BulletMachine(GameWorld gameWorld, BehaviorManager behaviorManager, KillingMachine killingMachine, ParticleManager particleManager) {
        this.gameWorld = gameWorld;
        this.behaviorManager = behaviorManager;
        this.killingMachine = killingMachine;
        this.particleManager = particleManager;
    }

    private final Map<BulletType, Bullet> bullets = new HashMap<BulletType, Bullet>();

    public void addListener(BulletListener listener) {
        this.listeners.add(listener);
    }

    public void register(BulletType type, float size) {
        bullets.put(type, new Bullet(size));
    }

    public void spawn(GameObject source, final BulletType bulletType, float velocityX, float velocityY, String hitParticleEffectId) {
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
              bulletType, velocityX, velocityY, hitParticleEffectId);
    }

    private void spawn(GameObject source, final float x, final float y, final BulletType bulletType, float velocityX, float velocityY, String hitParticleEffectId) {
        final Bullet bullet = bullets.get(bulletType);
        GameObject bulletObject = gameWorld.addObject(new Mutator<GameObject>() {
            @Override
            public void mutate(GameObject target) {
                target.setDimensions(bullet.getSize(), bullet.getSize());
                target.setPosition(x, y);
                target.setType(bulletType);
                target.setAttribute(Attribute.HEALTH, 2);
                target.setAttribute(Attribute.KIND, Kind.BULLET);
            }
        }, true);
        behaviorManager.apply(createBehavior(source, velocityX, velocityY, hitParticleEffectId), bulletObject);
    }

    private Behavior createBehavior(final GameObject sender, final float velocityX, final float velocityY, final String hitParticleEffectId) {
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
                if (targetType.equals(sourceType)) {
                    return;
                }
                // Ignore the same kind
                if (sender.getAttribute(Attribute.PLAYER) == target.getAttribute(Attribute.PLAYER)) {
                    return;
                }
                bulletCollision.set(source.getLeft(), source.getTop(), source.getWidth(), source.getHeight());
                targetCollision.set(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());
                if (bulletCollision.overlaps(targetCollision) || targetCollision.contains(bulletCollision)) {
                    if (target.hasAttribute(Attribute.HEALTH)) {
                        Integer health = (Integer)target.getAttribute(Attribute.HEALTH);
                        if (health > 1) {
                            for (BulletListener listener : listeners) {
                                listener.onShoot(sender, source, target);
                            }
                            target.setAttribute(Attribute.HEALTH, health - 1);
                            pushBack(source, target);
                        } else {
                            for (BulletListener listener : listeners) {
                                listener.onShoot(sender, source, target);
                                listener.onKill(sender, source, target);
                            }
                            killingMachine.kill(target);
                        }
                        particleManager.attachEffect(hitParticleEffectId, target,
                                source.getLeft() - target.getLeft() + source.getWidth() / 2f,
                                source.getTop() - target.getTop() + source.getHeight() / 2f);
                    }
                    gameWorld.remove(source);
                }

            }
        };
    }

    private void pushBack(GameObject bullet, GameObject target) {
        final float pushDistance = 8f;
        if (bullet.getLastPosition().y < bullet.getTop()) {
            // Bullet is moving up
            pushInternally(target, pushDistance);
        } else {
            pushInternally(target, -pushDistance);
        }
    }

    private void pushInternally(GameObject object, float pushDistance) {
        SharedAssetManager.getInstance().get(Assets.Sounds.HIT, Sound.class).play(0.4f + random.nextFloat() * 0.5f, 0.7f + random.nextFloat() * 0.5f, 0f);
        object.move(0f, pushDistance);
    }
}
