package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.BulletMachine;
import de.bitbrain.v0id.core.CameraController;
import de.bitbrain.v0id.core.CollisionHandler;
import de.bitbrain.v0id.core.GameObjectFactory;
import de.bitbrain.v0id.core.KillingMachine;
import de.bitbrain.v0id.core.PlayerStats;
import de.bitbrain.v0id.core.Respawner;
import de.bitbrain.v0id.core.TemplateService;
import de.bitbrain.v0id.core.WeaponFactory;
import de.bitbrain.v0id.core.movement.ObjectMover;
import de.bitbrain.v0id.graphics.ParallaxRenderLayer;
import de.bitbrain.v0id.graphics.ParticleManager;
import de.bitbrain.v0id.graphics.StarTextureFactory;
import de.bitbrain.v0id.input.PlayerMovement;
import de.bitbrain.v0id.levelgen.LevelBounds;
import de.bitbrain.v0id.levelgen.WorldGenerator;
import de.bitbrain.v0id.ui.PlayerScoreLabel;
import de.bitbrain.v0id.ui.Tooltip;

public class IngameScreen extends AbstractScreen {

    private Camera camera;

    private final StarTextureFactory starTextureFactory = new StarTextureFactory();

    private CameraController cameraController;

    private BulletMachine bulletMachine;

    private Respawner respawner;

    private PlayerMovement movement;

    private WorldGenerator worldGenerator;

    private ParticleManager particleManager;

    private GameContext context;

    private boolean resetCameraY = false;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        this.context = context;
        camera = context.getGameCamera().getInternal();
        camera.position.x = 0;
        camera.position.y = 0;
        respawner = new Respawner(camera);

        particleManager = new ParticleManager(context.getBehaviorManager());

        context.getScreenTransitions().in(1f);

        // Define game world
        context.getGameWorld().setBounds(new LevelBounds());

        // Setup background
        setBackgroundColor(Colors.DARK_SORROW);
        ParallaxRenderLayer backgroundLayer = new ParallaxRenderLayer(context.getGameCamera().getInternal());
        context.getRenderPipeline().set(RenderPipeIds.BACKGROUND, backgroundLayer );

        context.getRenderPipeline().set(RenderPipeIds.LIGHTING, new AbstractRenderLayer() {

            @Override
            public void render(Batch batch, float delta) {
                batch.begin();
                particleManager.draw(batch, delta);
                batch.end();
            }
        });

        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 3,30), 0.98f).setAlpha(0.5f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 4,17), 0.85f).setAlpha(0.55f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 5,12), 0.72f).setAlpha(0.6f);

        // Setup Camera
        cameraController = new CameraController(context.getGameCamera());

        // Setup weapon systems
        KillingMachine killingMachine = new KillingMachine(context.getGameWorld(), respawner, particleManager);
        bulletMachine = new BulletMachine(context.getGameWorld(), context.getBehaviorManager(), killingMachine, particleManager);
        WeaponFactory weaponFactory = new WeaponFactory(bulletMachine, context.getBehaviorManager());

        ObjectMover mover = new ObjectMover();
        context.getBehaviorManager().apply(mover);

        // Setup player
        GameObjectFactory factory = new GameObjectFactory(context, weaponFactory);
        GameObject player = factory.spawnShip(TemplateService.shipTemplates[1], 0f, 0f, false);
        player.setAttribute(Attribute.PLAYER, true);
        movement = new PlayerMovement(player, mover, killingMachine, context.getGameCamera().getInternal());
        respawner.respawn(player);

        PlayerStats stats = new PlayerStats(player);
        killingMachine.addListener(stats);

        // Setup world generation
        worldGenerator = new WorldGenerator(factory, context.getGameCamera().getInternal(), player);
        context.getBehaviorManager().apply(new CollisionHandler(killingMachine));

        // Setup UI
        Tooltip.getInstance().init(context.getWorldStage());
        TemplateService.initSystems(bulletMachine, context);

        Music music = SharedAssetManager.getInstance().get(Assets.Musics.DIVE_INTO_THE_VOID, Music.class);

        music.setLooping(true);
        AudioManager.getInstance().setVolume(0.3f);
        AudioManager.getInstance().fadeInMusic(music, 10f);

        PlayerScoreLabel points = new PlayerScoreLabel(stats);
        points.setPosition(35, Gdx.graphics.getHeight() - 50);
        context.getStage().addActor(points);
    }

    @Override
    protected void onUpdate(float delta) {
        if (!resetCameraY) {
            resetCameraY = true;
            camera.position.y = 0;
        }
        movement.update(delta);
        worldGenerator.update(delta);
        cameraController.update(delta);
        super.onUpdate(delta);
    }

    @Override
    public void dispose() {
        super.dispose();
        Tooltip.getInstance().clear();
        starTextureFactory.dispose();
    }
}
