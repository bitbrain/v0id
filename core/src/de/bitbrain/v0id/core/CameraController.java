package de.bitbrain.v0id.core;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.graphics.GameCamera;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.graphics.ScreenShake;

public class CameraController {


    private final GameCamera camera;

    private Vector2 internalPosition = new Vector2();

    public CameraController(GameCamera gameCamera) {
        this.camera = gameCamera;
    }

    public void update(float delta) {
        internalPosition.y += (GameConfig.BASE_SPEED + GameConfig.INITIAL_LEVEL_SPEED) * delta;
        camera.getInternal().position.x = internalPosition.x + ScreenShake.getShake().x;
        camera.getInternal().position.y = internalPosition.y + ScreenShake.getShake().y;
    }
}
