package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.util.DeltaTimer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.core.BulletMachine;
import de.bitbrain.v0id.core.CameraController;
import de.bitbrain.v0id.graphics.Colors;
import de.bitbrain.v0id.graphics.ParallaxRenderLayer;
import de.bitbrain.v0id.graphics.PolygonRenderer;
import de.bitbrain.v0id.graphics.StarTextureFactory;

public class IngameScreen extends AbstractScreen {

    private GameObject ship;

    private final StarTextureFactory starTextureFactory = new StarTextureFactory();

    private CameraController cameraController;

    private BulletMachine bulletMachine;

    private DeltaTimer timer;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {

        timer = new DeltaTimer();

        // Setup background
        setBackgroundColor(Colors.DARK_SORROW);
        ParallaxRenderLayer backgroundLayer = new ParallaxRenderLayer(context.getGameCamera().getInternal());
        context.getRenderPipeline().set(RenderPipeIds.BACKGROUND, backgroundLayer );

        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 2,30), 0.98f).setAlpha(0.5f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 3,17), 0.85f).setAlpha(0.6f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 5,12), 0.72f).setAlpha(0.7f);

        // Setup player
        ship = context.getGameWorld().addObject();
        ship.setType("ship");
        ship.setDimensions(64, 64);
        context.getRenderManager().register("ship", new PolygonRenderer(Colors.NEON_CHRIMSON, new float[]{}));
        context.getRenderManager().register("bullet", new PolygonRenderer(Colors.NEON_LIME, new float[]{}));
        ship.setPosition(Gdx.graphics.getWidth() / 2f - ship.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - ship.getHeight() / 2f);

        // Setup Camera
        cameraController = new CameraController(context.getGameCamera());

        bulletMachine = new BulletMachine(context.getGameWorld(), context.getBehaviorManager(), context.getGameCamera().getInternal());
    }

    @Override
    protected void onUpdate(float delta) {
        timer.update(delta);
        ship.move(0f, GameConfig.BASE_SPEED * delta);
        final float speed = (500f) * delta;
        cameraController.update(delta);
        super.onUpdate(delta);
        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            ship.move(0f, speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            ship.move(-speed, 0f);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            ship.move(0f, -speed);
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            ship.move(speed, 0f);
        }

        if (timer.reached(0.25f)) {
            timer.reset();
            bulletMachine.spawn(ship.getLeft(), ship.getTop(), "bullet", new Vector2(0f, 750f));
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        starTextureFactory.dispose();
    }
}
