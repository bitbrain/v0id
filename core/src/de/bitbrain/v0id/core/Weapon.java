package de.bitbrain.v0id.core;

import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.math.Vector2;

import java.util.Random;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;

public class Weapon {

    // The distance between bullets
    private static final float WEAPON_SPREAD_RATIO = 40;

    private final BulletType type;
    private final BulletMachine machine;
    private final String soundId;
    private final String hitParticleEffectId;
    private final Vector2 velocity;
    private final float frequency;
    private final DeltaTimer timer = new DeltaTimer();
    private final Random random = new Random();
    private final Vector2 baseSpeed;
    private final int[] pattern;
    private int patternCounter = 0;

    public Weapon(BulletType type, String soundId,  String hitParticleEffectId, BulletMachine machine, float frequency, float velocityX, float velocityY, int[] pattern) {
        this.type = type;
        this.soundId = soundId;
        this.hitParticleEffectId = hitParticleEffectId;
        this.machine = machine;
        this.frequency = frequency;
        this.baseSpeed = new Vector2(GameConfig.BASE_SPEED + GameConfig.INITIAL_LEVEL_SPEED, 0f);
        this.velocity = new Vector2(velocityX, velocityY);
        this.pattern = pattern;
        baseSpeed.setAngle(velocity.angle());
        velocity.add(baseSpeed);
        timer.update(frequency);
    }

    public void shoot(GameObject source, float delta) {
        timer.update(delta);
        if (!source.isActive() && !(source.hasAttribute(Attribute.IMMUNE) && (Boolean)source.getAttribute(Attribute.IMMUNE))) {
            return;
        }
        if (timer.reached(frequency)) {
            timer.reset();
            int numberOfShots = getNumberOfShots();
            if (numberOfShots > 0) {
                SharedAssetManager.getInstance().get(soundId, Sound.class).play(0.2f + random.nextFloat() * 0.4f, 0.7f + random.nextFloat() * 0.5f, 0f);
                float width = WEAPON_SPREAD_RATIO * (numberOfShots - 1);
                float offsetX = -width / 2f;
                if (numberOfShots == 1) {
                    offsetX = 0f;
                }
                for (int i = 0; i < numberOfShots; ++i) {
                    machine.spawn(source, type, velocity.x, velocity.y, hitParticleEffectId, offsetX, 0f);
                    offsetX += WEAPON_SPREAD_RATIO;
                }
            }
        }
    }

    private int getNumberOfShots() {
        if (pattern == null || pattern.length < 1) {
            // No pattern defined, shoot 1 bullet!
            return 1;
        }
        int shots = pattern[patternCounter++];
        if (patternCounter >= pattern.length) {
            patternCounter = 0;
        }
        return shots;
    }
}
