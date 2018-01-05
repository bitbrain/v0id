package de.bitbrain.v0id.core;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

public class Weapon {

    private final BulletType type;
    private final BulletMachine machine;
    private final Vector2 velocity;
    private final float frequency;
    private final DeltaTimer timer = new DeltaTimer();

    public Weapon(BulletType type, BulletMachine machine, float frequency, float velocityX, float velocityY) {
        this.type = type;
        this.machine = machine;
        this.frequency = frequency;
        this.velocity = new Vector2(velocityX, velocityY);
        timer.update(frequency);
    }

    public void shoot(GameObject source, float delta) {
        timer.update(delta);
        if (timer.reached(frequency)) {
            timer.reset();
            machine.spawn(source, type, velocity.x, velocity.y);
        }
    }
}
