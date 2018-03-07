package de.bitbrain.v0id.screens;


import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Table;

import aurelienribon.tweenengine.Tween;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.ActorTween;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.PlayerStats;
import de.bitbrain.v0id.ui.PlayerScoreLabel;
import de.bitbrain.v0id.ui.Styles;

public class GameOverScreen extends AbstractScreen {

    private final PlayerStats stats;

    private boolean exiting;

    private GameContext context;

    public GameOverScreen(BrainGdxGame game, PlayerStats stats) {
        super(game);
        this.stats = stats;
    }

    @Override
    protected void onCreate(GameContext context) {
        this.context = context;
        context.getScreenTransitions().in(0.5f);
        setBackgroundColor(Colors.DARK_SORROW);

        Table table = new Table();
        table.setFillParent(true);
        PlayerScoreLabel score = new PlayerScoreLabel(stats, Styles.LABEL_CAPTION, 2f);
        table.center().add(score).padBottom(150f).row();
        Label lblContinue = new Label("Press any key", Styles.LABEL_TEXT_MEDIUM);
        table.center().add(lblContinue);
        context.getStage().addActor(table);
        lblContinue.getColor().a = 0.6f;
        Tween.to(lblContinue, ActorTween.ALPHA, 0.6f)
                .target(1f)
                .repeatYoyo(Tween.INFINITY, 0f)
                .start(SharedTweenManager.getInstance());
    }

    @Override
    protected void onUpdate(float delta) {
        super.onUpdate(delta);
        if (!exiting && (Gdx.input.isTouched() || Gdx.input.isKeyJustPressed(Input.Keys.ANY_KEY))) {
            exiting = true;
            context.getScreenTransitions().out(new MainMenuScreen(getGame()), 1f);
            SharedAssetManager.getInstance().get(Assets.Sounds.EXIT, Sound.class).play();
        }
    }
}
