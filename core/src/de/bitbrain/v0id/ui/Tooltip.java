package de.bitbrain.v0id.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import java.util.HashSet;
import java.util.Set;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquation;
import aurelienribon.tweenengine.TweenEquations;
import aurelienribon.tweenengine.TweenManager;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;

public class Tooltip {

    public interface TooltipFactory {
        Actor create();
    }

    private static final Tooltip instance = new Tooltip();

    private TweenManager tweenManager = SharedTweenManager.getInstance();

    private Stage stage;

    private Set<Actor> tooltips = new HashSet<Actor>();

    private TweenEquation equation;

    private float scale;

    private Actor lastTooltip;

    private Tooltip() {
        setTweenEquation(TweenEquations.easeInOutCubic);
        scale = 1.0f;
    }

    public static Tooltip getInstance() {
        return instance;
    }

    public void create(GameObject object, Label.LabelStyle style, String text) {
        create(object.getLeft() + object.getWidth() / 2f, object.getTop() + object.getHeight() / 2f, style, text);
    }

    public void create(float x, float y, Label.LabelStyle style, String text) {
        create(x, y, style, text, Color.WHITE, null);
    }

    public void create(final float x, final float y, final Label.LabelStyle style, final String text, final Color color, final TweenCallback callback) {
        create(callback, 0.9f, new TooltipFactory() {
            @Override
            public Actor create() {
                final Label tooltip = new Label(text, style) {
                    @Override
                    public float getX() {
                        return super.getX() - super.getWidth() / 2f;
                    }

                    @Override
                    public float getY() {
                        return super.getY() - super.getHeight() / 2f;
                    }

                    @Override
                    public float getOriginX() {
                        return super.getOriginX() + super.getWidth() / 2f;
                    }

                    @Override
                    public float getOriginY() {
                        return super.getOriginY() + super.getHeight() / 2f;
                    }
                };
                tooltip.setColor(color);
                tooltip.setPosition(x, y);
                tooltip.setWrap(false);
                return tooltip;
            }
        }, true);
    }

    public void create(final TweenCallback callback, float duration, TooltipFactory factory, boolean removable) {
        if (lastTooltip != null && removable) {
            final Actor tooltip = lastTooltip;
            tweenManager.killTarget(lastTooltip);
            Tween.to(tooltip, ActorTween.ALPHA, 0.9f).target(0f).setCallbackTriggers(TweenCallback.COMPLETE)
                    .setCallback(new TweenCallback() {
                        @Override
                        public void onEvent(int type, BaseTween<?> source) {
                            stage.getActors().removeValue(tooltip, true);
                            lastTooltip = null;
                        }
                    }).ease(equation).start(tweenManager);
        }
        final Actor tooltip = factory.create();
        stage.addActor(tooltip);
        tooltips.add(tooltip);
        Tween.to(tooltip, ActorTween.ALPHA, duration).target(0f).setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int type, BaseTween<?> source) {
                        if (callback != null) {
                            callback.onEvent(type, source);
                        }
                        lastTooltip = null;
                        stage.getActors().removeValue(tooltip, true);
                    }
                }).ease(equation).start(tweenManager);
        Tween.to(tooltip, ActorTween.SCALE, duration).target(scale).ease(equation).start(tweenManager);
        if (lastTooltip != tooltip && removable) {
            lastTooltip = tooltip;
        }
    }

    public void setTweenEquation(TweenEquation equation) {
        this.equation = equation;
    }

    public void setScale(float scale) {
        this.scale = scale;
    }

    public void clear() {
        for (Actor a : tooltips) {
            tweenManager.killTarget(a);
            stage.getActors().removeValue(a, true);
        }
        tooltips.clear();
    }

    public void init(Stage stage) {
        this.stage = stage;
    }

}