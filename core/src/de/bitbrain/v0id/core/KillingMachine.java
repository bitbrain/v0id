package de.bitbrain.v0id.core;

import com.badlogic.gdx.audio.Sound;

import java.util.Random;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.graphics.ParticleManager;
import de.bitbrain.v0id.graphics.ScreenShake;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

public class KillingMachine {

    private static final float DEATH_DURATION = 1.5f;

    private final GameWorld world;

    private final Respawner respawner;

    private final ParticleManager particleManager;

    private final Random random = new Random();

    public KillingMachine(GameWorld world, Respawner respawner, ParticleManager particleManager) {
        this.world = world;
        this.respawner = respawner;
        this.particleManager = particleManager;
    }

    public void kill(final GameObject object) {
        if (!object.hasAttribute(Attribute.DEAD) || object.getAttribute(Attribute.DEAD).equals(false)) {
            Tooltip.getInstance().create(object, Styles.LABEL_TEXT_TOOLTIP, "KILLED!");
            object.setAttribute(Attribute.HEALTH, 0);
            object.setAttribute(Attribute.DEAD, true);
            object.setActive(false);

            /*particleManager.attachEffect(
                    Assets.Particles.EXPLOSION,
                    object,
                    object.getWidth() / 2f,
                    object.getHeight() / 2f
            );*/
            ScreenShake.shake(4.5f, 1.5f);
            if (object.hasAttribute(Attribute.PLAYER)) {
                SharedAssetManager.getInstance().get(Assets.Sounds.DEATH, Sound.class).play(0.4f + random.nextFloat() * 0.5f, 0.7f + random.nextFloat() * 0.5f, 0f);
            } else {
                SharedAssetManager.getInstance().get(Assets.Sounds.EXPLODE_01, Sound.class).play(0.4f + random.nextFloat() * 0.5f, 0.7f + random.nextFloat() * 0.5f, 0f);
            }
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
