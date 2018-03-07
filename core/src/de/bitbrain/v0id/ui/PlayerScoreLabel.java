package de.bitbrain.v0id.ui;

import com.badlogic.gdx.audio.Sound;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.tweens.ValueTween;
import de.bitbrain.braingdx.util.ValueProvider;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.PlayerStats;

public class PlayerScoreLabel extends HeightedLabel {

    private final PlayerStats stats;

    private final ValueProvider valueProvider = new ValueProvider();

    private int lastPoints;

    private final float duration;

    static {
        Tween.registerAccessor(ValueProvider.class, new ValueTween());
    }

    public PlayerScoreLabel(PlayerStats stats) {
        this(stats, Styles.LABEL_TEXT_POINTS, 0.5f);
    }

    public PlayerScoreLabel(PlayerStats stats, HeightedLabelStyle style, float duration) {
        super("", style);
        this.stats = stats;
        this.duration = duration;
    }

    @Override
    public void act(float delta) {
        if (lastPoints != stats.getPoints()) {
            lastPoints = stats.getPoints();
            SharedTweenManager.getInstance().killTarget(valueProvider);
            Tween.to(valueProvider, ValueTween.VALUE, duration)
                    .target(lastPoints)
                    .setCallbackTriggers(TweenCallback.ANY)
                    .ease(TweenEquations.easeInOutCubic)
                    .start(SharedTweenManager.getInstance());
        }
        setText(String.valueOf((int)Math.ceil(valueProvider.getValue())));
        super.act(delta);
        if (SharedTweenManager.getInstance().containsTarget(valueProvider)) {
            SharedAssetManager.getInstance().get(Assets.Sounds.POINT, Sound.class).play(0.1f);
        }
    }
}
