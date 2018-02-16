package de.bitbrain.v0id.core.movement;


import com.badlogic.gdx.math.Vector2;

public class MovementData {

    public final Vector2 velocity;
    public final Vector2 direction;
    public boolean triggered = false;

    public final float accellerationFactor;

    public final float minVelocity;

    public final float maxVelocity;

    public MovementData(float accellerationFactor,float minVelocity, float maxVelocity) {
        this.accellerationFactor = accellerationFactor;
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.velocity = new Vector2();
        this.direction = new Vector2(0f, 0f);
    }
}
