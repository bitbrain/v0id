package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.ai.ChasingEnemyBehavior;
import de.bitbrain.v0id.ai.CirclingEnemyBehavior;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.core.consumables.HealthConsumable;
import de.bitbrain.v0id.core.consumables.WeaponUpgradeConsumable;
import de.bitbrain.v0id.graphics.SpriteHealthRenderer;

public class TemplateService {

    // = = = CONSUMABLES = = =
    public static final ConsumableTemplate[] consumableTemplates = {
            new ConsumableTemplate(
                    GameObjectType.CONSUMABLE_HEALTH,
                    Assets.Textures.CONSUMABLE_HEALTH,
                    Assets.Particles.CONSUMABLE_HEALTH,
                    new HealthConsumable(),
                    0.04f
            ),
            new ConsumableTemplate(
                    GameObjectType.CONSUMABLE_WEAPON_UPGRADE,
                    Assets.Textures.CONSUMABLE_WEAPON_UPGRADE,
                    Assets.Particles.CONSUMABLE_WEAPON_UPGRADE,
                    new WeaponUpgradeConsumable(),
                    0.2f
            ),
    };

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
                    38f,
                    null // always shoot
            ),
            new WeaponTemplate(
                    BulletType.LASER,
                    Assets.Textures.BULLET_LASER,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME_RED,
                    0.5f,
                    0.0f,
                    50f,
                    35f,
                    new int[]{1,1,0,0,0}
            ),
            new WeaponTemplate(
                    BulletType.PHOTON,
                    Assets.Textures.BULLET_PHOTON,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME_BLUE,
                    0.3f,
                    0.0f,
                    1f,
                    35f,
                    new int[]{1,1,1,0,0,0,0}
            )
    };


    // = = = ENEMY SYSTEMS = = =

    public static final ShipSpawnTemplate[] shipTemplates = {

            // DESTROYER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_DESTROYER},
                    GameObjectType.DESTROYER,
                    12,
                    0.02f,
                    .3f,
                    weaponTemplates[2], // HEAVY LASER CANNON
                    0f,
                    50f,
                    30.3f,
                    1000,
                    CirclingEnemyBehavior.factory()
            ),
            // RAIDER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_RAIDER},
                    GameObjectType.RAIDER,
                    GameConfig.PLAYER_HEALTH_COUNT,
                    1f,
                    1f,
                    weaponTemplates[0], // PLASMA CANNON
                    0f,
                    700f,
                    46.3f,
                    250,
                    null
            ),

            // VIPER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_VIPER},
                    GameObjectType.VIPER,
                    3,
                    0.5f,
                    1.3f,
                    weaponTemplates[1], // LASER CANNON
                    0f,
                    300f,
                    26.3f,
                    100,
                    null
            ),
            // BOMBER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_BOMBER},
                    GameObjectType.BOMBER,
                    2,
                    0.4f,
                    2f,
                    null, // no weapon
                    0f,
                    450f,
                    17.3f,
                    500,
                    ChasingEnemyBehavior.factory()
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
        for (ConsumableTemplate template : consumableTemplates) {
            context.getRenderManager().register(template.type, new SpriteRenderer(template.assetId));
        }
    }
}
