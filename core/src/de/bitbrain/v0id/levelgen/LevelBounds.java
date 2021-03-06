package de.bitbrain.v0id.levelgen;

import com.badlogic.gdx.graphics.OrthographicCamera;

import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.braingdx.world.GameWorld;

public class LevelBounds implements GameWorld.WorldBounds {

    @Override
    public boolean isInBounds(GameObject object, OrthographicCamera camera) {
        float tolerance = 1f;
        float cameraBottom = camera.position.y - camera.viewportHeight / 2f - tolerance;
        float cameraTop = camera.position.y + camera.viewportHeight / 2f + tolerance;

        if (object.getTop() + object.getHeight() < cameraBottom) {
            return false;
        } else if (object.getTop() > cameraTop) {
            return false;
        }
        return true;
    }
}
