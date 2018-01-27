package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.graphics.g2d.freetype.FreeTypeFontGenerator;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Scaling;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.GraphicsFactory;
import de.bitbrain.braingdx.graphics.lighting.LightingManager;
import de.bitbrain.braingdx.graphics.lighting.PointLightBehavior;
import de.bitbrain.braingdx.graphics.pipeline.layers.RenderPipeIds;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.screens.AbstractScreen;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.Attribute;
import de.bitbrain.v0id.core.BulletMachine;
import de.bitbrain.v0id.core.BulletType;
import de.bitbrain.v0id.core.CameraController;
import de.bitbrain.v0id.core.GameObjectFactory;
import de.bitbrain.v0id.core.KillingMachine;
import de.bitbrain.v0id.core.Respawner;
import de.bitbrain.v0id.core.ShootingBehavior;
import de.bitbrain.v0id.core.Weapon;
import de.bitbrain.v0id.graphics.BitmapFontBaker;
import de.bitbrain.v0id.graphics.Colors;
import de.bitbrain.v0id.graphics.ParallaxRenderLayer;
import de.bitbrain.v0id.graphics.SpriteHealthRenderer;
import de.bitbrain.v0id.graphics.StarTextureFactory;
import de.bitbrain.v0id.levelgen.LevelBounds;
import de.bitbrain.v0id.levelgen.WorldGenerator;

public class IngameScreen extends AbstractScreen {

    private GameObject ship;

    private GameObjectFactory factory;

    private Camera camera;

    private final StarTextureFactory starTextureFactory = new StarTextureFactory();

    private CameraController cameraController;

    private BulletMachine bulletMachine;

    private Respawner respawner;

    private WorldGenerator worldGenerator;

    private GameContext context;

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
        this.context = context;
        camera = context.getGameCamera().getInternal();
        respawner = new Respawner(camera);

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
        bulletMachine.register(BulletType.PLASMA, 32f);
        bulletMachine.register(BulletType.LASER, 32f);
        context.getRenderManager().register(BulletType.PLASMA, new SpriteRenderer(Assets.Textures.BULLET_PLASMA));
        context.getRenderManager().register(BulletType.LASER, new SpriteRenderer(Assets.Textures.BULLET_LASER));

        // Setup player
        GameObjectFactory factory = new GameObjectFactory(context.getGameWorld(), context.getBehaviorManager(), bulletMachine);
        ship = context.getGameWorld().addObject();
        ship.setType("raider");
        ship.setAttribute(Attribute.HEALTH, 3);
        ship.setAttribute(Attribute.PLAYER, true);
        ship.setDimensions(64, 64);
        Map<Integer, String> raiderTextureMap = new HashMap<Integer, String>();
        raiderTextureMap.put(1, Assets.Textures.SHIP_RAIDER);
        context.getRenderManager().register("raider", new SpriteHealthRenderer(raiderTextureMap));
        ship.setPosition(Gdx.graphics.getWidth() / 2f - ship.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - ship.getHeight() / 2f);
        ShootingBehavior shootingBehavior = new ShootingBehavior();
        shootingBehavior.addWeapon(new Weapon(BulletType.PLASMA, bulletMachine, 0.3f, 0.0f, 700f));
        context.getBehaviorManager().apply(shootingBehavior, ship);

        Map<Integer, String> viperTextureMap = new HashMap<Integer, String>();
        viperTextureMap.put(1, Assets.Textures.SHIP_VIPER);
        context.getRenderManager().register("viper", new SpriteHealthRenderer(viperTextureMap));

        // Lighting
        //context.getBehaviorManager().apply(new PointLightBehavior(Color.WHITE, 1200f, context.getLightingManager()), ship);
        //context.getLightingManager().setAmbientLight(Colors.LIGHT_SORROW);
        //LightingManager.LightingConfig config = context.getLightingManager().new LightingConfig();
        //config.diffuseLighting(true);
        //context.getLightingManager().setConfig(config);

        // Setup world objects
        Map<Integer, String> blockTextureMap = new HashMap<Integer, String>();
        blockTextureMap.put(1, Assets.Textures.OBJECT_BLOCK_LAVA);
        blockTextureMap.put(2, Assets.Textures.OBJECT_BLOCK_DAMAGED);
        blockTextureMap.put(3, Assets.Textures.OBJECT_BLOCK_DAMAGED);
        blockTextureMap.put(4, Assets.Textures.OBJECT_BLOCK_CRACKED);
        blockTextureMap.put(5, Assets.Textures.OBJECT_BLOCK_CRACKED);
        blockTextureMap.put(6, Assets.Textures.OBJECT_BLOCK);
        context.getRenderManager().register("block", new SpriteHealthRenderer(blockTextureMap));

        // Setup world generation
        worldGenerator = new WorldGenerator(factory, context.getGameCamera().getInternal());

        // Setup UI
        NinePatch patch = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.UI_BACKGROUND_BAR, Texture.class), 32);
        Image image = new Image(new NinePatchDrawable(patch));
        image.setBounds(16f, 16f, 78f, 256f);
        context.getStage().addActor(image);
        NinePatch patch2 = GraphicsFactory.createNinePatch(SharedAssetManager.getInstance().get(Assets.Textures.UI_FOREGROUND_HEALTH, Texture.class), 36);
        Image overlay = new Image(new NinePatchDrawable(patch2));
        overlay.setBounds(16f, 16f, 78f, 256f);
        context.getStage().addActor(overlay);

        Label.LabelStyle style = new Label.LabelStyle();
        FreeTypeFontGenerator.FreeTypeFontParameter param = new FreeTypeFontGenerator.FreeTypeFontParameter();
        param.color = Color.WHITE;
        param.mono = false;
        param.size = 48;

        style.font = BitmapFontBaker.bake(Assets.Fonts.BUNGEE, param);
        Label points0 = new Label("8495", style);
        points0.setColor(Colors.NEON_BLUE);
        Label points = new Label("8495", style);
        points.setColor(Colors.NEON_LIME);
        points0.setPosition(16f, Gdx.graphics.getHeight() - points.getPrefHeight() - 18f);
        points.setPosition(16f, Gdx.graphics.getHeight() - points.getPrefHeight() - 12f);
        context.getStage().addActor(points0);
        context.getStage().addActor(points);
    }

    @Override
    protected void onUpdate(float delta) {
        worldGenerator.update(delta);
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

        // Correct ship position
        float cameraLeft = camera.position.x - camera.viewportWidth / 2f;
        float cameraRight = camera.position.x + camera.viewportWidth / 2f;
        float cameraTop = camera.position.y + camera.viewportHeight / 2f;
        float cameraBottom = camera.position.y - camera.viewportHeight / 2f;

        if (ship.getLeft() < cameraLeft) {
            ship.setPosition(cameraLeft, ship.getTop());
        }
        if (ship.getLeft() + ship.getWidth() > cameraRight) {
            ship.setPosition(cameraRight - ship.getWidth(), ship.getTop());
        }
        if (ship.getTop() + ship.getHeight() > cameraTop) {
            ship.setPosition(ship.getLeft(), cameraTop - ship.getHeight());
        }

        if (ship.getTop() < cameraBottom) {
            respawner.respawn(ship, GameConfig.PLAYER_HEALTH);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        starTextureFactory.dispose();
    }
}
