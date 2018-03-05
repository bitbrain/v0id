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
                    0.15f,
                    0.0f,
                    650f,
                    35f
            ),
            new WeaponTemplate(
                    BulletType.LASER,
                    Assets.Textures.BULLET_LASER,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME_RED,
                    1.5f,
                    0.0f,
                    450f,
                    35f
            )
    };


    // = = = ENEMY SYSTEMS = = =

    public static final ShipSpawnTemplate[] shipTemplates = {

            // DESTROYER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_DESTROYER},
                    GameObjectType.DESTROYER,
                    6,
                    1f,
                    12.3f,
                    weaponTemplates[1], // LASER CANNON
                    0f,
                    100f,
                    6.3f,
                    250
            ),
            // RAIDER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_RAIDER},
                    GameObjectType.RAIDER,
                    GameConfig.PLAYER_HEALTH_COUNT,
                    1f,
                    10f,
                    weaponTemplates[0], // PLASMA CANNON
                    0f,
                    500f,
                    26.3f,
                    500
            ),

            // VIPER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_VIPER},
                    GameObjectType.VIPER,
                    3,
                    1f,
                    4.3f,
                    weaponTemplates[1], // LASER CANNON
                    0f,
                    300f,
                    26.3f,
                    700
            ),
            // BOMBER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_BOMBER},
                    GameObjectType.BOMBER,
                    10,
                    1f,
                    7f,
                    null, // no weapon
                    0f,
                    50f,
                    6.3f,
                    300
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
