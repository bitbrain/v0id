package de.bitbrain.v0id.core;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

public class KillingMachine {

    private static final float DEATH_DURATION = 1.5f;

    private final GameWorld world;

    private final Respawner respawner;

    public KillingMachine(GameWorld world, Respawner respawner) {
        this.world = world;
        this.respawner = respawner;
    }

    public void kill(final GameObject object) {
        if (!object.hasAttribute(Attribute.DEAD) || object.getAttribute(Attribute.DEAD).equals(false)) {
            object.setAttribute(Attribute.HEALTH, 0);
            object.setAttribute(Attribute.DEAD, true);
            object.setActive(false);
            Tween.to(object, GameObjectTween.ALPHA, DEATH_DURATION)
                    .target(0f)
                    .ease(TweenEquations.easeOutCubic)
                    .start(SharedTweenManager.getInstance());
            Tween.to(object, GameObjectTween.SCALE, DEATH_DURATION)
                    .target(4f)
                    .ease(TweenEquations.easeOutCubic)
                    .setCallbackTriggers(TweenCallback.COMPLETE)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int i, BaseTween<?> baseTween) {
                            if (object.hasAttribute(Attribute.PLAYER)) {
                                respawner.respawn(object);
                            } else {
                                world.remove(object);
                            }
                        }
                    })
                    .start(SharedTweenManager.getInstance());
        }
    }
}
