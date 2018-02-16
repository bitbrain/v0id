package de.bitbrain.v0id.core;

public class WeaponTemplate {
    public final BulletType type;
    public final String assetId;
    public final float frequency;
    public final float velocityX;
    public final float velocityY;
    public final float size;

    public WeaponTemplate(//
            BulletType type,//
            String assetId,//
            float frequency,//
            float velocityX,//
            float velocityY,//
            float size) {
        this.type = type;
        this.assetId = assetId;
        this.frequency = frequency;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
        this.size = size;
    }
}
