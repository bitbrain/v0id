package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.Batch;

import aurelienribon.tweenengine.BaseTween;
import aurelienribon.tweenengine.Tween;
import aurelienribon.tweenengine.TweenCallback;
import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.graphics.pipeline.AbstractRenderLayer;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.tweens.SharedTweenManager;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.Colors;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.BulletMachine;
import de.bitbrain.v0id.core.CameraController;
import de.bitbrain.v0id.core.CollisionHandler;
import de.bitbrain.v0id.core.ConsumableSpawner;
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
import de.bitbrain.v0id.ui.HealthBar;
import de.bitbrain.v0id.ui.PlayerScoreLabel;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

public class IngameScreen extends AbstractScreen implements KillingMachine.KillingListener {

    private Camera camera;

    private final StarTextureFactory starTextureFactory = new StarTextureFactory();

    private CameraController cameraController;

    private PlayerMovement movement;

    private WorldGenerator worldGenerator;

    private ParticleManager particleManager;

    private PlayerStats stats;

    private boolean resetCameraY = false;

    private GameContext context;

    private ConsumableSpawner consumableSpawner;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        this.context = context;
        camera = context.getGameCamera().getInternal();
        camera.position.x = 0;
        camera.position.y = 0;
        Respawner respawner = new Respawner(camera);

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

        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 3,30), 0.98f).setAlpha(0.3f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 4,17), 0.85f).setAlpha(0.65f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 5,12), 0.72f).setAlpha(0.8f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 2,5), 0.52f).setAlpha(0.9f);

        // Setup Camera
        cameraController = new CameraController(context.getGameCamera());

        // Setup weapon systems
        KillingMachine killingMachine = new KillingMachine(context.getGameWorld(), respawner, particleManager);
        killingMachine.addListener(this);
        BulletMachine bulletMachine = new BulletMachine(context.getGameWorld(), context.getBehaviorManager(), killingMachine, particleManager);
        WeaponFactory weaponFactory = new WeaponFactory(bulletMachine, context.getBehaviorManager());

        ObjectMover mover = new ObjectMover();
        context.getBehaviorManager().apply(mover);

        // Setup player
        GameObjectFactory factory = new GameObjectFactory(context, weaponFactory);
        GameObject player = factory.spawnShip(TemplateService.shipTemplates[1], 0f, 0f, false);
        player.setAttribute(Attribute.PLAYER, true);
        movement = new PlayerMovement(player, mover, killingMachine, context.getGameCamera().getInternal());

        stats = new PlayerStats(player);
        player.setAttribute(Attribute.PLAYER_STATS, stats);

        consumableSpawner = new ConsumableSpawner(factory);

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
        points.setPosition(45, Gdx.graphics.getHeight() - 70);
        context.getStage().addActor(points);

        HealthBar healthBar = new HealthBar(stats);
        healthBar.setPosition(45f, 45f);
        context.getStage().addActor(healthBar);

        respawner.respawn(player);
    }

    @Override
    protected void onUpdate(float delta) {
        if (!resetCameraY) {
            resetCameraY = true;
            camera.position.y = 0;
            camera.update();
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

    @Override
    public void onKill(final GameObject target) {
        if (target.hasAttribute(Attribute.POINTS) && !target.hasAttribute(Attribute.PLAYER)) {
            int points = (Integer)target.getAttribute(Attribute.POINTS);
            stats.addPoints(points);
            Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP, String.valueOf(points));
            consumableSpawner.spawn(target);
        } else if (target.hasAttribute(Attribute.PLAYER)) {
            stats.reduceLifeCount();
            if (!stats.isGameOver()) {
                target.setAttribute(Attribute.HEALTH, stats.getTotalHealth());
                target.setActive(false);
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        target.setAttribute(Attribute.IMMUNE, true);
                    }
                }).delay(GameConfig.DEATH_DURATION).start(SharedTweenManager.getInstance());
                Tween.call(new TweenCallback() {
                    @Override
                    public void onEvent(int i, BaseTween<?> baseTween) {
                        target.setAttribute(Attribute.IMMUNE, false);
                        target.setActive(true);
                        target.setColor(1f, 1f, 1f,1f);
                    }
                }).delay(GameConfig.DEATH_DURATION + GameConfig.IMMUNE_DURATION).start(SharedTweenManager.getInstance());
            } else {
                target.setAttribute(Attribute.GAME_OVER, true);
                context.getScreenTransitions().out(new GameOverScreen(getGame(), stats), 1.5f);
                AudioManager.getInstance().stopMusic(Assets.Musics.DIVE_INTO_THE_VOID);
                AudioManager.getInstance().playMusic(Assets.Musics.INTRO);
            }
            Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_DEATH, "YOU DIED");
        }
    }
}
