package de.bitbrain.v0id.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import de.bitbrain.v0id.V0idGame;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
        config.resizable = true;
        config.width = 600;
        config.height = 900;
		new LwjglApplication(new V0idGame(), config);
	}
}
