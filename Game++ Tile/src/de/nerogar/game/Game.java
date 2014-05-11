package de.nerogar.game;

import static org.lwjgl.opengl.GL11.*;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;

import de.nerogar.game.graphics.GuiIngame;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundManager;

public class Game {
	public static String version = "1.0";
	public static String username = "nerogar";
	public static int port = 34543;
	public static String host = "localhost";

	private RenderEngine renderEngine;
	private Map map;

	final int WIDTH = 1280;
	final int HEIGHT = 720;
	final int FRAMERATE = 60;

	private GuiIngame guiIngame;

	private Sound bgMusic = new Sound(new String[] { "music1.ogg" }, new Vector(0, 0), new Vector(1000, 1000), true, 0.5f, 1f);

	public Game() {
		renderEngine = new RenderEngine();
		renderEngine.init(WIDTH, HEIGHT);
	}

	public void run() {
		//bgMusic.play();
		while (!Display.isCloseRequested()) {
			//long time1 = System.nanoTime();
			update();
			render();
			Display.sync(FRAMERATE);
			Display.update();
			glClear(GL_COLOR_BUFFER_BIT | GL_DEPTH_BUFFER_BIT);
			//long time2 = System.nanoTime();
			//System.out.println("time: " + ((time2 - time1) / 1000000d));
		}
		SoundManager.shutdown();
		if (map != null) map.cleanup();
	}

	private void update() {
		SoundManager.update();
		InputHandler.update(this);

		if (map == null) {
			startUpGui();
		}
		if (map != null) {
			map.update(1f / FRAMERATE);
			float listenerX = (float) map.getOffsX() + 0.5f * WIDTH / Map.TILE_RENDER_SIZE;
			float listenerY = (float) map.getOffsY() + 0.5f * HEIGHT / Map.TILE_RENDER_SIZE;
			SoundManager.recalculateListener(new Vector(listenerX, listenerY));
		}

	}

	private void startUpGui() {
		if (InputHandler.isKeyPressed(Keyboard.KEY_H)) {
			System.out.println("asdasd");
			map = MapLoader.loadMap(Map.SERVER_WORLD, "map.png");

			guiIngame = new GuiIngame(map.getPlayer());
		} else if (InputHandler.isKeyPressed(Keyboard.KEY_C)) {
			map = MapLoader.loadMap(Map.CLIENT_WORLD, "map.png");

			guiIngame = new GuiIngame(map.getPlayer());
		}
	}

	private void render() {
		if (map != null) {
			map.render();
			guiIngame.render();
		}
	}

	public static void main(String[] args) {
		Game game = new Game();
		game.run();
	}

}
