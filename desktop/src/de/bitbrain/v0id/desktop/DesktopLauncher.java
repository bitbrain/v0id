package de.bitbrain.v0id.desktop;

import com.badlogic.gdx.Files;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.badlogic.gdx.files.FileHandle;

import javax.swing.ImageIcon;

import de.bitbrain.v0id.V0idGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = false;
        config.vSyncEnabled = true;
        config.forceExit = false;
        config.title = "v0id";
        config.width = 700;
        config.height = 1000;
        setApplicationIcon(config);
		new LwjglApplication(new V0idGame(), config);
	}



    private static void setApplicationIcon(LwjglApplicationConfiguration config) {
        try {
            Class<?> cls = Class.forName("com.apple.eawt.Application");
            Object application = cls.newInstance().getClass().getMethod("getApplication").invoke(null);

            FileHandle icon = Gdx.files.local("icons/icon.png");
            application.getClass().getMethod("setDockIconImage", java.awt.Image.class)
                    .invoke(application, new ImageIcon(icon.file().getAbsolutePath()).getImage());
        } catch (Exception e) {
            // nobody cares!
        }
        config.addIcon("icons/icon.png", Files.FileType.Internal);
    }
}
