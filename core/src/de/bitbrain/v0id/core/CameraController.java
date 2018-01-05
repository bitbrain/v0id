package de.bitbrain.v0id.core;

import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.v0id.GameConfig;

public class CameraController {


    private final GameCamera camera;

    public CameraController(GameCamera gameCamera) {
        this.camera = gameCamera;
    }

    public void update(float delta) {
        camera.getInternal().position.y += (GameConfig.BASE_SPEED + GameConfig.INITIAL_LEVEL_SPEED) * delta;
    }
}
