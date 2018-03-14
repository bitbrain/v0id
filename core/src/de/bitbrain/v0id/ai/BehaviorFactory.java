package de.bitbrain.v0id.ai;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.behavior.Behavior;
import de.bitbrain.v0id.core.movement.ObjectMover;

public abstract class BehaviorFactory {

    protected GameContext context;
    protected ObjectMover mover;

    public abstract Behavior create();

    public void init(GameContext context, ObjectMover mover) {
        this.context = context;
        this.mover = mover;
    }
}
