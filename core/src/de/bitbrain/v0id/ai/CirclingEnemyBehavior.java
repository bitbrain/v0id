package de.bitbrain.v0id.ai;

import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.movement.ObjectMover;

public class CirclingEnemyBehavior extends BehaviorAdapter {

    private final ObjectMover mover;

    private final Vector2 rotation = new Vector2(1f, 0f);

    private CirclingEnemyBehavior(ObjectMover mover) {
        this.mover = mover;
    }

    public static BehaviorFactory factory() {
        return new BehaviorFactory() {
            @Override
            public Behavior create() {
                return new CirclingEnemyBehavior(mover);
            }
        };
    }

    @Override
    public void update(GameObject source, float delta) {
        rotation.rotate(30.5f * delta);
        mover.move(source, rotation.x, rotation.y);
    }
}
