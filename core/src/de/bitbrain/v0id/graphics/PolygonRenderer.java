package de.bitbrain.v0id.graphics;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import de.bitbrain.braingdx.graphics.GameObjectRenderManager;
import de.bitbrain.braingdx.world.GameObject;

public class PolygonRenderer implements GameObjectRenderManager.GameObjectRenderer, Disposable {

    private final ShapeRenderer shapeRenderer = new ShapeRenderer();

    private final Color color;

    private final float[] data;

    private Texture texture;

    public PolygonRenderer(Color color, float[] data) {
        this.color = color;
        this.data = data;
    }

    @Override
    public void init() {

    }

    @Override
    public void render(GameObject object, Batch batch, float delta) {
        if (hasTextureChanged(object)) {
            bakeTexture(object);
        }
        batch.draw(texture, object.getLeft(), object.getTop());
    }

    private boolean hasTextureChanged(GameObject object) {
        return texture == null || texture.getWidth() != (int)object.getWidth() || texture.getHeight() != (int)object.getHeight();
    }

    private void bakeTexture(GameObject object) {
        dispose();
        Gdx.app.debug("INFO", "PolygonRenderer - Baking new texture...");
        Pixmap pixmap = new Pixmap((int)object.getWidth(), (int)object.getHeight(), Pixmap.Format.RGBA8888);
        pixmap.setColor(color);
        pixmap.fillRectangle(0, 0, (int)object.getWidth(), (int)object.getHeight());
        texture = new Texture(pixmap);
        pixmap.dispose();
        Gdx.app.debug("INFO", "PolygonRenderer - Successfully baked texture @target=" + texture.glTarget);
    }

    @Override
    public void dispose() {
        if (texture != null) {
            Gdx.app.debug("DISPOSE", "PolygonRenderer - Disposing texture " + texture);
            texture.dispose();
        }
    }
}
