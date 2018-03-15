package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;

import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenEquations;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
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
import de.bitbrain.v0id.ui.Styles;

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
        context.getScreenTransitions().in(3.5f);
        setBackgroundColor(Colors.DARK_SORROW);
        context.getRenderManager().register("logo", new SpriteRenderer(Assets.Textures.LOGO_VOID));
        logo = context.getGameWorld().addObject();
        logo.setType("logo");
        logo.setDimensions(700, 300f);
        logo.getScale().set(0.7f, 0.7f);
        logo.getColor().a = 0.8f;
        context.getGameCamera().setTarget(logo);
        Tween.to(logo, GameObjectTween.ALPHA, 3.0f)
                .target(1f)
                .ease(TweenEquations.easeInOutCubic)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(SharedTweenManager.getInstance());
        Tween.to(logo, GameObjectTween.SCALE, 3.0f)
                .target(0.8f)
                .ease(TweenEquations.easeInOutCubic)
                .repeatYoyo(Tween.INFINITY, 0)
                .start(SharedTweenManager.getInstance());

        // Credits
        Label credits = new Label("game by bitbrain   music by k0stnix", Styles.LABEL_CREDITS);
        credits.getColor().a = 0.7f;
        credits.setPosition(Gdx.graphics.getWidth() / 2f - credits.getWidth() / 2f, 65f);
        context.getStage().addActor(credits);

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
            SharedAssetManager.getInstance().get(Assets.Sounds.ZOOM, Sound.class).play();
            AudioManager.getInstance().fadeOutMusic(Assets.Musics.INTRO, 1f);
        }
    }
}
