package de.bitbrain.v0id.graphics;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;

import java.util.Map;

import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.Attribute;

public class SpriteHealthRenderer implements GameObjectRenderManager.GameObjectRenderer {

    private final Map<Integer, String> textureMapping;
    private final int minHealth, maxHealth;

    public SpriteHealthRenderer(Map<Integer, String> textureMapping) {
        this.textureMapping = textureMapping;
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
        if (health != null) {
            Texture texture = getTexture((Integer)health);
            batch.draw(texture, object.getLeft(), object.getTop(), object.getWidth(), object.getHeight());
        }
    }

    private Texture getTexture(Integer health) {
        String textureId = resolveTextureId(health);
        if (textureId != null) {
            return SharedAssetManager.getInstance().get(textureId, Texture.class);
        }
        return null;
    }

    private String resolveTextureId(Integer health) {
        String textureId = textureMapping.get(health);
        if (textureId == null) {
            if (health > maxHealth) {
                return textureMapping.get(maxHealth);
            } else if (health < minHealth) {
                return textureMapping.get(minHealth);
            }
        }
        return textureId;
    }
}
