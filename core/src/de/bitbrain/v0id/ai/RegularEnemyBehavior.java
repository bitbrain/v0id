package de.bitbrain.v0id.ai;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class RegularEnemyBehavior extends BehaviorAdapter {

    @Override
    public void update(GameObject source, float delta) {
        super.update(source, delta);
        source.setPosition(source.getLeft(), source.getTop() - 30f * delta);
    }

    @Override
    public void update(GameObject source, GameObject target, float delta) {
        super.update(source, target, delta);
    }
}
