package de.bitbrain.v0id.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.scenes.scene2d.Actor;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.tweens.ColorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.PlayerStats;

public class HealthBar extends Actor {

    private final PlayerStats stats;

    private final Color currentCellColor = new Color();

    private Color lastColor = new Color();

    private int lastLife;

    public HealthBar(PlayerStats stats) {
        this.stats = stats;
        lastLife = stats.getLifeCount();
    }

    @Override
    public void draw(Batch batch, float parentAlpha) {
        super.draw(batch, parentAlpha);
        Texture fg = SharedAssetManager.getInstance().get(Assets.Textures.UI_HEALTH, Texture.class);
        Texture bg = SharedAssetManager.getInstance().get(Assets.Textures.UI_HEALTH_BACKGROUND, Texture.class);
        float offset = fg.getHeight() / 1.6f;
        for (int i = 0; i < stats.getTotalLifeCount(); ++i) {
            batch.setColor(Color.GRAY);
            batch.draw(bg, getX(), getY() + i * offset - 7f);
            batch.setColor(Color.WHITE);
            batch.draw(bg, getX(), getY() + i * offset);
            if (i == stats.getLifeCount() - 1) {
                batch.setColor(recalculate());
            } else if (i < stats.getLifeCount()){
                batch.setColor(Colors.NEON_LIME);
            } else if (i < stats.getLifeCount() + 1) {
                if (lastLife != stats.getLifeCount()) {
                    lastLife = stats.getLifeCount();
                    animateLifeLossColor();
                }
                batch.setColor(lastColor);
            } else {
                batch.setColor(Colors.DARK_CHRIMSON);
            }
            batch.draw(fg, getX(), getY() + i * offset);
        }
        batch.setColor(Color.WHITE);
    }

    private void animateLifeLossColor() {
        final float duration = 3f;
        SharedTweenManager.getInstance().killTarget(lastColor);
        lastColor = new Color(Colors.NEON_CHRIMSON);
        Tween.to(lastColor, ColorTween.R, duration)
                .delay(1f)
                .target(Colors.DARK_CHRIMSON.r)
                .ease(TweenEquations.easeOutCubic)
                .start(SharedTweenManager.getInstance());
        Tween.to(lastColor, ColorTween.G, duration)
                .delay(1f)
                .target(Colors.DARK_CHRIMSON.g)
                .ease(TweenEquations.easeOutCubic)
                .start(SharedTweenManager.getInstance());
        Tween.to(lastColor, ColorTween.B, duration)
                .delay(1f)
                .target(Colors.DARK_CHRIMSON.b)
                .ease(TweenEquations.easeOutCubic)
                .start(SharedTweenManager.getInstance());
    }

    private Color recalculate() {
        float percentage = (float)stats.getCurrentHealth() / (float)stats.getTotalHealth();
        if (percentage < 0) {
            percentage = 0;
        }
        if (percentage > 1f) {
            percentage = 1f;
        }
        if (percentage > 0.5f) {
            return lerp(currentCellColor, Colors.NEON_FIRE, Colors.NEON_LIME,  percentage * 2f - 1f);
        } else {
            return lerp(currentCellColor, Colors.NEON_CHRIMSON, Colors.NEON_FIRE, percentage * 2f);
        }
    }

    private static Color lerp(Color base, Color a, Color b, float t) {
        base.r = a.r + (b.r - a.r) * t;
        base.g = a.g + (b.g - a.g) * t;
        base.b = a.b + (b.b - a.b) * t;
        base.a = a.a + (b.a - a.a) * t;
        return base;
    }
}
