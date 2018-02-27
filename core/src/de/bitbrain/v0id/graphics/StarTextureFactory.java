package de.bitbrain.v0id.graphics;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.glutils.FrameBuffer;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.utils.Disposable;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class StarTextureFactory implements Disposable {

    private static final String STAR_SEED = "r3782387r2837r2973r-02r-298r-28r";
    private static final Color STAR_COLOR = Colors.LIGHT_SORROW;

    private final Random random = new Random(STAR_SEED.hashCode());
    private final List<Texture> textures = new ArrayList<Texture>();

    public Texture create(int textureSize, int starSize, int numberOfStars) {
        Pixmap pixmap = new Pixmap(textureSize, textureSize, Pixmap.Format.RGBA8888);
        pixmap.setColor(STAR_COLOR);
        for (int i = 0; i < numberOfStars; ++i) {
            int randomX = (int) (random.nextFloat() * textureSize);
            int randomY = (int) (random.nextFloat() * textureSize);
            pixmap.fillRectangle(randomX, randomY, starSize, starSize);
        }
        Texture texture = new Texture(pixmap);
        pixmap.dispose();
        textures.add(texture);
        return texture;
    }

    @Override
    public void dispose() {
        for (Texture texture : textures) {
            texture.dispose();
        }
        textures.clear();
    }
}
