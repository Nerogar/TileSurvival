package de.nerogar.game.graphics.gui;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.InputHandler;
import de.nerogar.game.Vector;
import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.graphics.gui.GuiIngame;
import de.nerogar.game.network.Server;

public class GuiBank {

	public static final Gui GUI_NONE = new GuiNone();
	public static final Gui GUI_TITLE = new GuiTitle();
	public static final Gui GUI_LOBBY_HOST = new GuiLobbyHost();
	public static final Gui GUI_LOBBY_CLIENT = new GuiLobbyClient();
	public static final Gui GUI_INGAME = new GuiIngame();
	
	private static Gui selectedGui = GUI_TITLE;

	public static void selectGui(Gui gui) {
		selectedGui = gui;
	}

	public static void update() {
		Vector mousePos = new Vector(Mouse.getX(), Display.getHeight() - Mouse.getY());
		if (InputHandler.isMouseButtonPressed(0))
			selectedGui.clickAt(mousePos, 0);
		if (InputHandler.isMouseButtonPressed(1))
			selectedGui.clickAt(mousePos, 1);
		if (InputHandler.isMouseButtonPressed(2))
			selectedGui.clickAt(mousePos, 2);
		selectedGui.update();
	}

	public static void render() {
		selectedGui.render();
	}

}