package de.nerogar.game.weapon;

import de.nerogar.game.Vector;
import de.nerogar.game.entity.*;
import de.nerogar.game.sound.Sound;
import de.nerogar.game.sound.SoundCategory;

public class Heal extends Weapon {

	public float MAX_RESTORE_DISTANCE = 5.0f;

	private Sound activateSound = new Sound(SoundCategory.EFFECT, "heal1.ogg");
	
	public Heal(Entity owner, int damage, float cooldown) {
		super(owner, damage, cooldown, 40);
		textureID = 8 * 3 + 0;
	}

	@Override
	public void start(Vector target) {
		for (Entity entity : owner.map.getEntities()) {
			if (entity.faction == owner.faction && !(entity instanceof EntityWeapon)) {
				float entityDist = owner.getCenter().subtract(entity.getCenter()).getValue();
				if (entityDist <= MAX_RESTORE_DISTANCE) {
					processEffect(entity);
				}
			}
		}
		
		activateSound.setPosition(owner.getCenter());
		activateSound.randomizePitch(0.4f);
		activateSound.play();
	}

	@Override
	public boolean canActivate() {
		return true;
	}

	@Override
	public void processEffect(Entity target) {
		target.health = Math.min(target.health + damage, target.maxHealth);
	}

}
