package de.bitbrain.v0id.core;

import com.badlogic.gdx.graphics.Camera;

import de.bitbrain.braingdx.world.GameObject;

public class Respawner {

    private final Camera camera;

    public Respawner(Camera camera) {
        this.camera = camera;
    }

    public void respawn(GameObject object) {
        object.setAttribute(Attribute.HEALTH, object.getAttribute(Attribute.INITIAL_HEALTH));
        object.setAttribute(Attribute.DEAD, false);
        object.setActive(true);
        object.getScale().set(1f, 1f);
        object.setColor(1f, 1f, 1f,1f);
        object.setPosition(camera.position.x - object.getWidth() / 2f, camera.position.y - object.getHeight() / 2f);
    }
}
