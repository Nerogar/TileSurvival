package de.nerogar.game.entity.playerClass;

import de.nerogar.game.entity.EntityPlayer;
import de.nerogar.game.weapon.*;

public class Engineer extends PlayerClass {

	public Engineer(EntityPlayer player) {
		super(player, 120, 100, 2.5f, 0);
	}

	@Override
	protected void initWeaponsUpgrades() {
		weaponUpgrades[0][0] = new GuardTower(player, 5, 3.0f);
		weaponUpgrades[0][1] = new GuardTower(player, 20, 3.0f);
		weaponUpgrades[0][2] = new GuardTower(player, 50, 3.0f);

		weaponUpgrades[1][0] = new TeamRestore(player, 5, 3.0f);
		weaponUpgrades[1][1] = new TeamRestore(player, 15, 3.0f);
		weaponUpgrades[1][2] = new TeamRestore(player, 25, 3.0f);

		weaponUpgrades[2][0] = new Bomb(player, 5, 3.0f);
		weaponUpgrades[2][1] = new Bomb(player, 15, 3.0f);
		weaponUpgrades[2][2] = new Bomb(player, 25, 3.0f);
	}
}
