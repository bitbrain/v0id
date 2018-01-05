package de.bitbrain.v0id.core;

import com.badlogic.gdx.graphics.Camera;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.world.GameObject;

public class GameOverLogic extends BehaviorAdapter {

    private final Camera camera;

    public GameOverLogic(Camera camera) {
        this.camera = camera;
    }

    @Override
    public void update(GameObject source, float delta) {
        float cameraLeft = camera.position.x - camera.viewportWidth / 2f;
        float cameraRight = camera.position.x + camera.viewportWidth / 2f;
        float cameraBottom = camera.position.y - camera.viewportHeight / 2f;
        if (cameraLeft > source.getLeft() + source.getWidth()) {
            respawn(source);
        } else if (cameraRight < source.getLeft()) {
            respawn(source);
        } else if (cameraBottom > source.getTop() + source.getHeight()) {
            respawn(source);
        }
    }

    private void respawn(GameObject object) {
        object.setPosition(camera.position.x - object.getWidth() / 2f, camera.position.y - object.getHeight() / 2f);
    }
}
