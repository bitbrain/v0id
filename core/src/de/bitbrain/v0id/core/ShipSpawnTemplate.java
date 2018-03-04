package de.bitbrain.v0id.core;

public class ShipSpawnTemplate {

    public final String[] assetIds;
    public final Object type;
    public final int life;
    public final float likelihood;
    public final float interval;
    public final WeaponTemplate weapon;
    public final float minVelocity;
    public final float maxVelocity;
    public final float accellerationFactor;
    public final int points;

    public ShipSpawnTemplate(
            String[] assetIds,//
            Object type,//
            int life,//
            float likelihood,//
            float interval,//
            WeaponTemplate weapon,
            float minVelocity,
            float maxVelocity,
            float accellerationFactor,
            int points) {
        this.assetIds = assetIds;
        this.type = type;
        this.likelihood = likelihood;
        this.life = life;
        this.interval = interval;
        this.weapon = weapon;
        this.minVelocity = minVelocity;
        this.maxVelocity = maxVelocity;
        this.accellerationFactor = accellerationFactor;
        this.points = points;
    }
}
