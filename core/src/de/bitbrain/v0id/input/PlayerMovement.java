package de.bitbrain.v0id.input;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.KillingMachine;
import de.bitbrain.v0id.core.movement.ObjectMover;

public class PlayerMovement {

    private final GameObject player;

    private final ObjectMover mover;

    private final KillingMachine killingMachine;

    private final Camera camera;

    private final Vector2 direction = new Vector2();

    public PlayerMovement(GameObject player, ObjectMover mover, KillingMachine killingMachine, Camera camera) {
        this.player = player;
        this.mover = mover;
        this.killingMachine = killingMachine;
        this.camera = camera;
        direction.set(0f, 1f);
    }
    public void update(float delta) {
        boolean pressed = false;
        direction.set(0f, 0f);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            direction.y = 1f;
            pressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            direction.y = -1f;
            pressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            direction.x = -1f;
            pressed = true;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            direction.x = 1f;
            pressed = true;
        }
        if (pressed) {
            mover.move(player, direction.x, direction.y);
        }
        // Kill off the player if he reaches the ground...
        if (player.getTop() - player.getHeight() < camera.position.y - camera.viewportHeight / 2f) {
            killingMachine.kill(player);
        }
        if (player.getLeft() < camera.position.x - camera.viewportWidth / 2f) {
            player.setPosition(camera.position.x - camera.viewportWidth / 2f, player.getTop());
        }
        if (player.getRight() > camera.position.x + camera.viewportWidth / 2f) {
            player.setPosition(camera.position.x + camera.viewportWidth / 2f - player.getWidth(), player.getTop());
        }
        if (player.getTop() > camera.position.y + camera.viewportHeight / 4f) {
            player.setPosition(player.getLeft(), camera.position.y + camera.viewportHeight / 4f);
        }
    }
}
