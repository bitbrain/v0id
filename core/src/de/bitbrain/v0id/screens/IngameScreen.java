package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.ParticleEffect;
import com.badlogic.gdx.graphics.g2d.ParticleEffectPool;
import com.badlogic.gdx.utils.Array;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.audio.AudioManager;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.BulletMachine;
import de.bitbrain.v0id.core.CameraController;
import de.bitbrain.v0id.core.GameObjectFactory;
import de.bitbrain.v0id.core.KillingMachine;
import de.bitbrain.v0id.core.Respawner;
import de.bitbrain.v0id.core.TemplateService;
import de.bitbrain.v0id.core.WeaponFactory;
import de.bitbrain.v0id.core.movement.ObjectMover;
import de.bitbrain.v0id.graphics.Colors;
import de.bitbrain.v0id.graphics.ParallaxRenderLayer;
import de.bitbrain.v0id.graphics.StarTextureFactory;
import de.bitbrain.v0id.input.PlayerMovement;
import de.bitbrain.v0id.levelgen.LevelBounds;
import de.bitbrain.v0id.levelgen.WorldGenerator;
import de.bitbrain.v0id.ui.Tooltip;

public class IngameScreen extends AbstractScreen {

    private Camera camera;

    private final StarTextureFactory starTextureFactory = new StarTextureFactory();

    private CameraController cameraController;

    private BulletMachine bulletMachine;

    private Respawner respawner;

    private PlayerMovement movement;

    private WorldGenerator worldGenerator;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        camera = context.getGameCamera().getInternal();
        respawner = new Respawner(camera);

        context.getScreenTransitions().in(1f);

        // Define game world
        context.getGameWorld().setBounds(new LevelBounds());

        // Setup background
        setBackgroundColor(Colors.DARK_SORROW);
        ParallaxRenderLayer backgroundLayer = new ParallaxRenderLayer(context.getGameCamera().getInternal());
        context.getRenderPipeline().set(RenderPipeIds.BACKGROUND, backgroundLayer );

        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 3,30), 0.98f).setAlpha(0.5f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 4,17), 0.85f).setAlpha(0.55f);
        backgroundLayer.addLayer(starTextureFactory.create(Gdx.graphics.getHeight() / 2, 5,12), 0.72f).setAlpha(0.6f);

        // Setup Camera
        cameraController = new CameraController(context.getGameCamera());

        // Setup weapon systems
        KillingMachine killingMachine = new KillingMachine(context.getGameWorld(), respawner);
        bulletMachine = new BulletMachine(context.getGameWorld(), context.getBehaviorManager(), killingMachine);
        WeaponFactory weaponFactory = new WeaponFactory(bulletMachine, context.getBehaviorManager());

        ObjectMover mover = new ObjectMover();
        context.getBehaviorManager().apply(mover);

        // Setup player
        GameObjectFactory factory = new GameObjectFactory(context.getGameWorld(), context.getBehaviorManager(), weaponFactory);
        GameObject player = factory.spawnShip(TemplateService.shipTemplates[1], 0f, 0f, false);
        player.setAttribute(Attribute.PLAYER, true);
        movement = new PlayerMovement(player, mover, killingMachine, context.getGameCamera().getInternal());
        respawner.respawn(player);

        // Setup world generation
        worldGenerator = new WorldGenerator(factory, context.getGameCamera().getInternal(), player);

        // Setup UI
        Tooltip.getInstance().init(context.getWorldStage());
        TemplateService.initSystems(bulletMachine, context);

        Music music = SharedAssetManager.getInstance().get(Assets.Musics.DIVE_INTO_THE_VOID, Music.class);

        music.setLooping(true);
        AudioManager.getInstance().setVolume(0.3f);
        AudioManager.getInstance().fadeInMusic(music, 10f);

        ParticleEffect effect = SharedAssetManager.getInstance().get(Assets.Particles.SLIME, ParticleEffect.class);
        ParticleEffectPool effectPool = new ParticleEffectPool(effect, 50, 200);
        Array<ParticleEffectPool.PooledEffect> additiveEffects;
        Array<ParticleEffectPool.PooledEffect> normalEffects;
    }

    @Override
    protected void onUpdate(float delta) {
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
