package de.bitbrain.v0id.graphics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.behavior.BehaviorManager;
import de.bitbrain.braingdx.world.GameObject;

public class ParticleManager {

    private static class InternalPooledEffect {
        public final ParticleEffectPool.PooledEffect effect;
        public final String assetId;

        public InternalPooledEffect(String assetId, ParticleEffectPool.PooledEffect effect) {
            this.effect = effect;
            this.assetId = assetId;
        }
    }

    private final Set<InternalPooledEffect> effects = new HashSet<InternalPooledEffect>();
    private final Set<InternalPooledEffect> removals = new HashSet<InternalPooledEffect>();
    private final Map<String, ParticleEffectPool> pools = new HashMap<String, ParticleEffectPool>();
    private final BehaviorManager behaviorManager;

    public ParticleManager(BehaviorManager behaviorManager) {
        this.behaviorManager = behaviorManager;
    }

    public void draw(Batch batch, float delta) {
        for (InternalPooledEffect internal : effects) {
            if (internal.effect.isComplete()) {
                removals.add(internal);
            }
            internal.effect.draw(batch, delta);
        }
        for (InternalPooledEffect internal : removals) {
            freeEffect(internal);
        }
        removals.clear();
    }

    public void attachEffect(String assetEffectId, GameObject object, final float offsetX, final float offsetY) {
        final InternalPooledEffect effect = ensureEffect(assetEffectId);
        behaviorManager.apply(new BehaviorAdapter() {
            @Override
            public void onDetach(GameObject source) {
                freeEffect(effect);
            }

            @Override
            public void update(GameObject source, float delta) {
                if (effect.effect.isComplete() && effects.contains(effect)) {
                    freeEffect(effect);
                } else {
                    effect.effect.setPosition(source.getLeft() + offsetX, source.getTop() + offsetY);
                }
            }
        }, object);
    }

    public void spawnEffect(String assetEffectId, float worldX, float worldY) {
        InternalPooledEffect internal = ensureEffect(assetEffectId);
        internal.effect.setPosition(worldX, worldY);
    }

    private InternalPooledEffect ensureEffect(String particleId) {
        ParticleEffectPool pool = pools.get(particleId);
        if (pool == null) {
            ParticleEffect effect = SharedAssetManager.getInstance().get(particleId, ParticleEffect.class);
            pool = new ParticleEffectPool(effect, 50, 500);
            pools.put(particleId, pool);
        }
        InternalPooledEffect effect = new InternalPooledEffect(particleId, pool.obtain());
        effects.add(effect);
        effect.effect.reset();
        effect.effect.start();
        return effect;
    }

    private void freeEffect(InternalPooledEffect effect) {
        ParticleEffectPool pool = pools.get(effect.assetId);
        if (pool != null) {
            effects.remove(effect);
            pool.free(effect.effect);
        } else {
            Gdx.app.error("Particles", "Unable to release effect " + effect.assetId + ". No pool available!");
        }
    }
}
