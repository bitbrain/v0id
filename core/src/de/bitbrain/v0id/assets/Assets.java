package de.bitbrain.v0id.assets;

public interface Assets {

    interface Textures {
        // GAME OBJECTS
        String SHIP_RAIDER = "textures/raider.png";
        String SHIP_VIPER = "textures/viper.png";
        String BULLET_PLASMA = "textures/plasma.png";
        String BULLET_LASER = "textures/laser.png";
        String OBJECT_BLOCK = "textures/block.png";
        String OBJECT_BLOCK_CRACKED = "textures/block-cracked.png";
        String OBJECT_BLOCK_DAMAGED = "textures/block-damaged.png";
        String OBJECT_BLOCK_LAVA = "textures/block-lava.png";

        // UI
        String UI_BACKGROUND_BAR = "textures/ui-bar-background.9.png";
        String UI_FOREGROUND_HEALTH = "textures/ui-bar-foreground-health.9.png";
    }

    interface Musics {
        String DIVE_INTO_THE_VOID = "musics/dive_into_the_void.ogg";
    }

    interface Sounds {
        String SOUND_SHOT_01 = "sounds/shot01.ogg";
        String DEATH = "sounds/death.ogg";
        String EXPLODE_01 = "sounds/explode01.ogg";
        String HIT = "sounds/hit.ogg";
        String RESPAWN = "sounds/respawn.ogg";
    }

    interface Fonts {
        String BUNGEE = "fonts/bungee.ttf";
    }
}
