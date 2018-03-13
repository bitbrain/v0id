package de.bitbrain.v0id.core;

public class WeaponTemplate {
    public final BulletType type;
    public final String assetId;
    public final String soundId;
    public final String hitParticleEffectId;
    public final float frequency;
    public final float velocityX;
    public final float velocityY;
    public final float size;
    public final int[] pattern;

    public WeaponTemplate(//
            BulletType type,//
            String assetId,//
            String soundId,
            String hitParticleEffectId,
            float frequency,//
            float velocityX,//
            float velocityY,//
            float size,
            int[] pattern) {
        this.type = type;
        this.assetId = assetId;
        this.soundId = soundId;
        this.hitParticleEffectId = hitParticleEffectId;
        this.frequency = frequency;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.size = size;
        this.pattern = pattern;
    }
}
