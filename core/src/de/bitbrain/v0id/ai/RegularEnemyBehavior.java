package de.bitbrain.v0id.ai;

import com.badlogic.gdx.Gdx;

import java.util.Random;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class RegularEnemyBehavior extends BehaviorAdapter {

    private Random random = new Random();

    private float velocity = 0f;

    @Override
    public void update(GameObject source, float delta) {
        super.update(source, delta);
        final float factor = 5f;
        velocity += source.getLeft() < Gdx.graphics.getWidth() / 2f ? factor * random.nextFloat() : -factor * random.nextFloat();
        source.setPosition(source.getLeft() + 5f * velocity * delta, source.getTop() - 50f * delta);
    }

    @Override
    public void update(GameObject source, GameObject target, float delta) {
        super.update(source, target, delta);
    }
}
