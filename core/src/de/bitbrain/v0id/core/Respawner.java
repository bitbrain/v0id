package de.bitbrain.v0id.core;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;

import java.util.Random;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;

public class Respawner {

    private final Camera camera;

    private final Random random = new Random();

    public Respawner(Camera camera) {
        this.camera = camera;
    }

    public void respawn(GameObject object) {
        if (object.hasAttribute(Attribute.PLAYER) && object.hasAttribute(Attribute.GAME_OVER)) {
            Gdx.app.log("INFO", "Do not respawn player -> Game Over!");
            return;
        }
        //SharedAssetManager.getInstance().get(Assets.Sounds.RESPAWN, Sound.class).play(0.2f + random.nextFloat() * 0.2f, 0.9f + random.nextFloat() * 0.2f, 0f);
        object.setAttribute(Attribute.HEALTH, object.getAttribute(Attribute.INITIAL_HEALTH));
        object.setAttribute(Attribute.DEAD, false);
        object.getScale().set(0f, 0f);
        object.setPosition(camera.position.x - object.getWidth() / 2f, camera.position.y - object.getHeight() / 2f);
        Tween.to(object, GameObjectTween.SCALE, 0.5f)
             .target(1f)
             .ease(TweenEquations.easeOutCubic)
             .start(SharedTweenManager.getInstance());
    }
}
