package de.bitbrain.v0id.graphics;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.utils.Disposable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenAccessor;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.Attribute;

public class SpriteHealthRenderer implements GameObjectRenderManager.GameObjectRenderer, Disposable {

    private class BooleanProvider {

        private boolean value = false;

        public void setValue(boolean value) {
            this.value = value;
        }
        public boolean getValue() {
            return value;
        }
    }

    private static class BooleanProviderTween implements TweenAccessor<BooleanProvider> {

        @Override
        public int getValues(BooleanProvider booleanProvider, int i, float[] floats) {
            if (booleanProvider.getValue()) {
                floats[0] = 1;
                return 1;
            } else {
                floats[0] = 0;
                return 1;
            }
        }

        @Override
        public void setValues(BooleanProvider booleanProvider, int i, float[] floats) {
            booleanProvider.setValue(floats[0] > 0);
        }
    }

    static {
        Tween.registerAccessor(BooleanProvider.class, new BooleanProviderTween());
    }

    private static final float FLICKER_DURATION = 0.05f;

    private final Map<Integer, String> textureMapping;
    private final Map<String, Texture> damageTextureMapping;
    private final int minHealth, maxHealth;
    private int previousHealth = -1;
    private final Map<String, BooleanProvider> booleanProviders = new HashMap<String, BooleanProvider>();

    public SpriteHealthRenderer(Map<Integer, String> textureMapping) {
        this.textureMapping = textureMapping;
        damageTextureMapping = buildDamageTextures(textureMapping.values());
        int tmpMin = 0;
        int tmpMax = 0;
        for (Integer value : textureMapping.keySet()) {
            if (value > tmpMax) {
                tmpMax = value;
            }
            if (value < tmpMin) {
                tmpMin = value;
            }
        }
        this.minHealth = tmpMin;
        this.maxHealth = tmpMax;
    }

    @Override
    public void init() {

    }

    @Override
    public void render(GameObject object, Batch batch, float delta) {
        Object health = object.getAttribute(Attribute.HEALTH);
        if (health != null && health instanceof Integer) {
            if ((Integer)health < previousHealth && previousHealth > 0f && (Integer)health > 0) {
                triggerDamageAnimation(object);
            }
            Texture texture = getTexture(object, (Integer)health);
            if (texture != null) {
                Sprite sprite = new Sprite(texture);
                sprite.setBounds(object.getLeft(), object.getTop(), object.getWidth(), object.getHeight());
                sprite.setScale(object.getScale().x);
                sprite.draw(batch);
            }
            previousHealth = (Integer)health;
        }
    }

    @Override
    public void dispose() {
        for (Texture texture : damageTextureMapping.values()) {
            texture.dispose();
        }
        damageTextureMapping.clear();
    }

    private void triggerDamageAnimation(final GameObject object) {
        BooleanProvider provider = booleanProviders.get(object.getId());
        if (provider != null) {
            SharedTweenManager.getInstance().killTarget(provider);
            booleanProviders.remove(object.getId());
        }
        provider = new BooleanProvider();
        booleanProviders.put(object.getId(), provider);
        provider.setValue(true);
        Tween.to(provider, 0, FLICKER_DURATION)
                .target(1f, 0f)
                .ease(TweenEquations.easeNone)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        booleanProviders.remove(object.getId());
                    }
                })
                .start(SharedTweenManager.getInstance());
    }

    private Texture getTexture(GameObject object, Integer health) {
        String textureId = resolveTextureId(health);
        if (textureId != null) {
            BooleanProvider booleanProvider = booleanProviders.get(object.getId());
            if (booleanProvider != null && booleanProvider.getValue() && health > 0) {
                return damageTextureMapping.get(textureId);
            }
            return SharedAssetManager.getInstance().get(textureId, Texture.class);
        }
        return null;
    }

    private String resolveTextureId(Integer health) {
        String textureId = textureMapping.get(health);
        if (textureId == null) {
            if (health > maxHealth) {
                return textureMapping.get(maxHealth);
            } else if (health <= minHealth) {
                return textureMapping.get(minHealth);
            }
        }
        return textureId;
    }

    private Map<String, Texture> buildDamageTextures(Collection<String> textureIds) {
        Map<String, Texture> damageTextures = new HashMap<String, Texture>();
        for (String textureId : textureIds) {
            Texture texture = SharedAssetManager.getInstance().get(textureId, Texture.class);
            Pixmap target = new Pixmap(texture.getWidth(), texture.getHeight(), Pixmap.Format.RGBA8888);
            target.setColor(Color.WHITE);
            texture.getTextureData().prepare();
            Pixmap source = texture.getTextureData().consumePixmap();
            for (int x = 0; x < source.getWidth(); ++x) {
                for (int y = 0; y < source.getHeight(); ++y) {
                    if (source.getPixel(x, y) != 0) {
                        target.drawPixel(x, y);
                    }
                }
            }
            damageTextures.put(textureId, new Texture(target));
            target.dispose();
        }
        return damageTextures;
    }
}
