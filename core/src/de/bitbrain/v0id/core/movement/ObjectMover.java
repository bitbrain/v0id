package de.bitbrain.v0id.core.movement;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.core.Attribute;

public class ObjectMover extends BehaviorAdapter {

    public void move(GameObject source, float directionX, float directionY) {
        if (!source.hasAttribute(Attribute.MOVEMENT_DATA)) {
            return;
        }
        MovementData data = (MovementData)source.getAttribute(Attribute.MOVEMENT_DATA);
        data.triggered = true;
        data.direction.set(directionX, directionY);
        data.direction.setLength(1f);
    }

    public void move(GameObject source) {
        if (!source.hasAttribute(Attribute.MOVEMENT_DATA)) {
            return;
        }
        MovementData data = (MovementData)source.getAttribute(Attribute.MOVEMENT_DATA);
        data.triggered = true;
    }


    @Override
    public void update(GameObject source, float delta) {
        if (!source.hasAttribute(Attribute.MOVEMENT_DATA)) {
            return;
        }
        MovementData data = (MovementData)source.getAttribute(Attribute.MOVEMENT_DATA);

        // Calculate velocity
        if (data.triggered) {
            float deltaX = data.direction.x * data.accellerationFactor;
            float deltaY = data.direction.y * data.accellerationFactor;
            data.velocity.add(deltaX, deltaY);
        } else {
            data.velocity.scl(55.0f * delta);
        }

        // Normalize velocity bounds
        if (data.velocity.len() > data.maxVelocity) {
            data.velocity.setLength(data.maxVelocity);
        } else if (data.velocity.len() < data.minVelocity) {
            data.velocity.setLength(data.minVelocity);
        }

        // Apply velocity
        source.setPosition(source.getLeft() + data.velocity.x * delta, source.getTop() + (GameConfig.BASE_SPEED + data.velocity.y) * delta);
        // We have moved! Disable movement
        data.triggered = false;
    }
}
