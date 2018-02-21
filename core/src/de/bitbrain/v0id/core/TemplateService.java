package de.bitbrain.v0id.core;


import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
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
                    0.35f,
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
            // VIPER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_VIPER},
                    GameObjectType.VIPER,
                    5,
                    1f,
                    7f,
                    weaponTemplates[1], // LASER CANNON
                    0f,
                    500f,
                    26.3f
            ),
            // RAIDER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_RAIDER},
                    GameObjectType.RAIDER,
                    5,
                    1f,
                    10f,
                    weaponTemplates[0], // PLASMA CANNON
                    0f,
                    500f,
                    26.3f
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
