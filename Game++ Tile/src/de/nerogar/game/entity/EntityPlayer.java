package de.nerogar.game.entity;

import static org.lwjgl.opengl.GL11.glColor3f;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.Display;

import de.nerogar.game.*;
import de.nerogar.game.entity.playerClass.*;
import de.nerogar.game.graphics.Light;
import de.nerogar.game.network.*;
import de.nerogar.game.weapon.Weapon;

public class EntityPlayer extends Entity {

	private PlayerClass playerClass = null;
	public int pClass;

	private float nextEnergyRestore;

	public EntityPlayer(Map map, Vector pos) {
		super(map, pos, new Vector(1.0f), 100, true);
		//playerClass = new Mage(this);
		faction = FACTION_PLAYER;

		light = new Light(new Vector(), 4f, 1.0f);
	}

	@Override
	public void update(float time) {
		super.update(time);
	}

	public Weapon getSelectedWeapon() {
		return getPlayerClass().getSelectedWeapon();
	}

	public void updateInput(float time, Client client) {
		float[] sendPos = new float[2];
		boolean newPos = false;

		Vector dir = new Vector();
		if (Keyboard.isKeyDown(Keyboard.KEY_D)) {
			dir.addX(moveSpeed * time * speedmult);
			newPos = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_A)) {
			dir.addX(-moveSpeed * time * speedmult);
			newPos = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_W)) {
			dir.addY(-moveSpeed * time * speedmult);
			newPos = true;
		}
		if (Keyboard.isKeyDown(Keyboard.KEY_S)) {
			dir.addY(moveSpeed * time * speedmult);
			newPos = true;
		}
		if (dir.getX() != 0 || dir.getY() != 0) move(dir);

		if (client != null && newPos) {
			PacketPlayerPosition playerPositionPacket = new PacketPlayerPosition();
			playerPositionPacket.playerID = id;
			sendPos[0] = pos.getX();
			sendPos[1] = pos.getY();
			playerPositionPacket.playerPosition = sendPos;
			playerPositionPacket.playerDirection = facingDir;
			Game.game.client.sendPacket(playerPositionPacket);
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_1)) {
			getPlayerClass().selectWeapon(0);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_2)) {
			getPlayerClass().selectWeapon(1);
		} else if (Keyboard.isKeyDown(Keyboard.KEY_3)) {
			getPlayerClass().selectWeapon(2);
		}
		if (InputHandler.isMouseButtonPressed(1)) {
			getPlayerClass().selectNextWeapon();
		}

		if (Mouse.isButtonDown(0) && health > 0) {

			Vector target = new Vector();

			target.setX(((float) Mouse.getX()) / Map.TILE_RENDER_SIZE + map.getOffsX());
			target.setY((float) (Display.getHeight() - Mouse.getY()) / Map.TILE_RENDER_SIZE + map.getOffsY());

			Weapon weapon = getPlayerClass().getSelectedWeapon();

			if (weapon.cooldown <= 0f && energy >= weapon.energyCost && weapon.canActivate()) {
				if (client == null) getPlayerClass().getSelectedWeapon().start(target);
				weapon.cooldown = weapon.maxCooldown;
				playerClass.setCurrentWeaponUsed();

				if (client != null) {
					PacketActivateWeapon activateWeaponPacket = new PacketActivateWeapon();
					activateWeaponPacket.targetPosition = new float[] { target.getX(), target.getY() };
					activateWeaponPacket.playerID = id;
					activateWeaponPacket.selectedWeapon = getPlayerClass().selectedWeapon;
					Game.game.client.sendPacket(activateWeaponPacket);
				} else {
					energy -= getPlayerClass().getSelectedWeapon().energyCost;
				}
			}
		}

		if (Keyboard.isKeyDown(Keyboard.KEY_P)) {
			getPlayerClass().activateWeaponDebugTimes();
		}

	}

	public void updateStats(float time) {
		getPlayerClass().updateWeapons(time);

		if (nextEnergyRestore <= 0 && energy < maxEnergy) {
			nextEnergyRestore = 0.5f;
			energy += 1;
		}
		nextEnergyRestore -= time;
	}

	@Override
	public void onDie() {
		//TODO game end
	}

	public PlayerClass getPlayerClass() {
		return playerClass;
	}

	public void setPlayerClass(PlayerClass playerClass) {
		this.playerClass = playerClass;
	}

	@Override
	public void renderAfterShader() {

		float healthPercent = (float) health / maxHealth;
		glColor3f(0.0f, 0.1f, 0.1f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(0.0f, 0.5f, 0.0f);
		RenderHelper.renderLine((pos.getX() - map.getOffsX()) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, (pos.getX() - map.getOffsX() + dimension.getX() * healthPercent) * Map.TILE_RENDER_SIZE, (pos.getY() - map.getOffsY()) * Map.TILE_RENDER_SIZE, 2);
		glColor3f(1, 1, 1);

	}

}
