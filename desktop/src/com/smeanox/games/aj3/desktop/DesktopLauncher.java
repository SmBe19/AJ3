package com.smeanox.games.aj3.desktop;

import com.badlogic.gdx.backends.lwjgl.LwjglApplication;
import com.badlogic.gdx.backends.lwjgl.LwjglApplicationConfiguration;
import com.smeanox.games.aj3.AJ3;
import com.smeanox.games.aj3.Consts;

public class DesktopLauncher {
	public static void main (String[] arg) {
		LwjglApplicationConfiguration config = new LwjglApplicationConfiguration();
		config.width = Consts.DESIGN_WIDTH;
		config.height = Consts.DESIGN_HEIGHT;
		config.title = Consts.GAME_NAME;
		config.resizable = false;
		new LwjglApplication(new AJ3(), config);
	}
}
