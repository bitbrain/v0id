package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.assets.Assets;

public class MainMenuScreen extends AbstractScreen {

    private GameContext context;

    private boolean exiting;
    private GameObject logo;

    public MainMenuScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        this.context = context;
        context.getScreenTransitions().in(0.5f);
        setBackgroundColor(Colors.DARK_SORROW);
        context.getRenderManager().register("logo", new SpriteRenderer(Assets.Textures.LOGO_VOID));
        logo = context.getGameWorld().addObject();
        logo.setType("logo");
        logo.setDimensions(700, 300f);
        logo.getScale().set(0.7f, 0.7f);
        logo.getColor().a = 0.8f;
        context.getGameCamera().setTarget(logo);
        Tween.to(logo, GameObjectTween.ALPHA, 1.0f)
                .target(1f)
                .ease(TweenEquations.easeInOutCubic)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(SharedTweenManager.getInstance());
        Tween.to(logo, GameObjectTween.SCALE, 1.0f)
                .target(0.8f)
                .ease(TweenEquations.easeInOutCubic)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(SharedTweenManager.getInstance());
    }

    @Override
    protected void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
            exiting = true;
            context.getScreenTransitions().out(new IngameScreen(getGame()), 1f);
            SharedTweenManager.getInstance().killTarget(logo);
            Tween.to(logo, GameObjectTween.SCALE, 1.0f)
                    .target(6f)
                    .ease(TweenEquations.easeOutCubic)
                    .start(SharedTweenManager.getInstance());
            Tween.to(logo, GameObjectTween.ALPHA, 1.0f)
                    .target(0f)
                    .ease(TweenEquations.easeOutCubic)
                    .start(SharedTweenManager.getInstance());
        }
    }
}
