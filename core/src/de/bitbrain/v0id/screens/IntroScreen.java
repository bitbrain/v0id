package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.g2d.Sprite;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.postprocessing.effects.Vignette;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.GameObjectTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.assets.Assets;

public class IntroScreen extends AbstractScreen {

    private Sprite logo;

    private GameContext context;

    private boolean exiting;

    public IntroScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(final GameContext context) {
        this.context = context;
        context.getScreenTransitions().in(0.5f);
        setBackgroundColor(Colors.DARK_SORROW);
        context.getRenderManager().register("logo", new SpriteRenderer(Assets.Textures.LOGO_BITBRAIN));
        GameObject logo = context.getGameWorld().addObject();
        logo.setType("logo");
        logo.setDimensions(64f, 64f);
        context.getGameCamera().setTarget(logo);

        Tween.to(logo, GameObjectTween.SCALE, 2.5f)
                .target(5f)
                .ease(TweenEquations.easeOutElastic)
                .setCallbackTriggers(TweenCallback.COMPLETE)
                .setCallback(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        if (!exiting) {
                            context.getScreenTransitions().out(new MainMenuScreen(getGame()), 1f);
                        }
                    }
                })
                .start(SharedTweenManager.getInstance());
        AudioManager.getInstance().fadeInMusic(Assets.Musics.INTRO, 3f);

        // Shaders
        Vignette v = new Vignette(Gdx.graphics.getWidth(), Gdx.graphics.getHeight(), false);
        v.setIntensity(1.2f);
        v.setSaturation(0.7f);
        v.setLutIntensity(0.9f);
        context.getRenderPipeline().getPipe(RenderPipeIds.WORLD).addEffects(v);
    }

    @Override
    protected void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
            exiting = true;
            context.getScreenTransitions().out(new MainMenuScreen(getGame()), 1f);
        }
    }
}
