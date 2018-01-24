package de.bitbrain.v0id.screens;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.Color;

import java.util.HashMap;
import java.util.Map;

import de.bitbrain.braingdx.BrainGdxGame;
import de.bitbrain.braingdx.GameContext;
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
import de.bitbrain.v0id.core.Respawner;
import de.bitbrain.v0id.core.ShootingBehavior;
import de.bitbrain.v0id.core.Weapon;
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

    public IngameScreen(BrainGdxGame game) {
        super(game);
    }

    @Override
    protected void onCreate(GameContext context) {
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
        bulletMachine = new BulletMachine(context.getGameWorld(), context.getBehaviorManager(), respawner);
        bulletMachine.register(BulletType.PLASMA, 32f);
        bulletMachine.register(BulletType.LASER, 32f);
        context.getRenderManager().register(BulletType.PLASMA, new SpriteRenderer(Assets.Textures.BULLET_PLASMA));
        context.getRenderManager().register(BulletType.LASER, new SpriteRenderer(Assets.Textures.BULLET_LASER));

        // Setup player
        GameObjectFactory factory = new GameObjectFactory(context.getGameWorld(), context.getBehaviorManager(), bulletMachine);
        ship = context.getGameWorld().addObject();
        ship.setType("ship");
        ship.setAttribute(Attribute.HEALTH, 3);
        ship.setDimensions(64, 64);
        context.getRenderManager().register("ship", new SpriteRenderer(Assets.Textures.SHIP_RAIDER));
        context.getRenderManager().register("viper", new SpriteRenderer(Assets.Textures.SHIP_VIPER));
        ship.setPosition(Gdx.graphics.getWidth() / 2f - ship.getWidth() / 2f, Gdx.graphics.getHeight() / 2f - ship.getHeight() / 2f);
        ShootingBehavior shootingBehavior = new ShootingBehavior();
        shootingBehavior.addWeapon(new Weapon(BulletType.PLASMA, bulletMachine, 0.3f, 0.0f, 700f));
        context.getBehaviorManager().apply(shootingBehavior, ship);

        // Lighting
        //context.getBehaviorManager().apply(new PointLightBehavior(Color.WHITE, 1200f, context.getLightingManager()), ship);
        //context.getLightingManager().setAmbientLight(Colors.LIGHT_SORROW);
        //LightingManager.LightingConfig config = context.getLightingManager().new LightingConfig();
        //config.diffuseLighting(true);
        //context.getLightingManager().setConfig(config);

        // Setup world objects
        Map<Integer, String> map = new HashMap<Integer, String>();
        map.put(1, Assets.Textures.OBJECT_BLOCK_LAVA);
        map.put(2, Assets.Textures.OBJECT_BLOCK_DAMAGED);
        map.put(3, Assets.Textures.OBJECT_BLOCK_DAMAGED);
        map.put(4, Assets.Textures.OBJECT_BLOCK_CRACKED);
        map.put(5, Assets.Textures.OBJECT_BLOCK_CRACKED);
        map.put(6, Assets.Textures.OBJECT_BLOCK);
        context.getRenderManager().register("block", new SpriteHealthRenderer(map));

        // Setup world generation
        worldGenerator = new WorldGenerator(factory, context.getGameCamera().getInternal());
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
            respawner.respawn(ship);
        }
    }

    @Override
    public void dispose() {
        super.dispose();
        starTextureFactory.dispose();
    }
}
