package de.nerogar.game.weapon;

import de.nerogar.game.entity.EntityFireball;
import de.nerogar.game.entity.EntityPlayer;

public class Fireball extends Weapon {

	public Fireball(EntityPlayer player, int damage, float cooldown) {
		super(player, damage, cooldown);
		textureID = 0;
		maxCooldown = 0.5f;
	}

	@Override
	public void start(float targetX, float targetY) {
		if (cooldown > 0f) return;
		cooldown = maxCooldown;

		EntityFireball fireball = new EntityFireball(player.map, player.posX, player.posY, targetX, targetY);
		player.map.spawnEntity(fireball);
	}

}
