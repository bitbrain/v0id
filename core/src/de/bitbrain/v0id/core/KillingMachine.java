package de.bitbrain.v0id.core;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.graphics.ScreenShake;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

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
            Tooltip.getInstance().create(object, Styles.LABEL_TEXT_TOOLTIP, "KILLED!");
            object.setAttribute(Attribute.HEALTH, 0);
            object.setAttribute(Attribute.DEAD, true);
            object.setActive(false);
            ScreenShake.shake(3f, 1f);
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
