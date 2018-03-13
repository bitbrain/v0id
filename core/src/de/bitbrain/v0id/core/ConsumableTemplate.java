package de.bitbrain.v0id.core;

public class ConsumableTemplate {

    public final GameObjectType type;
    public final String particleEffectId;
    public final String assetId;
    public final Consumable consumable;
    public final float likelihood;

    public ConsumableTemplate(
            GameObjectType type,
            String assetId,
            String particleEffectId,
            Consumable consumable,
            float likelihood) {
        this.type = type;
        this.assetId = assetId;
        this.particleEffectId = particleEffectId;
        this.consumable = consumable;
        this.likelihood = likelihood;
    }
}
