package de.bitbrain.v0id.core;


import com.badlogic.gdx.audio.Sound;

import de.bitbrain.braingdx.GameContext;
import de.bitbrain.braingdx.assets.SharedAssetManager;
import de.bitbrain.braingdx.graphics.renderer.SpriteRenderer;
import de.bitbrain.braingdx.world.GameObject;
import de.bitbrain.v0id.GameConfig;
import de.bitbrain.v0id.assets.Assets;
import de.bitbrain.v0id.graphics.SpriteHealthRenderer;
import de.bitbrain.v0id.ui.Styles;
import de.bitbrain.v0id.ui.Tooltip;

public class TemplateService {

    // = = = CONSUMABLES = = =
    public static final ConsumableTemplate[] consumableTemplates = {
            new ConsumableTemplate(
                    GameObjectType.CONSUMABLE_HEALTH,
                    Assets.Textures.CONSUMABLE_HEALTH,
                    Assets.Particles.CONSUMABLE_HEALTH,
                    new Consumable() {
                        @Override
                        public void consume(GameObject target) {
                            if (target.hasAttribute(Attribute.PLAYER_STATS) && target.hasAttribute(Attribute.HEALTH)) {
                                PlayerStats stats = (PlayerStats)target.getAttribute(Attribute.PLAYER_STATS);
                                stats.increaseLifeCount();
                                target.setAttribute(Attribute.HEALTH, target.getAttribute(Attribute.INITIAL_HEALTH));
                                Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_HEALTH_UP, "HEALTH UP!");
                                SharedAssetManager.getInstance().get(Assets.Sounds.HEALTH_UP, Sound.class).play();
                            }
                        }
                    },
                    0.04f
            ),
            new ConsumableTemplate(
                    GameObjectType.CONSUMABLE_WEAPON_UPGRADE,
                    Assets.Textures.CONSUMABLE_WEAPON_UPGRADE,
                    Assets.Particles.CONSUMABLE_WEAPON_UPGRADE,
                    new Consumable() {
                        @Override
                        public void consume(GameObject target) {
                            if (target.hasAttribute(Attribute.WEAPON)) {
                                Weapon weapon = (Weapon)target.getAttribute(Attribute.WEAPON);
                                weapon.upgrade();
                                Tooltip.getInstance().create(target, Styles.LABEL_TEXT_TOOLTIP_WEAPON_UP, "WEAPON UPGRADED!");
                                SharedAssetManager.getInstance().get(Assets.Sounds.WEAPON_UP, Sound.class).play();
                            }
                        }
                    },
                    0.02f
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
                    BulletType.HEAVY_LASER,
                    Assets.Textures.BULLET_LASER,
                    Assets.Sounds.SOUND_SHOT_01,
                    Assets.Particles.SLIME_RED,
                    0.8f,
                    0.0f,
                    30f,
                    45f,
                    new int[]{2,1,0,0,0}
            )
    };


    // = = = ENEMY SYSTEMS = = =

    public static final ShipSpawnTemplate[] shipTemplates = {

            // DESTROYER
            new ShipSpawnTemplate(
                    new String[]{Assets.Textures.SHIP_DESTROYER},
                    GameObjectType.DESTROYER,
                    8,
                    0.05f,
                    .3f,
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
                    1f,
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
                    0.5f,
                    1.3f,
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
                    0.2f,
                    2f,
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
        for (ConsumableTemplate template : consumableTemplates) {
            context.getRenderManager().register(template.type, new SpriteRenderer(template.assetId));
        }
    }
}
