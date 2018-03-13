package de.bitbrain.v0id.core;

import com.badlogic.gdx.math.Rectangle;

import de.bitbrain.braingdx.behavior.BehaviorAdapter;
import de.bitbrain.braingdx.world.GameObject;

public class CollisionHandler extends BehaviorAdapter {

    private final KillingMachine killingMachine;
    private final Rectangle sourceRect, targetRect;

    public CollisionHandler(KillingMachine killingMachine) {
        this.killingMachine = killingMachine;
        this.sourceRect = new Rectangle();
        this.targetRect = new Rectangle();
    }

    @Override
    public void update(GameObject source, GameObject target, float delta) {
        if (!source.hasAttribute(Attribute.KIND) || !target.hasAttribute(Attribute.KIND)) {
            return;
        }
        if (source.getAttribute(Attribute.KIND).equals(Kind.BULLET) ||
            target.getAttribute(Attribute.KIND).equals(Kind.BULLET)) {
            return;
        }
        sourceRect.set(source.getLeft(), source.getTop(), source.getWidth(), source.getHeight());
        targetRect.set(target.getLeft(), target.getTop(), target.getWidth(), target.getHeight());
        if (sourceRect.contains(targetRect) || sourceRect.overlaps(targetRect)) {
            if (source.hasAttribute(Attribute.PLAYER) && Kind.CONSUMABLE.equals(target.getAttribute(Attribute.KIND))) {
                Consumable c = (Consumable)target.getAttribute(Attribute.CONSUMABLE);
                c.consume(source);
                killingMachine.kill(target);
                return;
            }
            if (source.hasAttribute(Attribute.PLAYER)) {
                killingMachine.kill(source);
            }
            if (target.hasAttribute(Attribute.PLAYER)) {
                killingMachine.kill(target);
            }
        }
    }
}
