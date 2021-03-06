package de.bitbrain.v0id.assets;

public interface Assets {

    interface Textures {
        // GAME OBJECTS
        String SHIP_DESTROYER = "textures/destroyer.png";
        String SHIP_RAIDER = "textures/raider.png";
        String SHIP_VIPER = "textures/viper.png";
        String SHIP_BOMBER = "textures/bomber.png";
        String BULLET_PLASMA = "textures/plasma.png";
        String BULLET_LASER = "textures/laser.png";
        String BULLET_PHOTON = "textures/photon.png";
        String CONSUMABLE_HEALTH = "textures/consumable-health-canister.png";
        String CONSUMABLE_WEAPON_UPGRADE = "textures/consumable-weapon-upgrade.png";

        // UI
        String UI_HEALTH = "textures/ui-health.png";
        String UI_HEALTH_BACKGROUND = "textures/ui-health-background.png";
        String LOGO_BITBRAIN = "textures/bitbrain.png";
        String LOGO_VOID = "textures/void-logo.png";
    }

    interface Particles {
        String SLIME = "particles/slime.p";
        String SLIME_RED = "particles/slime-red.p";
        String SLIME_BLUE = "particles/slime-blue.p";
        String CONSUMABLE_HEALTH = "particles/slime.p";
        String CONSUMABLE_WEAPON_UPGRADE = "particles/slime.p";
    }

    interface Musics {
        String DIVE_INTO_THE_VOID = "musics/dive_into_the_void.ogg";
        String INTRO = "musics/intro.ogg";
    }

    interface Sounds {
        String SOUND_SHOT_01 = "sounds/shot01.ogg";
        String DEATH = "sounds/death.ogg";
        String EXPLODE_01 = "sounds/explode01.ogg";
        String HIT = "sounds/hit.ogg";
        String RESPAWN = "sounds/respawn.ogg";
        String ZOOM = "sounds/zoom.ogg";
        String EXIT = "sounds/exit.ogg";
        String POINT = "sounds/point.ogg";
        String WEAPON_UP = "sounds/weapon_up.ogg";
        String HEALTH_UP = "sounds/life_up.ogg";
    }

    interface Fonts {
        String MAIN = "fonts/hydrant.ttf";
    }
}
