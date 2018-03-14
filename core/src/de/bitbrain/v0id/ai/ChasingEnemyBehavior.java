package de.bitbrain.v0id.ai;


import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.movement.ObjectMover;

public class ChasingEnemyBehavior extends BehaviorAdapter {

    private final ObjectMover mover;

    private GameObject player;

    private Vector2 direction = new Vector2();

    private ChasingEnemyBehavior(ObjectMover mover) {
        this.mover = mover;
    }

    public static BehaviorFactory factory() {
        return new BehaviorFactory() {
            @Override
            public Behavior create() {
                return new ChasingEnemyBehavior(mover);
            }
        };
    }

    @Override
    public void update(GameObject source, float delta) {
        if (player != null && source.getTop() > player.getTop()) {
            direction.x = player.getLeft() - source.getLeft();
            direction.y = player.getTop() - source.getTop();
            direction.nor();
            mover.move(source, direction.x, direction.y);
        } else {
            mover.move(source, 0f, -1f);
        }
    }

    @Override
    public void update(GameObject source, GameObject target, float delta) {
        if (target.hasAttribute(Attribute.PLAYER)) {
            player = target;
        }
    }
}
