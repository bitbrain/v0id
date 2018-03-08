package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.graphics.SpriteHealthRenderer;

public class TemplateService {

    // = = = WEAPON SYSTEMS = = =

    private static final WeaponTemplate[] weaponTemplates = {
            new WeaponTemplate(
                    BulletType.PLASMA,
                    Assets.Textures.BULLET_PLASMA,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME,
                    0.25f,
                    0.0f,
                    650f,
                    38f
            ),
            new WeaponTemplate(
                    BulletType.LASER,
                    Assets.Textures.BULLET_LASER,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME_RED,
                    1f,
                    0.0f,
                    50f,
                    35f
            ),
            new WeaponTemplate(
                    BulletType.HEAVY_LASER,
                    Assets.Textures.BULLET_LASER,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME_RED,
                    0.4f,
                    0.0f,
                    30f,
                    45f
            )
    };


    // = = = ENEMY SYSTEMS = = =

    public static final ShipSpawnTemplate[] shipTemplates = {

            // DESTROYER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_DESTROYER},
                    GameObjectType.DESTROYER,
                    8,
                    1f,
                    12.3f,
                    weaponTemplates[2], // HEAVY LASER CANNON
                    0f,
                    20f,
                    2.3f,
                    1000
            ),
            // RAIDER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_RAIDER},
                    GameObjectType.RAIDER,
                    GameConfig.PLAYER_HEALTH_COUNT,
                    1f,
                    5f,
                    weaponTemplates[0], // PLASMA CANNON
                    0f,
                    700f,
                    46.3f,
                    250
            ),

            // VIPER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_VIPER},
                    GameObjectType.VIPER,
                    3,
                    1f,
                    8.3f,
                    weaponTemplates[1], // LASER CANNON
                    0f,
                    300f,
                    26.3f,
                    250
            ),
            // BOMBER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_BOMBER},
                    GameObjectType.BOMBER,
                    3,
                    1f,
                    7f,
                    null, // no weapon
                    0f,
                    370f,
                    35.3f,
                    400
            )
    };

    public static void initSystems(BulletMachine bulletMachine, GameContext context) {
        for (WeaponTemplate template : weaponTemplates) {
            bulletMachine.register(template.type, template.size);
            context.getRenderManager().register(template.type, new SpriteRenderer(template.assetId));
        }
        for (ShipSpawnTemplate template : shipTemplates) {
            context.getRenderManager().register(template.type, new SpriteHealthRenderer(template.life, template.assetIds));
        }
    }
}
