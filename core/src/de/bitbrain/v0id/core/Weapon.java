package de.bitbrain.v0id.core;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;

public class Weapon {

    private final BulletType type;
    private final BulletMachine machine;
    private final String soundId;
    private final String hitParticleEffectId;
    private final Vector2 velocity;
    private final float frequency;
    private final DeltaTimer timer = new DeltaTimer();
    private final Random random = new Random();

    public Weapon(BulletType type, String soundId,  String hitParticleEffectId, BulletMachine machine, float frequency, float velocityX, float velocityY) {
        this.type = type;
        this.soundId = soundId;
        this.hitParticleEffectId = hitParticleEffectId;
        this.machine = machine;
        this.frequency = frequency;
        this.velocity = new Vector2(velocityX, velocityY);
        timer.update(frequency);
    }

    public void shoot(GameObject source, float delta) {
        timer.update(delta);
        if (!source.isActive()) {
            return;
        }
        if (timer.reached(frequency)) {
            timer.reset();
            SharedAssetManager.getInstance().get(soundId, Sound.class).play(0.4f + random.nextFloat() * 0.5f, 0.7f + random.nextFloat() * 0.5f, 0f);
            machine.spawn(source, type, velocity.x, velocity.y, hitParticleEffectId);
        }
    }
}
